package studygroup;

public class StudygroupFormatter {
    public String formatStudyGroup(StudyGroup studyGroup) {
        String formOfEducation, groupAdmin;
        Person person = studyGroup.getGroupAdmin();

        if (studyGroup.getFormOfEducation() == null ) {
            formOfEducation = "null";
        } else {
            formOfEducation = studyGroup.getFormOfEducation().toString();
        }

        if (studyGroup.getGroupAdmin() == null) {
            groupAdmin = "null";
        } else {
            groupAdmin = String.format("name:%s, birthday:%s, height:%f, nationality:%s, location.x:%d, location.y:%d," +
                    " location.z:%d, location.name:%s", person.getName(), person.getBirthday(), person.getHeight(),
                    person.getNationality(), person.getLocation().getX(), person.getLocation().getY(),
                    person.getLocation().getZ(), person.getLocation().getName());
        }

        return String.format("StudyGroup: id:%d, name:%s, coordinates.x:%f, coordinates.y:%f," +
                        "creation date:%s, studentsCount:%d, expelledStundets:%d, transferredStudents:%d," +
                        "formOfEducation:%s, formOfEducation:%s, groupAdmin:%s, userName:%s", studyGroup.getId(),
                studyGroup.getName(), studyGroup.getCoordinates().getX(), studyGroup.getCoordinates().getY(),
                studyGroup.getCreationDate(), studyGroup.getStudentsCount(), studyGroup.getExpelledStudents(),
                studyGroup.getTransferredStudents(), studyGroup.getFormOfEducation(), formOfEducation, groupAdmin, studyGroup.getUsername());
    }
}
