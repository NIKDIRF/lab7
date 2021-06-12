package client;

import commands.*;
import connection.ConnectionManager;
import connection.ConnectionManagerImpl;
import exception.*;
import io.ConsoleIO;
import io.StudygroupParser;
import io.Reader;
import io.UserIO;
import locale.ClientLocale;
import request.RequestCreator;
import request.RequestSender;
import request.RequestSenderImpl;
import response.ResponseReader;
import response.ResponseReaderImpl;
import studygroup.Request;
import studygroup.RequestType;
import studygroup.Response;
import studygroup.User;

import java.io.EOFException;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Locale;

public class Application {
    private final String address;
    private final int port;
    private final UserIO userIO;
    private final CommandInvoker commandInvoker;
    private final ConnectionManager connectionManager;
    private final RequestCreator requestCreator;
    private final RequestSender requestSender;
    private final ResponseReader reader;
    private final Reader studyGroupReader;
    private boolean isRunning = true;
    private final AuthModule authModule;


    public Application(String address, int port) {
        Locale.setDefault(new Locale("ru", "RU"));
        this.address = address;
        this.port = port;
        userIO = new ConsoleIO();
        connectionManager = new ConnectionManagerImpl();
        requestCreator = new RequestCreator();
        requestSender = new RequestSenderImpl();
        reader = new ResponseReaderImpl();
        authModule = new AuthModule(userIO, connectionManager, requestSender, reader);
        commandInvoker = new CommandInvoker(authModule);
        studyGroupReader = new StudygroupParser(userIO);
        setCommands(commandInvoker, authModule);
    }

    public void setIsRunning(boolean b) {
        isRunning = b;
    }

    public void start() {
        try {
            connectionManager.openConnection(address, port);
        } catch (IOException e) {
            System.err.println("server is unavailable");
            return;
        }
        while(isRunning) {
            userIO.printUserPrompt();
            String userString = "";
            try {
                userString = userIO.readLine();
                commandInvoker.execute(userString, null);
            } catch (CommandExecutionException executionException) {
                userIO.printErrorMessage(ClientLocale.getString("exception.command_exec_error") + ": " + executionException.getMessage());
            } catch (IOException ioe) {
                userIO.printErrorMessage(ioe.getMessage());
            } catch (CommandNotFoundException ise) {
                try {
                    Response response = communicateWithServer(userString, studyGroupReader);
                    userIO.printLine(response.getMessage());
                } catch (EOFException eofe) {
                    userIO.printErrorMessage(ClientLocale.getString("exception.too_many_bytes"));
                } catch (IOException | ClassNotFoundException ioe) {
                    userIO.printErrorMessage("server is unavailable, reconnecting");
                    try {
                        connectionManager.openConnection(address, port);
                        userIO.printErrorMessage("successfully reconnected");
                    } catch (IOException e) {
                        userIO.printErrorMessage("failure, try again later");
                    }
                } catch (StudyGroupReadException | StudyGroupBuildException | IllegalStateException e) {
                    userIO.printErrorMessage(e.getMessage());

                }
            }
        }
    }

    public Response communicateWithServer(String userString, Reader studyGroupReader) throws IOException, ClassNotFoundException,
            StudyGroupReadException, StudyGroupBuildException {
        SocketChannel socketChannel = connectionManager.getConnection();
        Request request = requestCreator.createStudyGroupRequest(userString, authModule.getUser());
        requestSender.sendRequest(socketChannel, request);
        Response response = reader.getResponse(socketChannel);

        if (!response.isSuccess())
            throw new IllegalStateException(response.getMessage());


        if (response.isStudyGroupRequired()) {
            request.setStudyGroup(studyGroupReader.read());
        }

        request.setType(RequestType.COMMAND_REQUEST);
        requestSender.sendRequest(socketChannel, request);
        response = reader.getResponse(socketChannel);

        if (!response.isSuccess())
            throw new IllegalStateException(response.getMessage());

        return response;
    }

    private void setCommands(CommandInvoker commandInvoker, AuthModule authModule) {
        commandInvoker.addCommand("exit", new ExitCommand(this));
        commandInvoker.addCommand("execute_script", new ExecuteScriptCommand(this, commandInvoker, userIO));
        commandInvoker.addCommand("client_help", new ClientHelpCommand(userIO));
        commandInvoker.addCommand("login", new LoginCommand(authModule));
        commandInvoker.addCommand("register", new RegisterCommand(authModule));
    }
}
