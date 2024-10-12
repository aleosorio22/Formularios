package umg.progra2.Reportes;

import umg.progra2.DataBase.Conexion.DatabaseConnection;
import umg.progra2.DataBase.Conexion.DatabaseInitializer;
import umg.progra2.DataBase.Dao.ProductoDao;
import umg.progra2.DataBase.Model.Producto;
import umg.progra2.DataBase.Services.ProductoService;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.List;

public class prueba {
    public static void main(String[] args) {
        try {
            //1. Inicializar bd y el dao

            ProductoDao productoDao = new ProductoDao();

            //2. Obtener todos los productos
            List<Producto> productos = productoDao.obtenerTodos();

            //3. Mostrar todos los productos en consola
            if(productos.isEmpty()){
                JOptionPane.showMessageDialog(null, "No hay productos en la base de datos");
            }else {
                System.out.println("Lista de productos en base de datos");
                for (Producto producto : productos) {
                    System.out.println(producto.toString());
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error al obtener los datos de la base de datos");
        }

//        ProductoService productoService = new ProductoService();
//        try {
//            String[] opciones = {
//                    "Reporte de productos con existencia menor a 30 unidades",
//                    "Reporte de productos con precio entre 200 y 400",
//                    "Reporte ordenado por precio de mayor a menor",
//                    "Reporte ordenado por existencias de menor a mayor",
//                    "Generar reporte de todos los productos"
//            };
//
//            // Mostrar un cuadro de diálogo para que el usuario seleccione el reporte a generar
//            String opcionSeleccionada = (String) JOptionPane.showInputDialog(
//                    null,
//                    "Seleccione el reporte que desea generar:",
//                    "Generar Reporte",
//                    JOptionPane.QUESTION_MESSAGE,
//                    null,
//                    opciones,
//                    opciones[0]
//            );
//
//            List<Producto> productos = null;
//
//            // Obtener los productos según la opción seleccionada
//            if (opcionSeleccionada != null) {
//                switch (opcionSeleccionada) {
//                    case "Reporte de productos con existencia menor a 30 unidades":
//                        productos = productoService.obtenerProductosConExistenciaMenorA30();
//                        break;
//                    case "Reporte de productos con precio entre 200 y 400":
//                        productos = productoService.obtenerProductosConPrecioEntre200y400();
//                        break;
//                    case "Reporte ordenado por precio de mayor a menor":
//                        productos = productoService.obtenerProductosOrdenadosPorPrecioDesc();
//                        break;
//                    case "Reporte ordenado por existencias de menor a mayor":
//                        productos = productoService.obtenerProductosOrdenadosPorExistenciasAsc();
//                        break;
//                    case "Generar reporte de todos los productos":
//                        productos = productoService.obtenerTodosLosProductos();
//                        break;
//                }
//
//                if (productos != null && !productos.isEmpty()) {
//                    // Generar el reporte PDF con los productos obtenidos
//                    new PdfReport().generateProductReport(productos, "C:\\projects\\reporte.pdf");
//                    JOptionPane.showMessageDialog(null, "Reporte generado exitosamente.");
//                } else {
//                    JOptionPane.showMessageDialog(null, "No se encontraron productos para el reporte seleccionado.");
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Ocurrió un error al generar el reporte: " + e.getMessage());
//        }
    }
}

