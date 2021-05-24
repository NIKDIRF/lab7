package studygroup;

import java.io.Serializable;

public class User implements Serializable {
    private String password;
    private String login;

    public User(String login, String password) {
        setLogin(login);
        setPassword(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
