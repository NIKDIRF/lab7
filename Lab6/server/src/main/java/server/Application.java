package server;

import collection.CollectionControl;
import collection.HashSetControl;
import command.*;
import connection.ConnectionListener;
import connection.ConnectionListenerAdvanced;
import connection.ConnectionListenerImpl;
import exception.*;
import file.*;
import io.ConsoleIO;
import io.UserIO;
import locale.ServerBundle;
import log.Log;
import request.RequestHandler;
import request.RequestHandlerImpl;
import request.RequestReader;
import request.RequestReaderImpl;
import response.Creator;
import response.ResponseCreator;
import response.ResponseSender;
import response.ResponseSenderImpl;
import studygroup.Request;
import studygroup.Response;
import util.CryptoModule;
import util.RequestOpsState;
import util.SHA224CryptoModule;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Application {
    private boolean isRunning = true;

    private RequestHandler requestHandler;
    private ResponseSender responseSender;
    private RequestReader requestReader;
    private Selector selector;
    private final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
    private final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    private final List<RequestOpsState> changeRequests = new ArrayList<>();
    private final Map<SocketChannel, Response> responseMap = new HashMap<>();

    public void start(String address, int port, String url, String login, String password) {
        Locale.setDefault(new Locale("ru", "RU"));
        Creator creator = new ResponseCreator();
        CommandHistory commandHistory = new CommandHistory();
        CommandInvoker commandInvoker = new CommandInvoker(commandHistory);
        ConnectionListener connectionListener = new ConnectionListenerAdvanced();
        requestReader = new RequestReaderImpl();
        DataManager dataManager = new PSQLDataManager();
        dataManager.start(url, login, password);
        connectionListener.setSocketAddress(new InetSocketAddress(address, port));
        UsersDAO usersDAO = null;
        StudyGroupDAO studyGroupDAO = null;
        try {
            usersDAO = new PSQLUsersDAO(dataManager.createConnection());
            studyGroupDAO = new PSQLStudyGroupDAO(dataManager.createConnection());
        } catch (SQLException e) {
            Log.getLogger().error("error", e);
        }
        CryptoModule cryptoModule = new SHA224CryptoModule();
        UserAuthModule userAuthModule = new UserAuthModule(usersDAO, cryptoModule);
        requestHandler = new RequestHandlerImpl(commandInvoker, creator, userAuthModule);
        responseSender = new ResponseSenderImpl();
        CollectionControl studyGroupManager = new HashSetControl(creator, studyGroupDAO);
        putCommands(commandInvoker, studyGroupManager, creator, commandHistory);
        putServerCommands(commandInvoker, connectionListener);
        consoleStart(commandInvoker);
        try {
            selector = connectionListener.openConnection();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }

        while (isRunning) {
            try {
                synchronized (changeRequests) {
                    for (RequestOpsState requestOpsState : changeRequests) {
                        if (requestOpsState.getType() == RequestOpsState.CHANGEOPS) {
                            SelectionKey key = requestOpsState.getSocketChannel().keyFor(selector);
                            key.interestOps(requestOpsState.getOps());
                        } else if (requestOpsState.getType() == RequestOpsState.DEREGISTER) {
                            SelectionKey key = requestOpsState.getSocketChannel().keyFor(selector);
                            key.cancel();
                            requestOpsState.getSocketChannel().close();
                        }
                    }
                    changeRequests.clear();
                }
                selector.select();
                Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
                while (selectedKeys.hasNext()) {
                    SelectionKey key = selectedKeys.next();
                    selectedKeys.remove();
                    if (!key.isValid()) {
                        continue;
                    }
                    if (key.isAcceptable()) {
                        connectionListener.accept(key);
                    } else if (key.isReadable()) {
                        readRequest(key);
                        key.interestOps(0);
                    } else if (key.isWritable()) {
                        sendResponse(key);
                        key.interestOps(0);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void readRequest(SelectionKey selectionKey) {
        new Thread(() -> {
            try {
                Request request = requestReader.getRequest(selectionKey);
                log.Log.getLogger().info(ServerBundle.getString("server.got_request"));
                handleRequest(request);
                Log.getLogger().info(request.toString());
            } catch (IOException | ClassNotFoundException e) {
                Log.getLogger().error("error", e);
            }
        }).start();
    }

    private void handleRequest(Request request) {
        fixedThreadPool.submit(() -> {
            try {
                Response response = requestHandler.handleRequest(request);
                response.setChannel(request.getChannel());
                prepareToSend(response);
            } catch (CommandNotFoundException | CommandExecutionException | AuthException e) {
                Log.getLogger().error(String.valueOf(e));
                Log.getLogger().error(Arrays.toString(e.getStackTrace()));
                Response response = new Response(e.getMessage(), false, false);
                response.setChannel(request.getChannel());
                prepareToSend(response);
            } catch (DataBaseException e) {
                Log.getLogger().error(e.getDbErrorMessage());
                Log.getLogger().error(String.valueOf(e));
                Response response = new Response(e.getMessage(), false, false);
                response.setChannel(request.getChannel());
                prepareToSend(response);
            } catch (Exception e) {
                Log.getLogger().error(Arrays.toString(e.getStackTrace()));
                Log.getLogger().error(String.valueOf(e));
                Response response = new Response(e.getMessage(), false, false);
                response.setChannel(request.getChannel());
                prepareToSend(response);
            }
        });
    }

    private void prepareToSend(Response response) {
        synchronized (changeRequests) {
            changeRequests.add(new RequestOpsState(response.getChannel(), RequestOpsState.CHANGEOPS, SelectionKey.OP_WRITE));
            synchronized (responseMap) {
                responseMap.put(response.getChannel(), response);
            }
        }
        this.selector.wakeup();
    }

    private void sendResponse(SelectionKey selectionKey) {
        cachedThreadPool.submit(() -> {
            try {
                synchronized (responseMap) {
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    responseSender.sendResponse(channel, responseMap.get(selectionKey.channel()));
                    synchronized (changeRequests) {
                        changeRequests.add(new RequestOpsState((SocketChannel) selectionKey.channel(), RequestOpsState.CHANGEOPS, SelectionKey.OP_READ));
                    }
                }
                selector.wakeup();
            } catch (IOException | ClassNotFoundException e) {
                Log.getLogger().error(String.valueOf(e));
                Log.getLogger().error(Arrays.toString(e.getStackTrace()));
            }
        });
    }

    private void consoleStart(CommandInvoker commandInvoker) {
        Thread consoleThread = new Thread(() -> {
            UserIO userIO = new ConsoleIO();
            while(!Thread.interrupted()) {
                userIO.printUserPrompt();
                try {
                    String str = userIO.readLine();
                    commandInvoker.execute(str);
                } catch (IOException | CommandNotFoundException | CommandExecutionException ioe) {
                    userIO.printErrorMessage(ioe.getMessage());
                }
            }
        });
        consoleThread.setDaemon(true);
        consoleThread.start();
    }

    private void putCommands(CommandInvoker commandInvoker, CollectionControl manager, Creator creator, CommandHistory commandHistory) {
        commandInvoker.addCommand("help", new HelpCommand(false, creator));
        commandInvoker.addCommand("info", new InfoCommand(manager, false));
        commandInvoker.addCommand("clear", new ClearCommand(manager, false));
        commandInvoker.addCommand("show", new ShowCommand(manager, false));
        commandInvoker.addCommand("remove_by_id", new RemoveByIdCommand(manager, false));
        commandInvoker.addCommand("history", new HistoryCommand(commandHistory, creator, false));
        commandInvoker.addCommand("add", new AddCommand(manager, true));
        commandInvoker.addCommand("update", new UpdateCommand(manager, true));
        commandInvoker.addCommand("add_if_max", new AddIfMaxCommand(manager, true));
        commandInvoker.addCommand("remove_greater", new RemoveGreaterCommand(manager, true));
        commandInvoker.addCommand("remove_lower", new RemoveLowerCommand(manager, true));
        commandInvoker.addCommand("print_ascending", new PrintAscendingCommand(manager, false));
        commandInvoker.addCommand("print_unique_expelled_students", new PrintUniqueExpelledStudentsCommand(manager, false));
        commandInvoker.addCommand("print_field_descending_form_of_education", new PrintFieldDescendingFormOfEducationCommand(manager, false));
    }

    private void putServerCommands(CommandInvoker commandInvoker, ConnectionListener connectionListener) {
        commandInvoker.addServerCommand("exit", new ExitCommand(connectionListener, this));
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }
}
