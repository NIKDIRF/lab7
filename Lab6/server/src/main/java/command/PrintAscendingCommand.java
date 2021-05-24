package command;

import collection.CollectionControl;
import commands.AbstractCommand;
import studygroup.StudyGroup;
import studygroup.User;

public class PrintAscendingCommand extends AbstractCommand {
    private final CollectionControl StudyGroupManager;

    public PrintAscendingCommand(CollectionControl StudyGroupManager, boolean req) {
        super(req);
        this.StudyGroupManager = StudyGroupManager;
    }

    public void execute(User user) {
        StudyGroupManager.printAscending();
    }

    public void execute(StudyGroup studyGroup) {
        throw new UnsupportedOperationException();
    }

}
