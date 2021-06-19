package request;

import command.CommandInvoker;
import exception.AuthException;
import exception.CommandExecutionException;
import exception.CommandNotFoundException;
import file.UserAuthModule;
import locale.ServerBundle;
import log.Log;
import response.Creator;
import studygroup.Request;
import studygroup.RequestType;
import studygroup.Response;

import java.util.Locale;

public class RequestHandlerImpl implements RequestHandler{

    private final Creator responseCreator;

    private final CommandInvoker commandInvoker;

    private final UserAuthModule userAuthModule;

    public RequestHandlerImpl(CommandInvoker commandInvoker, Creator responseCreator, UserAuthModule userAuthModule) {
        this.commandInvoker = commandInvoker;
        this.responseCreator = responseCreator;
        this.userAuthModule = userAuthModule;
    }

    public Response handleRequest(Request request) throws CommandNotFoundException, CommandExecutionException, AuthException {
        Locale.setDefault(request.getLocale());
        Response response;
        if (request.getType().equals(RequestType.STUDYGROUP_REQUEST)) {
            response = handleStudyGroupRequest(request);
        } else if (request.getType().equals(RequestType.COMMAND_REQUEST)){
            response = handleCommandRequest(request);
        } else if (request.getType().equals(RequestType.LOGIN)){
            response = handleAuthRequest(request);
        } else {
            response = handleRegisterRequest(request);
        }
        return response;
    }

    private Response handleStudyGroupRequest(Request request) throws CommandNotFoundException, AuthException {
        if (request.getUser() == null || !userAuthModule.authUser(request.getUser())) {
            throw new AuthException(ServerBundle.getString("exception.no_auth"));
        }
        Log.getLogger().info(ServerBundle.getString("server.ask_studyGroup_requirement"));
        if (commandInvoker.checkStudyGroupRequirement(request.getUserString())) {
            Log.getLogger().info(ServerBundle.getString("server.ask_studyGroup_positive"));
            return responseCreator.createResponse("", true, true);
        } else {
            Log.getLogger().info(ServerBundle.getString("server.ask_studyGroup_negative"));
            return responseCreator.createResponse("", true, false);
        }
    }

    private Response handleCommandRequest(Request request) throws CommandExecutionException, CommandNotFoundException, AuthException {
        if (request.getUser() == null || !userAuthModule.authUser(request.getUser())) {
            throw new AuthException(ServerBundle.getString("exception.no_auth"));
        }
        Log.getLogger().info(ServerBundle.getFormattedString("server.execute_attempt", request.getUserString()));
        commandInvoker.execute(request.getUserString(), request.getStudyGroup(), request.getUser());
        return responseCreator.createResponse();
    }

    private Response handleAuthRequest(Request request) {
        boolean result = userAuthModule.authUser(request.getUser());
        return responseCreator.createResponse(userAuthModule.getReason(), result, result);
    }

    private Response handleRegisterRequest(Request request) {
        boolean result = userAuthModule.registerUser(request.getUser());
        return responseCreator.createResponse(userAuthModule.getReason(), result, result);
    }
}
