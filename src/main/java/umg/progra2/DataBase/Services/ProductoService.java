package umg.progra2.DataBase.Services;

import umg.progra2.DataBase.Dao.ProductoDao;
import umg.progra2.DataBase.Model.Producto;

import java.sql.SQLException;
import java.util.List;

public class ProductoService {
    private ProductoDao productoDAO;

    public ProductoService() {
        this.productoDAO = new ProductoDao();
    }

    // Constructor adicional con ProductoDao como parámetro
    public ProductoService(ProductoDao productoDAO) {
        this.productoDAO = productoDAO;
    }

    public void crearProducto(Producto producto) throws SQLException {
        productoDAO.insertar(producto);
    }

    public Producto obtenerProducto(int id) throws SQLException {
        return productoDAO.obtenerPorId(id);
    }

    public List<Producto> obtenerTodos() throws SQLException {
        return productoDAO.obtenerTodos();
    }

    public void actualizarProducto(Producto producto) throws SQLException {
        productoDAO.actualizar(producto);
    }

    public void eliminarProducto(int id) throws SQLException {
        productoDAO.eliminar(id);
    }

    // Método para obtener productos según una condición
    public List<Producto> obtenerProductosConCondicion(String condicion) throws SQLException {
        return productoDAO.obtenerProductosConCondicion(condicion);
    }
}
