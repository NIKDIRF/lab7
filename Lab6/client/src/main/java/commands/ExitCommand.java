package commands;

import client.Application;
import studygroup.User;

public class ExitCommand extends AbstractCommand{
    Application application;


    public ExitCommand(Application application) {
        this.application = application;
    }

    @Override
    public void execute(User user) {
        application.setIsRunning(false);
    }
}
