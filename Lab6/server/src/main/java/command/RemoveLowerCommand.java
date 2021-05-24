package command;

import collection.CollectionControl;
import commands.AbstractCommand;
import commands.StudyGroupCommand;
import exception.CommandExecutionException;
import studygroup.StudyGroup;
import studygroup.User;

import java.util.NoSuchElementException;

public class RemoveLowerCommand extends AbstractCommand implements StudyGroupCommand {
    private final CollectionControl StudyGroupManager;

    public RemoveLowerCommand(CollectionControl StudyGroupManager, boolean req) {
        super(req);
        this.StudyGroupManager = StudyGroupManager;
    }

    public void execute(User user) {
        throw new UnsupportedOperationException();
    }

    public void execute(StudyGroup studyGroup, User user) throws CommandExecutionException {
        StudyGroupManager.removeLower(studyGroup, user);
    }
}
