package umg.progra2.DataBase.Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:src/main/java/umg/progra2/data/productos.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}

