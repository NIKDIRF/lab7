package io;

import exception.StudyGroupBuildException;
import exception.StudyGroupReadException;
import locale.ClientLocale;
import studygroup.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class StudygroupParser implements Reader {
    private final UserIO userIO;

    public StudygroupParser(UserIO userIO) {
        this.userIO = userIO;
    }

    public StudyGroup read() throws StudyGroupBuildException {
        StudygroupBuilder studygroupBuilder = new StudygroupBuilder();
        return studygroupBuilder.setName(readName())
                .setCoordinates(readCoordinates())
                .setStudentsCount(readStudentsCount())
                .setExpelledStudents(readExpelledStudents())
                .setTransferredStudents(readTransferredStudents())
                .setFormOfEducation(readFormOfEducation())
                .setGroupAdmin(readAdminGroup())
                .build();
    }

    /**
     * Метод, который считывает поля объекта Coordinates и возвращает его
     * @return - объект Coordinates
     */
    private Coordinates readCoordinates() {
        while (true) {
            try {
                userIO.printLine("Введите координату по X:");
                double x = Double.parseDouble(userIO.readLine());

                userIO.printLine("Введите координату по Y:");
                double y = Double.parseDouble(userIO.readLine());

                return new Coordinates(x, y);
            } catch (IOException ioe) {
                userIO.printErrorMessage(ClientLocale.getString("exception.general"));
            } catch (NumberFormatException nfe) {
                userIO.printErrorMessage("Неверный формат");
            }
        }
    }



    private String readName() {
        while (true) {
            try {
                userIO.printLine("Введите имя группы:\n*Значениие не может быть пустым*");
                String str = userIO.readLine();
                return str.replaceAll("[^\\w\\s]", "");
            } catch (IOException ioe) {
                userIO.printErrorMessage(ClientLocale.getString("exception.general"));
            } catch (NumberFormatException e) {
                userIO.printErrorMessage("Неверная формат");
            }
        }
    }

    private long readStudentsCount() {
        while(true) {
            try {
                userIO.printLine("Введите колличество студентов:\n*Значение должно быть больше 0*");
                return Long.parseLong(userIO.readLine());
            } catch (IOException ioe) {
                userIO.printErrorMessage("Неправильный формат");
            } catch (NumberFormatException e) {
                userIO.printErrorMessage("Неверная формат");
            }
        }
    }

    private long readExpelledStudents() {
        while(true) {
            try {
                userIO.printLine("Введите колличество исключённых студентов:\n*Значение должно быть больше 0*");
                return Long.parseLong(userIO.readLine());
            } catch (IOException ioe) {
                userIO.printErrorMessage("Неправильный формат");
            } catch (NumberFormatException e) {
                userIO.printErrorMessage("Неверная формат");
            }
        }
    }

    private Integer readTransferredStudents() {
        while(true) {
            try {
                userIO.printLine("Введите колличество переведённых студентов:\n*Значение должно быть больше 0 и не может быть пустым*");
                return Integer.parseInt(userIO.readLine());
            } catch (IOException ioe) {
                userIO.printErrorMessage("Неправильный формат");
            } catch (NumberFormatException e) {
                userIO.printErrorMessage("Неверная формат");
            }
        }
    }

    private FormOfEducation readFormOfEducation() {
        while(true) {
            try {
                userIO.printLine("Выбор формата обучения:\nНажмите 1 для выбора дистанционного формата...\nНажмите 2 для выбора очного формата...\nНажмите 3 для выбора вечернего формата...");
                String form = userIO.readLine();
                switch (form) {
                    case "1" :
                        return FormOfEducation.DISTANCE_EDUCATION;
                    case "2" :
                        return FormOfEducation.FULL_TIME_EDUCATION;
                    case "3" :
                        return FormOfEducation.EVENING_CLASSES;
                    default :
                        if (form.length() == 0) {
                            return null;
                        }
                        userIO.printErrorMessage("Неправильный формат");
                }
            } catch (IOException e) {
                userIO.printErrorMessage("Неправильный формат");
            } catch (NumberFormatException e) {
                userIO.printErrorMessage("Неверная формат");
            }
        }
    }

    private Person readAdminGroup() {
        while (true) {
            try {
                userIO.printLine("Введите данные старосты группы...\nимя старосты:\n*Старосте нельзя остаться без имени*");
                String name = userIO.readLine();

                userIO.printLine("Введите дату рождения старосты...");
                String date = userIO.readLine();
                LocalDateTime dateTime = null;
                if (date.length() != 0) {
                    dateTime = LocalDateTime.parse(date);
                }


                userIO.printLine("Введите рост старосты:");
                Double height = null;
                String heightStr = userIO.readLine();
                if (heightStr.length() != 0) {
                    height = Double.parseDouble(heightStr);
                }

                userIO.printLine("Нажмите 1 если староста родом из России...\n" +
                        "Нажмите 2 если староста родом из Ватикана...\n" +
                        "Нажмите 3 если староста родом из италии...\n" +
                        "Нажмите 4 если староста родом из Японии...");
                Country country = null;
                String form = userIO.readLine();
                switch (form) {
                    case "1" :
                        country = Country.RUSSIA;
                        break;
                    case "2" :
                        country = Country.VATICAN;
                        break;
                    case "3" :
                        country = Country.ITALY;
                        break;
                    case "4" :
                        country = Country.JAPAN;
                        break;
                    default :
                        if (form.length() != 0) {
                            throw new StudyGroupReadException();
                        }
                }

                userIO.printLine("Введите координату локации по X:");
                int x = Integer.parseInt(userIO.readLine());

                userIO.printLine("Введите координату локации по Y:");
                long y = Long.parseLong(userIO.readLine());

                userIO.printLine("Введите координату локации по Z:");
                long z = Long.parseLong(userIO.readLine());

                userIO.printLine("Введите название локации:");
                String locationName = userIO.readLine();

                return new Person(name, dateTime, height, country, new Location(x, y, z, locationName));

            } catch (DateTimeParseException e) {
                userIO.printErrorMessage("Неверная дата");
            } catch (StudyGroupReadException e) {
                userIO.printErrorMessage("Неверный формат");
            } catch (IOException | NumberFormatException e) {
                userIO.printErrorMessage("Неверная формат");
            }

        }
    }

}
