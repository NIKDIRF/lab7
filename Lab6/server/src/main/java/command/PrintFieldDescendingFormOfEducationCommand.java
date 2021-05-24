package command;

import collection.CollectionControl;
import commands.AbstractCommand;
import studygroup.StudyGroup;
import studygroup.User;

public class PrintFieldDescendingFormOfEducationCommand extends AbstractCommand {
    private final CollectionControl StudyGroupManager;

    public PrintFieldDescendingFormOfEducationCommand(CollectionControl StudyGroupManager, boolean req) {
        super(req);
        this.StudyGroupManager = StudyGroupManager;
    }

    public void execute(User user) {
        StudyGroupManager.printFieldDescendingFormOfEducation();
    }

    public void execute(StudyGroup studyGroup) {
        throw new UnsupportedOperationException();
    }
}
