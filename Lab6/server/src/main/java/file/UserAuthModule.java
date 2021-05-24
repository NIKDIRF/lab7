package file;

import exception.DataBaseException;
import locale.ServerBundle;
import studygroup.User;
import util.CryptoModule;

public class UserAuthModule {
    private final UsersDAO userDAO;
    private final CryptoModule cryptoModule;
    private User user;
    private String reason;

    public UserAuthModule(UsersDAO userDAO, CryptoModule cryptoModule) {
        this.userDAO = userDAO;
        this.cryptoModule = cryptoModule;
    }

    public boolean authUser(User user) {
        User authUser = userDAO.getUser(user.getLogin());
        if (authUser == null) {
            reason = ServerBundle.getString("auth.incorrect_username_or_password");
            this.user = null;
            return false;
        } else if(cryptoModule.hash(authUser.getPassword()).equals(cryptoModule.hash(user.getPassword()))) {
            this.user = user;
            reason = "";
        }
        return true;
    }

    public boolean registerUser(User user) {
        try {
            user.setPassword(cryptoModule.hash(user.getPassword()));
            userDAO.insertUser(user);
            reason = "";
            return true;
        } catch (DataBaseException e) {
            reason = ServerBundle.getString("auth.user_exists");
            return false;
        }
    }

    public User getUser() {
        System.out.println(user);
        return user;
    }

    public String getReason() {
        return reason;
    }
}
