package studygroup;

import java.io.Serializable;
import java.nio.channels.SocketChannel;
import java.util.Locale;

public class Request implements Serializable {
    private static final long serialVersionUID = -4287447999382808577L;

    private RequestType type;
    private String userString;
    private StudyGroup studyGroup;
    private User user;
    private Locale locale;
    transient private SocketChannel channel;

    public Request(RequestType type, String userString, StudyGroup studyGroup, User user, Locale locale) {
        this.type = type;
        this.userString = userString;
        this.studyGroup = studyGroup;
        this.user = user;
        this.locale = locale;
    }

    public String getUserString() {
        return userString;
    }

    public StudyGroup getStudyGroup() {
        return studyGroup;
    }


    public RequestType getType() {
        return type;
    }

    public void setUserString(String userString) {
        this.userString = userString;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public void setStudyGroup(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
    }

    @Override
    public String toString() {
        return "Request{" +
                "type=" + type +
                ", userString='" + userString + '\'' +
                ", studyGroup=" + studyGroup +
                ", user=" + user +
                ", locale=" + locale +
                ", channel=" + channel +
                '}';
    }

    public User getUser() {
        return user;
    }

    public Locale getLocale() {
        return locale;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public void setChannel(SocketChannel channel) {
        this.channel = channel;
    }
}
