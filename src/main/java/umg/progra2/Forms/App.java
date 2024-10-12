    package umg.progra2.Forms;

    import umg.progra2.DataBase.Model.Producto;
    import umg.progra2.DataBase.Services.ProductoService;
    import umg.progra2.Reportes.PdfReport;

    import javax.swing.*;
    import javax.swing.table.DefaultTableModel;
    import java.awt.*;
    import java.sql.SQLException;
    import java.util.List;


    public class App extends JFrame{
        private JPanel Panel;
        private JTextField txtId;
        private JTextField txtDescripcion;
        private JTextField txtOrigen;
        private JTextField txtPrecio;
        private JTextField txtExistencia;
        private JButton btnInsertar;
        private JButton btnBuscar;
        private JButton btnEliminar;
        private JButton btnActualizar;
        private JTable tblProductos;
        private DefaultTableModel modeloTabla;
        private JComboBox cmbReportes;
        private JButton btnGenerarReporte;

        // Añadir el servicio de productos
        private ProductoService productoService = new ProductoService();


        public App(){
            setTitle("Gestion de Productos");
            setSize(800, 600);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            add(Panel, BorderLayout.CENTER);

            // Configurar el modelo de la tabla
            modeloTabla = new DefaultTableModel();
            modeloTabla.setColumnIdentifiers(new Object[] {"ID", "Descripción", "Origen", "Precio", "Existencia"});
            tblProductos.setModel(modeloTabla);  // Asignar el modelo a la tabla


            // Configurar combo box (se asume que cmbReportes ya fue creado en el editor)
            cmbReportes.addItem("Existencias menores a 20");
            cmbReportes.addItem("Precios mayores a 2000");
            cmbReportes.addItem("Productos ordenados por país");
            cmbReportes.addItem("Reporte agrupado por país");

            setLocationRelativeTo(null);

            // Cargar los datos en la tabla al iniciar
            cargarDatosTabla();
            

            btnInsertar.addActionListener(e -> insertarProducto());

            btnBuscar.addActionListener(e -> buscarProducto());

            // Acción para actualizar producto
            btnActualizar.addActionListener(e -> actualizarProducto());

            // Acción para eliminar producto
            btnEliminar.addActionListener(e -> eliminarProducto());

            // Acción para generar el reporte
            btnGenerarReporte.addActionListener(e -> {
                String seleccionReporte = (String) cmbReportes.getSelectedItem();

                switch (seleccionReporte) {
                    case "Existencias menores a 20":
                        generarReporteConCondicion("existencia < 20", "Reporte de Existencias Menores a 20 Unidades");
                        break;

                    case "Precios mayores a 2000":
                        generarReporteConCondicion("precio > 2000", "Reporte de Precios Mayores a 2000");
                        break;

                    case "Productos ordenados por país":
                        generarReporteConCondicion("1=1 ORDER BY origen", "Reporte de Productos Ordenados por País");
                        break;

                    case "Reporte agrupado por país":
                        generarReporteConCondicion("1=1 ORDER BY origen, precio DESC", "Reporte Agrupado por País y Ordenado por Precio");
                        break;

                    default:
                        JOptionPane.showMessageDialog(this, "Seleccione un reporte válido.");
                }
            });


        }

        // Método para cargar los datos de productos en la tabla
        private void cargarDatosTabla() {
            try {
                modeloTabla.setRowCount(0);  // Limpiar la tabla
                for (Producto producto : productoService.obtenerTodos()) {
                    modeloTabla.addRow(new Object[]{
                            producto.getIdProducto(),
                            producto.getDescripcion(),
                            producto.getOrigen(),
                            producto.getPrecio(),
                            producto.getExistencia()
                    });
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar los productos: " + e.getMessage());
            }
        }

        // Método para insertar un producto
        private void insertarProducto() {
            try {
                Producto producto = new Producto(
                        txtDescripcion.getText(),
                        txtOrigen.getText(),
                        Double.parseDouble(txtPrecio.getText()),
                        Integer.parseInt(txtExistencia.getText())
                );
                productoService.crearProducto(producto);
                cargarDatosTabla();  // Recargar los datos en la tabla
                limpiarCampos();  // Limpiar los campos de texto
                JOptionPane.showMessageDialog(this, "Producto insertado con éxito");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al insertar el producto: " + ex.getMessage());
            }
        }

        // Método para buscar un producto por ID
        private void buscarProducto() {
            try {
                int id = Integer.parseInt(txtId.getText());
                Producto producto = productoService.obtenerProducto(id);
                if (producto != null) {
                    txtDescripcion.setText(producto.getDescripcion());
                    txtOrigen.setText(producto.getOrigen());
                    txtPrecio.setText(String.valueOf(producto.getPrecio()));
                    txtExistencia.setText(String.valueOf(producto.getExistencia()));
                } else {
                    JOptionPane.showMessageDialog(this, "Producto no encontrado");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al buscar el producto: " + ex.getMessage());
            }
        }

        // Método para actualizar un producto
        private void actualizarProducto() {
            try {
                int id = Integer.parseInt(txtId.getText());
                Producto producto = new Producto(
                        id,
                        txtDescripcion.getText(),
                        txtOrigen.getText(),
                        Double.parseDouble(txtPrecio.getText()),
                        Integer.parseInt(txtExistencia.getText())
                );
                productoService.actualizarProducto(producto);
                cargarDatosTabla();  // Recargar los datos en la tabla
                limpiarCampos();  // Limpiar los campos de texto
                JOptionPane.showMessageDialog(this, "Producto actualizado con éxito");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al actualizar el producto: " + ex.getMessage());
            }
        }

        // Método para eliminar un producto
        private void eliminarProducto() {
            try {
                int id = Integer.parseInt(txtId.getText());
                productoService.eliminarProducto(id);
                cargarDatosTabla();  // Recargar los datos en la tabla
                limpiarCampos();  // Limpiar los campos de texto
                JOptionPane.showMessageDialog(this, "Producto eliminado con éxito");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar el producto: " + ex.getMessage());
            }
        }

        // Método para limpiar los campos de texto
        private void limpiarCampos() {
            txtId.setText("");
            txtDescripcion.setText("");
            txtOrigen.setText("");
            txtPrecio.setText("");
            txtExistencia.setText("");
        }

        private void generarReporteConCondicion(String condition, String tituloReporte) {
            try {
                List<Producto> productos = productoService.obtenerProductosConCondicion(condition);
                if (productos.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No se encontraron productos para este reporte.");
                    return;
                }

                PdfReport pdfReport = new PdfReport();
                String outputPath = "reporte_productos.pdf";
                pdfReport.generateProductReport(productos, outputPath, tituloReporte);

                JOptionPane.showMessageDialog(this, "Reporte generado: " + outputPath);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + ex.getMessage());
            }
        }

        private void insertarProductosDePrueba() {
            try {
                String[][] productosPrueba = {
                        {"Laptop Dell", "USA", "1200.00", "10"},
                        {"Smartphone Samsung", "Corea del Sur", "950.00", "15"},
                        {"Televisor LG", "Corea del Sur", "800.00", "5"},
                        {"Tablet Huawei", "China", "600.00", "20"},
                        {"Bicicleta Trek", "USA", "1500.00", "7"},
                        {"Cámara Nikon", "Japón", "1300.00", "3"},
                        {"Refrigerador LG", "Corea del Sur", "1800.00", "2"},
                        {"Cafetera Nespresso", "Suiza", "300.00", "25"},
                        {"Ventilador Xiaomi", "China", "200.00", "30"},
                        {"Lavadora Samsung", "Corea del Sur", "1200.00", "8"},
                        {"Microondas Panasonic", "Japón", "400.00", "18"},
                        {"Coche Toyota", "Japón", "22000.00", "1"},
                        {"Batería Externa", "China", "50.00", "50"},
                        {"Smartwatch Apple", "USA", "400.00", "12"},
                        {"Audífonos Bose", "USA", "350.00", "10"},
                        {"Proyector Epson", "Japón", "700.00", "4"},
                        {"Drone DJI", "China", "1000.00", "5"},
                        {"Monitor Dell", "USA", "300.00", "14"},
                        {"Ratón Logitech", "Suiza", "25.00", "40"},
                        {"Teclado Razer", "USA", "150.00", "22"},
                        {"Parlante JBL", "USA", "250.00", "35"},
                        {"Smartphone iPhone", "USA", "1200.00", "9"},
                        {"Cámara Canon", "Japón", "900.00", "6"},
                        {"Impresora HP", "USA", "250.00", "17"},
                        {"Procesador Intel", "USA", "500.00", "11"},
                        {"Placa Base ASUS", "Taiwán", "300.00", "10"},
                        {"Auriculares Sony", "Japón", "150.00", "19"},
                        {"Tableta Wacom", "Japón", "600.00", "8"},
                        {"Silla Gaming", "China", "350.00", "12"},
                        {"Coche Tesla", "USA", "35000.00", "1"}
                };

                for (String[] productoData : productosPrueba) {
                    Producto producto = new Producto(
                            productoData[0],
                            productoData[1],
                            Double.parseDouble(productoData[2]),
                            Integer.parseInt(productoData[3])
                    );
                    productoService.crearProducto(producto);  // Insertar producto en la base de datos
                }

                cargarDatosTabla();  // Recargar la tabla con los nuevos productos
                JOptionPane.showMessageDialog(this, "Productos de prueba insertados con éxito.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al insertar productos de prueba: " + ex.getMessage());
            }
        }


        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                App frame = new App();
                frame.setVisible(true);
            });

        }
    }
