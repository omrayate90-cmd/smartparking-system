import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    static final String URL =
        "jdbc:mysql://localhost:3306/electrical_smart_car_parking";

    static final String USER = "root";
    static final String PASSWORD = "obr2005";

    public static Connection getConnection() {

        try {

            Connection con = DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
            );

            System.out.println("MySQL Connected Successfully!");

            return con;

        } catch (Exception e) {

            System.out.println("Database Connection Failed!");
            e.printStackTrace();

            return null;
        }
    }
}