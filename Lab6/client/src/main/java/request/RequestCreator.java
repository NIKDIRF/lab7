package request;

import studygroup.Request;
import studygroup.RequestType;
import studygroup.StudyGroup;
import studygroup.User;

import java.util.Locale;

public class RequestCreator {
    public Request createStudyGroupRequest(String userString, User user) {
        return new Request(RequestType.STUDYGROUP_REQUEST, userString, null, user, Locale.getDefault());
    }

    public Request createCommandRequest(String userString, StudyGroup studyGroup, User user) {
        return new Request(RequestType.COMMAND_REQUEST, userString, studyGroup, user, Locale.getDefault());
    }
}
