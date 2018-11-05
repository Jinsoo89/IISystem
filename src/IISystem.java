import java.sql.*;
import java.util.Properties;
import java.io.FileInputStream;

/*
 * Main model for IISystem,
 * connect the program to database in external server,
 * and contain functions that create queries to get
 * data from the database
 */
public class IISystem {
    private String JDBC_DRIVER;
    private String DB_URL;
    private String DB_USER;
    private String DB_PW;

    private String configFilename = "src/dbconn.properties";
    private Properties configProps = new Properties();
    private Connection conn;

    public IISystem() throws Exception {
        openConnection();
    }

    public void openConnection() {
        try {
            configProps.load(new FileInputStream(configFilename));

            JDBC_DRIVER = configProps.getProperty("iisystem.jdbc_driver");
            DB_URL = configProps.getProperty("iisystem.url");
            DB_USER = configProps.getProperty("iisystem.sqlazure_username");
            DB_PW = configProps.getProperty("iisystem.sqlazure_password");

            Class.forName(JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(DB_URL, // database
                    DB_USER, // user
                    DB_PW); // password

            conn.setAutoCommit(true);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    public void register(
            String f_name, String l_name, Date dob, String sex, int mal,
            int mea, int dpts, Date schedule) {
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO PATIENT (" +
                            "f_name, l_name, birth, sex, malaria, measles, DPT, schedule)" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

            statement.setString(1, f_name.toUpperCase());
            statement.setString(2, l_name.toUpperCase());
            statement.setDate(3, dob);
            statement.setString(4, sex.toUpperCase());
            statement.setInt(5, mal);
            statement.setInt(6, mea);
            statement.setInt(7, dpts);
            statement.setDate(8, schedule);

            statement.executeQuery();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

    }

    public void update(
            String f_name, String l_name, Date dob, String sex, int mal,
            int mea, int dpts, Date schedule
    ) {
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "UPDATE PATIENT" +
                            " SET malaria = ?, measles = ?, DPT = ?, schedule = ?" +
                            " WHERE f_name = ? AND l_name = ? AND birth = ?");

            statement.setInt(1, mal);
            statement.setInt(2, mea);
            statement.setInt(3, dpts);
            statement.setDate(4, schedule);
            statement.setString(5, f_name.toUpperCase());
            statement.setString(6, l_name.toUpperCase());
            statement.setDate(7, dob);

            statement.executeQuery();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public ResultSet patientInfo(String f_name, String l_name, Date dob) throws Exception {
        PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM PATIENT AS p WHERE p.f_name = ? AND p.l_name = ? AND p.birth = ?");
        statement.setString(1, f_name.toUpperCase());
        statement.setString(2, l_name.toUpperCase());
        statement.setDate(3, dob);

        ResultSet result = statement.executeQuery();

        return result;
    }

    public ResultSet searchOverdue() throws Exception {
        PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM PATIENT WHERE malaria = 0 OR measles = 0 OR DPT = 0 " +
                        "AND DATEDIFF(DAY, birth, GETDATE()) > 365");

        ResultSet result = statement.executeQuery();

        return result;
    }

    public ResultSet incompleteMalaria() throws Exception {
        PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM PATIENT AS p WHERE p.malaria = 0");

        ResultSet result = statement.executeQuery();

        return result;
    }

    public ResultSet incompleteMeasles() throws Exception {
        PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM PATIENT AS p WHERE p.measles = 0");

        ResultSet result = statement.executeQuery();

        return result;
    }

    public ResultSet incompleteDPT() throws Exception {
        PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM PATIENT AS p WHERE p.dpt = 0");

        ResultSet result = statement.executeQuery();

        return result;
    }
}
