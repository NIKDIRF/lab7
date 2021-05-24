package commands;

import client.AuthModule;
import exception.CommandExecutionException;
import exception.CommandNotFoundException;
import locale.ClientLocale;
import studygroup.StudyGroup;

import java.util.*;

/**
 * Класс, выбирающий и вызывающий команду для исполнения
 */
public class CommandInvoker {
    private final Map<String, Command> commands = new HashMap<>();
    private static final Set<String> scripts = new HashSet<>();
    private final AuthModule authModule;

    public CommandInvoker(AuthModule authModule) {
        this.authModule = authModule;
    }

    public Set<String> getScripts() {
        return scripts;
    }

    public void addCommand(String commandName, Command command) {
        commands.put(commandName, command);
    }

    /**
     * Метод, который добавляет название файла скрипта в множество
     * @param name - название файла
     */
    public void addScript(String name) {
        scripts.add(name);
    }

    /**
     * Метод, который убирает название файла скрипта из множества
     * @param name - название файла
     */
    public void removeScript(String name) {
        scripts.remove(name);
    }


    /**
     * Метод, выполняющий команду
     * @param inputString - название команды
     */
    public void execute(String inputString, StudyGroup studyGroup) throws CommandNotFoundException, CommandExecutionException {
        if (inputString == null) {
            return;
        }
        Command command;
        String[] split = inputString.split("\\s+");
        String[] args = Arrays.copyOfRange(split, 1, split.length);

        if(commands.containsKey(split[0].toLowerCase().trim())) {
            command = commands.get(split[0].toLowerCase().trim());
            command.setArgs(args);

            if(studyGroup == null) {
                command.execute(authModule.getUser());
            } else {
                StudyGroupCommand studyGroupCommand = (StudyGroupCommand) command;
                studyGroupCommand.execute(studyGroup, authModule.getUser());
            }
        } else {
            if (split[0].equals("")) {
                throw new CommandNotFoundException(ClientLocale.getString("exception.command_not_found"));
            } else {
                throw new CommandNotFoundException(ClientLocale.getString("exception.command_not_found") + ": " + split[0]);
            }
        }
    }
}
