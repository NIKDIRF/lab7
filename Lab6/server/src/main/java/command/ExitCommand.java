package command;

import connection.ConnectionListener;
import server.Application;
import studygroup.StudyGroup;

import java.io.IOException;

/**
 * Класс-команда, реализующая выход из JVM
 */
public class ExitCommand implements ServerCommand {
    private final ConnectionListener connectionListener;
    private final Application application;

    public ExitCommand(ConnectionListener connectionListener, Application application) {
        this.connectionListener = connectionListener;
        this.application = application;
    }

    public void execute() {
        try {
            application.setIsRunning(false);
            connectionListener.stop();
            System.exit(0);
        } catch (IOException ignored) {

        }
    }

    public void execute(StudyGroup studyGroup) {
        throw new UnsupportedOperationException();
    }
}
