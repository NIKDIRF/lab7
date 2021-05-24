package commands;

import exception.CommandExecutionException;
import studygroup.StudyGroup;
import studygroup.User;

public interface StudyGroupCommand extends Command {
    void execute(StudyGroup studyGroup, User user) throws CommandExecutionException;
}
