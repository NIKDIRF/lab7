package file;

import exception.DataBaseException;
import studygroup.User;

import java.sql.*;

public class PSQLUsersDAO implements UsersDAO{

    private final Connection connection;

    public PSQLUsersDAO(Connection connection) throws SQLException {
        this.connection = connection;
        create();
    }

    private void create() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS  users " +
                "(username TEXT primary key, " +
                " password TEXT not null)");
    }

    @Override
    public User getUser(String username) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            if (preparedStatement.execute()) {
                ResultSet resultSet = preparedStatement.getResultSet();
                if (resultSet.next()) {
                    return new User(resultSet.getString(1), resultSet.getString(2));
                }
            }
            return null;
        } catch (SQLException e) {
            throw new DataBaseException(e.getErrorCode(), e.getMessage());
        }
    }

    @Override
    public void insertUser(User user) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users VALUES (?,?)");
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DataBaseException(e.getErrorCode(), e.getMessage());
        }
    }

    @Override
    public boolean deleteUser(User user) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE username = ?");
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            throw new DataBaseException(e.getErrorCode(), e.getMessage());
        }
    }
}
