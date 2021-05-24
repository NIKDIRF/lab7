package command;

import commands.AbstractCommand;
import commands.StudyGroupCommand;
import collection.CollectionControl;
import studygroup.StudyGroup;
import studygroup.User;


/**
 * Класс-команда, реализующая добавление элемента в коллекцию
 */
public class AddCommand extends AbstractCommand implements StudyGroupCommand {
    private final CollectionControl StudyGroupManager;

    public AddCommand(CollectionControl StudyGroupManager, boolean req) {
        super(req);
        this.StudyGroupManager = StudyGroupManager;
    }

    public void execute(User user) {
        throw new UnsupportedOperationException();
    }

    public void execute(StudyGroup studyGroup, User user) {
        StudyGroupManager.addStudyGroupById(studyGroup, user);
    }
}
