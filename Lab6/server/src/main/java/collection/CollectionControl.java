package collection;

import studygroup.StudyGroup;
import studygroup.User;

import java.util.Collection;
import java.util.HashSet;

public interface CollectionControl {
    HashSet<StudyGroup> getStudyGroupHashSet();
    void addStudyGroup(StudyGroup studyGroup);
    void addStudyGroupByIdIfMax(StudyGroup studyGroup, User user);
    void addStudyGroupById(StudyGroup studyGroup, User user);
    void updateId(int id, StudyGroup studyGroup, User user) throws NumberFormatException;
    void info();
    void clear(User user);
    void show();
    void printAscending();
    void printUniqueExpelledStudents();
    void printFieldDescendingFormOfEducation();
    void removeById(int id, User user);
    void removeGreater(StudyGroup studyGroup, User user);
    void removeLower(StudyGroup studyGroup, User user);
}
