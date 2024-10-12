package umg.progra2.DataBase.Dao;
import umg.progra2.DataBase.Conexion.DatabaseConnection;
import umg.progra2.DataBase.Model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ProductoDao {

    public List<Producto> obtenerProductosConCondicion(String condition) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM tb_producto WHERE " + condition + " ORDER BY origen";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                productos.add(new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("descripcion"),
                        rs.getString("origen"),
                        rs.getDouble("precio"),
                        rs.getInt("existencia")
                ));
            }
        }
        return productos;
    }


    public List<Producto> obtenerTodos() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM tb_producto";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                productos.add(new Producto(rs.getInt("id_producto"),
                        rs.getString("descripcion"),
                        rs.getString("origen"),
                        rs.getDouble("precio"),
                        rs.getInt("existencia")));
            }
        }
        return productos;
    }


    public void insertar(Producto producto) throws SQLException {
        String sql = "INSERT INTO tb_producto (descripcion, origen, precio, existencia) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            pstmt.setString(1, producto.getDescripcion());
            pstmt.setString(2, producto.getOrigen());
            pstmt.setDouble(3, producto.getPrecio());
            pstmt.setInt(4, producto.getExistencia());
            pstmt.executeUpdate();

        //Obtener el ID generado automáticamente y asignarlo al objeto Producto
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if(generatedKeys.next()){
                    producto.setIdProducto(generatedKeys.getInt(1));
                }
            }
        }
    }


    public Producto obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_producto WHERE id_producto = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Producto(rs.getInt("id_producto"), rs.getString("descripcion"), rs.getString("origen"), rs.getDouble("precio"),rs.getInt("existencia") );
                }
            }
        }
        return null;
    }

    public void actualizar(Producto producto) throws SQLException {
        String sql = "UPDATE tb_producto SET descripcion = ?, origen = ? WHERE id_producto = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, producto.getDescripcion());
            pstmt.setString(2, producto.getOrigen());
            pstmt.setInt(3, producto.getIdProducto());
            pstmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM tb_producto WHERE id_producto = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }


    public boolean eliminarCondicional(int id) throws SQLException {
        String sqlCheck = "SELECT precio FROM tb_producto WHERE id_producto = ?";
        String sqlDelete = "DELETE FROM tb_producto WHERE id_producto = ? AND precio = 0.00";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck);
             PreparedStatement pstmtDelete = conn.prepareStatement(sqlDelete)) {

            // Primero, verificamos el precio del producto
            pstmtCheck.setInt(1, id);
            try (ResultSet rs = pstmtCheck.executeQuery()) {
                if (rs.next()) {
                    double precio = rs.getDouble("precio");
                    if (precio != 0.00) {
                        // El precio no es 0.00, no se puede eliminar
                        return false;
                    }
                } else {
                    // El producto no existe
                    return false;
                }
            }

            // Si llegamos aquí, el precio es 0.00, procedemos con la eliminación
            pstmtDelete.setInt(1, id);
            int rowsAffected = pstmtDelete.executeUpdate();

            // Si se eliminó al menos una fila, consideramos que fue exitoso
            return rowsAffected > 0;
        }
    } // fin de eliminarCondicional

}



