package command;

import commands.AbstractCommand;
import commands.Command;
import response.Creator;
import studygroup.StudyGroup;
import studygroup.User;

import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * Класс-команда, реализующая вывод всех команд, доступных для выполнения
 */
public class HelpCommand extends AbstractCommand {
    private final Creator creator;

    public HelpCommand(boolean req, Creator creator) {
        super(req);
        this.creator = creator;
    }

    public void execute(User user) {
        creator.addToMsg("help : вывести справку по доступным командам\n" +
                "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                "add {element} : добавить новый элемент в коллекцию\n" +
                "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                "remove_by_id id : удалить элемент из коллекции по его id\n" +
                "clear : очистить коллекцию\n" +
                "save : сохранить коллекцию в файл\n" +
                "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                "exit : завершить программу (без сохранения в файл)\n" +
                "add_if_max {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции\n" +
                "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный\n" +
                "remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный\n" +
                "print_ascending : вывести элементы коллекции в порядке возрастания\n" +
                "print_unique_expelled_students : вывести уникальные значения поля expelledStudents всех элементов в коллекции\n" +
                "print_field_descending_form_of_education : вывести значения поля formOfEducation всех элементов в порядке убывания");
    }

    public void execute(StudyGroup studyGroup) {
        throw new UnsupportedOperationException();
    }
}
