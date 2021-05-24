package command;

import collection.CollectionControl;
import commands.AbstractCommand;
import commands.Command;
import studygroup.StudyGroup;
import studygroup.User;

/**
 * Класс-команда, реализующая вывод информации о коллекции
 */
public class InfoCommand extends AbstractCommand implements Command {
    private final CollectionControl StudyGroupManager;

    public InfoCommand(CollectionControl StudyGroupManager, boolean req) {
        super(req);
        this.StudyGroupManager = StudyGroupManager;
    }

    public void execute(User user) {
        StudyGroupManager.info();
    }

    public void execute(StudyGroup studyGroup) {
        throw new UnsupportedOperationException();
    }
}
