package file;

import studygroup.User;

public interface UsersDAO {
    User getUser(String username);
    void insertUser(User user);
    boolean deleteUser(User user);
}
