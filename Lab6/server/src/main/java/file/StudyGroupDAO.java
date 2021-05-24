package file;

import studygroup.StudyGroup;
import java.util.Collection;

public interface StudyGroupDAO {
    Collection<StudyGroup> getCollection();
    StudyGroup getStudyGroup(int id);
    void insertGroup(StudyGroup studyGroup);
    boolean updateGroup(StudyGroup studyGroup);
    boolean deleteGroup(StudyGroup group);
    boolean deleteGroups();
    boolean deleteGroupByID(int id);
}
