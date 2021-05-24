package io;

import exception.StudyGroupBuildException;
import exception.StudyGroupReadException;
import studygroup.StudyGroup;

public interface Reader {
    StudyGroup read() throws StudyGroupBuildException, StudyGroupReadException;
}
