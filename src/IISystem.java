import java.sql.*;
import java.util.Properties;
import java.io.FileInputStream;



public class IISystem {
    static  String JDBC_DRIVER;
    static  String DB_URL;
    static  String DB_USER;
    static  String DB_PW;

    private String configFilename = "src/dbconn.properties";
    private Properties configProps = new Properties();

    static Connection conn;
    static PreparedStatement stmt;

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

    public static void register(
            String f_name, String l_name, String sex, Date dob, int mal,
            int mea, int dpts) {
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO PATIENT (" +
                            "f_name, l_name, sex, birth, malaria, measles, DPT)" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)");

            statement.setString(1, f_name.toUpperCase());
            statement.setString(2, l_name.toUpperCase());
            statement.setString(3, sex.toUpperCase());
            statement.setDate(4, dob);
            statement.setInt(5, mal);
            statement.setInt(6, mea);
            statement.setInt(7, dpts);

            statement.executeQuery();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage() );
        }

    }

    public static ResultSet patientInfo(String f_name, String l_name, Date dob) throws Exception {
        PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM PATIENT AS p WHERE p.f_name = ? AND p.l_name = ? AND p.birth = ?");
        statement.setString(1, f_name.toUpperCase());
        statement.setString(2, l_name.toUpperCase());
        statement.setDate(3, dob);

        ResultSet result = statement.executeQuery();

        return result;
    }

    public static ResultSet searchByAges() throws Exception {
        PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM PATIENT AS p WHERE p.malaria = 0");

        ResultSet result = statement.executeQuery();

        return result;
    }

    public static ResultSet searchOverdue() throws Exception {
        PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM PATIENT WHERE malaria = 0 OR measles = 0 OR DPT = 0 " +
                        "AND DATEDIFF(DAY, birth, GETDATE()) > 365");

        ResultSet result = statement.executeQuery();

        return result;
    }

    public static ResultSet incompleteMalaria() throws Exception {
        PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM PATIENT AS p WHERE p.malaria = 0");

        ResultSet result = statement.executeQuery();

        return result;
    }

    public static ResultSet incompleteMeasles() throws Exception {
        PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM PATIENT AS p WHERE p.measles = 0");

        ResultSet result = statement.executeQuery();

        return result;
    }

    public static ResultSet incompleteDPT() throws Exception {
        PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM PATIENT AS p WHERE p.dpt = 0");

        ResultSet result = statement.executeQuery();

        return result;
    }
//
//
//
//    public static void main(String[] args) {
//        try{
//            Class.forName(JDBC_DRIVER).newInstance();
//
//            /* open connections to the flights database */
//            conn = DriverManager.getConnection(DB_URL, // database
//                    DB_USER, // user
//                    DB_PW); // password
//
//            conn.setAutoCommit(true);
//
////            stmt = conn.prepareStatement("SELECT * FROM PATIENT;");
////            ResultSet rs = stmt.executeQuery();
//
////            ResultSet rs = patientInfo("JIWON", "KIM");
//
//            ResultSet rs = patientInfo("Jinsoo", "Choi", Date.valueOf("1989-05-04"));
//
//            while (rs.next()) {
//                String f_name = rs.getString("f_name");
//                String l_name = rs.getString("l_name");
//                String sex  = rs.getString("sex");
//                Date birth = rs.getDate("birth");
//                Boolean malaria = rs.getBoolean("malaria");
//                Boolean measles = rs.getBoolean("measles");
//                Boolean dpt = rs.getBoolean("dpt");
//
//                System.out.println("NAME = " + f_name + " " + l_name);
//                System.out.println("SEX = " + sex);
//                System.out.println("DOB = " + birth);
//                System.out.println("Malaria Complete = " + malaria);
//                System.out.println("Measles Complete = " + measles);
//                System.out.println("DPT Complete = " + dpt);
//                System.out.println();
//            }
//
//            rs.close();
////            stmt.close();
//            conn.close();
//
//        } catch (Exception e) {
//            System.err.println(e.getClass().getName() + ": " + e.getMessage() );
//            System.exit(0);
//        }
//    }
}
