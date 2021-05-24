package studygroup;

import exception.StudyGroupBuildException;

import java.time.LocalDateTime;

public class StudygroupBuilder {
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private long studentsCount; //Значение поля должно быть больше 0
    private long expelledStudents; //Значение поля должно быть больше 0
    private Integer transferredStudents; //Значение поля должно быть больше 0, Поле не может быть null
    private FormOfEducation formOfEducation; //Поле может быть null
    private Person groupAdmin; //Поле может быть null
    private String username;

    public StudygroupBuilder setId(int id) throws StudyGroupBuildException {
        if (id <= 0) {
            throw new StudyGroupBuildException("id <= 0");
        }
        this.id = id;
        return this;
    }

    public StudygroupBuilder setName(String name) throws StudyGroupBuildException {
        if (name.length() == 0) {
            throw new StudyGroupBuildException("name is empty");
        }
        this.name = name;
        return this;
    }

    public StudygroupBuilder setCoordinates(Coordinates coordinates) throws StudyGroupBuildException {
        if (coordinates == null) {
            throw new StudyGroupBuildException("coordinates is null");
        }
        this.coordinates = coordinates;
        return this;
    }

    public StudygroupBuilder setStudentsCount(long count) throws StudyGroupBuildException {
        if (count <= 0) {
            throw new StudyGroupBuildException("count <= 0");
        }
        this.studentsCount = count;
        return this;
    }

    public StudygroupBuilder setExpelledStudents(long studentsCount) throws StudyGroupBuildException {
        if (studentsCount <= 0) {
            throw new StudyGroupBuildException("studentsCount <= 0");
        }
        this.expelledStudents = studentsCount;
        return this;
    }

    public StudygroupBuilder setTransferredStudents(Integer studentsCount) throws StudyGroupBuildException {
        if (studentsCount <= 0) {
            throw new StudyGroupBuildException("transferred students <= 0");
        }
        this.transferredStudents = studentsCount;
        return this;
    }

    public StudygroupBuilder setFormOfEducation(FormOfEducation formOfEducation) throws StudyGroupBuildException {
        this.formOfEducation = formOfEducation;
        return this;
    }

    public StudygroupBuilder setGroupAdmin(Person person) throws StudyGroupBuildException {
        this.groupAdmin = person;
        return this;
    }

    public StudyGroup build() {
        return new StudyGroup(1, name, coordinates, studentsCount, expelledStudents,
                transferredStudents, formOfEducation, groupAdmin, username);
    }

    public StudyGroup buildId() {
        return new StudyGroup(id, name, coordinates, creationDate, studentsCount, expelledStudents,
                transferredStudents, formOfEducation, groupAdmin, username);
    }

    public StudygroupBuilder setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setUsername(String string) {
        this.username = string;
    }
}
