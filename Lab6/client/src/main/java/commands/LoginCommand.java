package commands;

import client.AuthModule;
import exception.CommandExecutionException;
import locale.ClientLocale;
import studygroup.User;

import java.io.IOException;

public class LoginCommand extends AbstractCommand{
    private final AuthModule authModule;

    public LoginCommand(AuthModule authModule) {
        this.authModule = authModule;
    }

    @Override
    public void execute(User user) throws CommandExecutionException {
        try {
            authModule.authorize();
        } catch (IOException e) {
            throw new CommandExecutionException(ClientLocale.getString("exception.general"));
        }
    }
}
