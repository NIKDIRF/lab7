package file;

import log.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.LogManager;

public class PSQLDataManager implements DataManager{

    private Connection connection;

    public void start(String url, String user, String password) {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            Log.getLogger().error("Error. Can't connect to data base: exception {}", e.getClass());
            System.exit(1);
        }
        if (connection != null)
            Log.getLogger().info("Successfully connected to the data base.");
        else
            Log.getLogger().error("Error. Can't connect to data base.");
    }


    public Connection createConnection() {
        return connection;
    }
}
