package command;

import commands.AbstractCommand;
import commands.Command;
import response.Creator;
import studygroup.StudyGroup;
import studygroup.User;

import java.util.List;

public class HistoryCommand extends AbstractCommand implements Command {
    private final Creator creator;
    private final CommandHistory commandHistory;

    public HistoryCommand(CommandHistory commandHistory, Creator creator, boolean req) {
        super(req);
        this.commandHistory = commandHistory;
        this.creator = creator;
    }

    public void execute(User user) {
        List<String> history = commandHistory.getHistory();
        for (int i = history.size() - 1; i >= 0; i--) {
            creator.addToMsg(history.get(i));
        }
    }

    public void execute(StudyGroup studyGroup) {
        throw new UnsupportedOperationException();
    }
}
