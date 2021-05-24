package io;

import exception.StudyGroupBuildException;
import exception.StudyGroupReadException;
import locale.ClientLocale;
import studygroup.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class StudygroupScriptParser implements Reader {
    private final UserIO userIO;
    private BufferedReader input;

    public StudygroupScriptParser(BufferedReader input, UserIO userIO) {
        this.input = input;
        this.userIO = userIO;
    }

    public StudyGroup read() throws StudyGroupBuildException, StudyGroupReadException {
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
     *
     * @return - объект Coordinates
     */
    private Coordinates readCoordinates() throws StudyGroupReadException {
        try {
            double x, y;
            if (input.ready()) {
                x = Double.parseDouble(input.readLine());
            } else {
                throw new StudyGroupReadException();
            }

            if (input.ready()) {
                y = Double.parseDouble(input.readLine());
            } else {
                throw new StudyGroupReadException();
            }

            return new Coordinates(x, y);
        } catch (IOException ioe) {
            userIO.printErrorMessage(ClientLocale.getString("exception.general"));
        } catch (NumberFormatException nfe) {
            userIO.printErrorMessage("Неверный формат");
        }
        return null;
    }


    private String readName() throws StudyGroupReadException {
        try {
            String str;
            if (input.ready()) {
                str = input.readLine();
            } else {
                throw new StudyGroupReadException();
            }
            return str.replaceAll("[^\\w\\s]", "");
        } catch (IOException ioe) {
            userIO.printErrorMessage(ClientLocale.getString("exception.general"));
        } catch (NumberFormatException e) {
            userIO.printErrorMessage("Неверная формат");
        }
        return null;
    }

    private Long readStudentsCount() throws StudyGroupReadException {
        try {
            if (input.ready()) {
                return Long.parseLong(input.readLine());
            } else {
                throw new StudyGroupReadException();
            }
        } catch (IOException ioe) {
            userIO.printErrorMessage("Неправильный формат");
        } catch (NumberFormatException e) {
            userIO.printErrorMessage("Неверная формат");
        }
        return null;
    }

    private Long readExpelledStudents() throws StudyGroupReadException {
        try {
            if (input.ready()) {
                return Long.parseLong(input.readLine());
            } else {
                throw new StudyGroupReadException();
            }
        } catch (IOException ioe) {
            userIO.printErrorMessage("Неправильный формат");
        } catch (NumberFormatException e) {
            userIO.printErrorMessage("Неверная формат");
        }
        return null;
    }

    private Integer readTransferredStudents() throws StudyGroupReadException {
        try {
            if (input.ready()) {
                return Integer.parseInt(input.readLine());
            } else {
                throw new StudyGroupReadException();
            }
        } catch (IOException ioe) {
            userIO.printErrorMessage("Неправильный формат");
        } catch (NumberFormatException e) {
            userIO.printErrorMessage("Неверная формат");
        }
        return null;
    }

    private FormOfEducation readFormOfEducation() throws StudyGroupReadException {
        try {
            if (input.ready()) {
                String form = input.readLine();
                switch (form) {
                    case "1":
                        return FormOfEducation.DISTANCE_EDUCATION;
                    case "2":
                        return FormOfEducation.FULL_TIME_EDUCATION;
                    case "3":
                        return FormOfEducation.EVENING_CLASSES;
                    default:
                        if (form.length() == 0) {
                            return null;
                        }
                        throw new StudyGroupReadException();
                }
            } else {
                throw new StudyGroupReadException();
            }
        } catch (IOException e) {
            userIO.printErrorMessage("Неправильный формат");
        } catch (NumberFormatException e) {
            userIO.printErrorMessage("Неверная формат");
        }
        return null;
    }

    private Person readAdminGroup() {
        try {
            String name, date;
            if (input.ready()) {
                name = input.readLine();
            } else {
                throw new StudyGroupReadException();
            }

            if (input.ready()) {
                date = input.readLine();
            } else {
                throw new StudyGroupReadException();
            }

            LocalDateTime dateTime = null;
            if (input.ready()) {
                if (date.length() != 0) {
                    dateTime = LocalDateTime.parse(date);
                }
            }

            Double height;
            if (input.ready()) {
                height = null;
                String heightStr = input.readLine();
                if (heightStr.length() != 0) {
                    height = Double.parseDouble(heightStr);
                }
            } else {
                throw new StudyGroupReadException();
            }

            Country country;
            if (input.ready()) {
                country = null;
                String form = input.readLine();
                switch (form) {
                    case "1":
                        country = Country.RUSSIA;
                        break;
                    case "2":
                        country = Country.VATICAN;
                        break;
                    case "3":
                        country = Country.ITALY;
                        break;
                    case "4":
                        country = Country.JAPAN;
                        break;
                    default:
                        if (form.length() != 0) {
                            throw new StudyGroupReadException();
                        }
                }
            } else {
                throw new StudyGroupReadException();
            }

            int x;
            long y, z;
            String locationName;
            if (input.ready()) {
                x = Integer.parseInt(input.readLine());
            } else {
                throw new StudyGroupReadException();
            }

            if (input.ready()) {
                y = Long.parseLong(input.readLine());
            } else {
                throw new StudyGroupReadException();
            }

            if (input.ready()) {
                z = Long.parseLong(input.readLine());
            } else {
                throw new StudyGroupReadException();
            }

            if (input.ready()) {
                locationName = input.readLine();
            } else {
                throw new StudyGroupReadException();
            }

            return new Person(name, dateTime, height, country, new Location(x, y, z, locationName));

        } catch (DateTimeParseException e) {
            userIO.printErrorMessage("Неверная дата");
        } catch (StudyGroupReadException e) {
            userIO.printErrorMessage("Неверный формат");
        } catch (IOException | NumberFormatException e) {
            userIO.printErrorMessage("Неверная формат");
        }
        return null;
    }
}

