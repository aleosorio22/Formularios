package umg.progra2.DataBase.Conexion;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initializeDatabase() {
        String sqlCreateTable = "CREATE TABLE IF NOT EXISTS tb_producto (" +
                "id_producto INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "descripcion TEXT NOT NULL, " +
                "origen TEXT NOT NULL, " +
                "precio REAL NOT NULL, " +
                "existencia INTEGER NOT NULL" +
                ");";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlCreateTable);
            System.out.println("Tabla `tb_producto` creada exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al crear la tabla: " + e.getMessage());
        }
    }
}
