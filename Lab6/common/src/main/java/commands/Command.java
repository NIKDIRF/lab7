package commands;

import exception.CommandExecutionException;
import studygroup.User;

public interface Command {
    void execute(User user) throws CommandExecutionException;
    default void setArgs(String[] args) {

    }
    boolean isStudyGroupRequired();
}
