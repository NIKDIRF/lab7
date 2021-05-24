package file;

import exception.DataBaseException;
import exception.StudyGroupBuildException;
import studygroup.*;
import util.DateLocalDateConverter;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class PSQLStudyGroupDAO implements StudyGroupDAO {

    private final Connection connection;

    public PSQLStudyGroupDAO(Connection connection) throws SQLException {
        this.connection = connection;
        create();
    }

    private void create() throws SQLException {
        String create = "CREATE TABLE IF NOT EXISTS  studyGroups (" +
                "id serial primary key," +
                "name varchar," +
                "coordinates_x double precision," +
                "coordinates_y double precision," +
                "creationdate timestamp," +
                "students_count bigint," +
                "expelled_students bigint," +
                "transferred_students integer," +
                "form_of_education varchar," +
                "admin_name varchar," +
                "admin_birthday timestamp," +
                "admin_height double precision," +
                "admin_nationality varchar," +
                "admin_location_x integer," +
                "admin_location_y bigint," +
                "admin_location_z bigint," +
                "admin_location_name varchar," +
                "username varchar)";
        Statement statement = connection.createStatement();
        statement.execute(create);
    }

    @Override
    public Collection<StudyGroup> getCollection() {
        try {
            Statement statement = connection.createStatement();
            Collection<StudyGroup> studyGroups = new ArrayList<>();
            ResultSet r = statement.executeQuery("SELECT * FROM studyGroups");
            while (r.next()) {
                StudygroupBuilder builder = new StudygroupBuilder();
                try {
                    buildGroup(r, builder);
                    studyGroups.add(builder.buildId());
                } catch (StudyGroupBuildException e) {
                    throw new DataBaseException("UNKNOWN", e.getMessage());
                }
            }
            return studyGroups;
        } catch (SQLException e) {
            throw new DataBaseException(e.getErrorCode(), e.getMessage());
        }
    }

    @Override
    public StudyGroup getStudyGroup(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM studyGroups WHERE id=?");
            StudyGroup studyGroup = null;
            statement.setInt(1, id);
            ResultSet r = statement.executeQuery();
            while (r.next()) {
                StudygroupBuilder builder = new StudygroupBuilder();
                try {
                    buildGroup(r, builder);
                    studyGroup = builder.buildId();
                } catch (StudyGroupBuildException e) {
                    throw new DataBaseException("UNKNOWN", e.getMessage());
                }
            }
            return studyGroup;
        } catch (SQLException e) {
            throw new DataBaseException(e.getErrorCode(), e.getMessage());
        }
    }


    private void buildGroup(ResultSet r, StudygroupBuilder studyGroupBuilder) throws SQLException, StudyGroupBuildException {
        studyGroupBuilder.setId(r.getInt(1));
        studyGroupBuilder.setName(r.getString(2));
        studyGroupBuilder.setCoordinates(new Coordinates(r.getDouble(3), r.getDouble(4)));
        studyGroupBuilder.setCreationDate(DateLocalDateConverter.convertDateToLocalDate(r.getTimestamp(5)));
        studyGroupBuilder.setStudentsCount(r.getLong(6));
        studyGroupBuilder.setExpelledStudents(r.getLong(7));
        studyGroupBuilder.setTransferredStudents(r.getInt(8));
        String form = r.getString(9);
        if (form == null)
            studyGroupBuilder.setFormOfEducation(null);
        else
            studyGroupBuilder.setFormOfEducation(FormOfEducation.valueOf(form));
        if (r.getString(10) == null && r.getDate(11) == null && r.getDouble(12) == 0 &&
                r.getString(13) == null && r.getInt(14) == 0 && r.getLong(15) == 0 &&
                r.getLong(16) == 0 && r.getString(17) == null)
            studyGroupBuilder.setGroupAdmin(null);
        else
            studyGroupBuilder.setGroupAdmin(new Person(r.getString(10),
                    r.getDate(11) == null ? null : DateLocalDateConverter.convertDateToLocalDate(r.getTimestamp(11)),
                    r.getDouble(12) == 0 ? null : r.getDouble(12),
                    r.getString(13) == null ? null : Country.valueOf(r.getString(13)),
                    new Location(r.getInt(14), r.getLong(15),
                            r.getLong(16), r.getString(17))));
        studyGroupBuilder.setUsername(r.getString(18));
    }

    @Override
    public void insertGroup(StudyGroup studyGroup) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO studyGroups VALUES (default, " +
                    "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, studyGroup.getName());
            statement.setDouble(2, studyGroup.getCoordinates().getX());
            statement.setDouble(3, studyGroup.getCoordinates().getY());
            statement.setTimestamp(4, DateLocalDateConverter.convertLocalDateToDate(studyGroup.getCreationDate()));
            statement.setLong(5, studyGroup.getStudentsCount());
            statement.setLong(6, studyGroup.getExpelledStudents());
            statement.setInt(7, studyGroup.getTransferredStudents());
            if (studyGroup.getFormOfEducation() != null)
                statement.setString(8, studyGroup.getFormOfEducation().toString());
            else
                statement.setNull(8, Types.VARCHAR);
            if (studyGroup.getGroupAdmin() != null) {
                statement.setString(9, studyGroup.getGroupAdmin().getName());
                statement.setTimestamp(10, DateLocalDateConverter.convertLocalDateToDate(studyGroup.getGroupAdmin().getBirthday()));
                statement.setDouble(11, studyGroup.getGroupAdmin().getHeight());
                if (studyGroup.getGroupAdmin().getNationality() != null)
                    statement.setString(12, studyGroup.getGroupAdmin().getNationality().toString());
                else
                    statement.setNull(12, Types.VARCHAR);
                statement.setInt(13, studyGroup.getGroupAdmin().getLocation().getX());
                statement.setLong(14, studyGroup.getGroupAdmin().getLocation().getY());
                statement.setLong(15, studyGroup.getGroupAdmin().getLocation().getZ());
                statement.setString(16, studyGroup.getGroupAdmin().getLocation().getName());
            } else {
                statement.setNull(9, Types.VARCHAR);
                statement.setNull(10, Types.DATE);
                statement.setNull(11, Types.DOUBLE);
                statement.setNull(12, Types.VARCHAR);
                statement.setNull(13, Types.INTEGER);
                statement.setNull(14, Types.BIGINT);
                statement.setNull(15, Types.BIGINT);
                statement.setNull(16, Types.VARCHAR);
            }
            statement.setString(17, studyGroup.getUsername());
            statement.execute();
            ResultSet set = statement.getGeneratedKeys();
            if (set.next()) {
                int id = set.getInt(set.findColumn("id"));
                studyGroup.setId(id);
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            throw new DataBaseException(e.getErrorCode(), e.getMessage());
        }
    }

    @Override
    public boolean updateGroup(StudyGroup studyGroup) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE studyGroups SET " +
                    "name = ?," +
                    "coordinates_x = ?," +
                    "coordinates_y = ?," +
                    "creationdate = ?," +
                    "students_count = ?," +
                    "expelled_students = ?," +
                    "transferred_students = ?," +
                    "form_of_education = ?," +
                    "admin_name = ?," +
                    "admin_birthday = ?," +
                    "admin_height = ?," +
                    "admin_nationality = ?," +
                    "admin_location_x = ?," +
                    "admin_location_y  = ?," +
                    "admin_location_z  = ?," +
                    "admin_location_name  = ?" +
                    "WHERE id = ?");
            statement.setString(1, studyGroup.getName());
            statement.setDouble(2, studyGroup.getCoordinates().getX());
            statement.setDouble(3, studyGroup.getCoordinates().getY());
            statement.setTimestamp(4, DateLocalDateConverter.convertLocalDateToDate(studyGroup.getCreationDate()));
            statement.setLong(5, studyGroup.getStudentsCount());
            statement.setLong(6, studyGroup.getExpelledStudents());
            statement.setInt(7, studyGroup.getTransferredStudents());
            if (studyGroup.getFormOfEducation() != null)
                statement.setString(8, studyGroup.getFormOfEducation().toString());
            else
                statement.setNull(8, Types.VARCHAR);
            if (studyGroup.getGroupAdmin() == null) {
                statement.setNull(9, Types.VARCHAR);
                statement.setNull(10, Types.DATE);
                statement.setNull(11, Types.DOUBLE);
                statement.setNull(12, Types.VARCHAR);
                statement.setNull(13, Types.INTEGER);
                statement.setNull(14, Types.BIGINT);
                statement.setNull(15, Types.BIGINT);
                statement.setNull(16, Types.VARCHAR);
                statement.setString(9, studyGroup.getGroupAdmin().getName());
            } else {
                statement.setTimestamp(10, DateLocalDateConverter.convertLocalDateToDate(studyGroup.getGroupAdmin().getBirthday()));
                statement.setDouble(11, studyGroup.getGroupAdmin().getHeight());
                if (studyGroup.getGroupAdmin().getNationality() != null)
                    statement.setString(12, studyGroup.getGroupAdmin().getNationality().toString());
                else
                    statement.setNull(12, Types.VARCHAR);
                statement.setInt(13, studyGroup.getGroupAdmin().getLocation().getX());
                statement.setLong(14, studyGroup.getGroupAdmin().getLocation().getY());
                statement.setLong(15, studyGroup.getGroupAdmin().getLocation().getZ());
                statement.setString(16, studyGroup.getGroupAdmin().getLocation().getName());
            }
            statement.setInt(17, studyGroup.getId());
            statement.execute();
            return true;
        } catch (SQLException e) {
            throw new DataBaseException(e.getErrorCode(), e.getMessage());
        }
    }

    @Override
    public boolean deleteGroup(StudyGroup group) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM studyGroups WHERE id=?");
            statement.setInt(1, group.getId());
            statement.execute();
            return true;
        } catch (SQLException e) {
            throw new DataBaseException(e.getErrorCode(), e.getMessage());
        }
    }

    @Override
    public boolean deleteGroupByID(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM studyGroups WHERE id=?");
            statement.setInt(1, id);
            statement.execute();
            return true;
        } catch (SQLException e) {
            throw new DataBaseException(e.getErrorCode(), e.getMessage());
        }
    }

    @Override
    public boolean deleteGroups() {
        try {
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM studyGroups");
            return true;
        } catch (SQLException e) {
            throw new DataBaseException(e.getErrorCode(), e.getMessage());
        }
    }
}
