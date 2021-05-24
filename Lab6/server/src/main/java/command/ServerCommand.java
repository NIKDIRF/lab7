package command;

import exception.CommandExecutionException;
import studygroup.User;

public interface ServerCommand {
    void execute() throws CommandExecutionException;
}
