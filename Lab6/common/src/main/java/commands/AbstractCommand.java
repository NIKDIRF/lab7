package commands;

import exception.CommandExecutionException;
import studygroup.User;

abstract public class AbstractCommand implements Command {
    private final boolean studyGroupRequired;

    public AbstractCommand (boolean b) {
        studyGroupRequired = b;
    }

    public AbstractCommand () {
        studyGroupRequired = false;}

    public boolean isStudyGroupRequired() {
        return studyGroupRequired;
    }

    public abstract void execute(User user) throws CommandExecutionException;
}
