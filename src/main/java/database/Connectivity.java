package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connectivity {
    public static Connection connectDb() {
        try {
            // Chargement du pilote JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connexion à la base de données
            String url = "jdbc:mysql://localhost:3306/locasapp?useSSL=false";
            String user = "root";
            String password = "";
            Connection connect = DriverManager.getConnection(url, user, password);

            return connect;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
