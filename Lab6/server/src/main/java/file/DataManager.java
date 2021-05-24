package file;

import java.sql.Connection;

public interface DataManager {
    void start(String url, String user, String password);
    Connection createConnection();
}
