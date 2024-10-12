package umg.progra2.DataBase.Model;

public class Producto {
    private int idProducto;
    private String descripcion;
    private String origen;
    private double precio; // Nuevo campo agregado
    private int existencia;
    private int peso;

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    // Constructor
    public Producto(int idProducto, String descripcion, String origen, double precio, int existencia) {
        this.idProducto = idProducto;
        this.descripcion = descripcion;
        this.origen = origen;
        this.precio = precio;
        this.existencia = existencia;
    }
    public Producto() {
    }

    // Constructor para crear productos sin el ID (por ejemplo, para insertar)
    public Producto(String descripcion, String origen, double precio, int existencia) {
        this.descripcion = descripcion;
        this.origen = origen;
        this.precio = precio;
        this.existencia = existencia;
    }

    // Getters y setters
    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getOrigen() {
        return origen;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getExistencia() {
        return existencia;
    }

    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    //para imprimir en consola
    @Override
    public String toString() {
        return "Producto {" +
                "ID: " + idProducto +
                ", Descripción: '" + descripcion + '\'' +
                ", Origen: '" + origen + '\'' +
                ", Precio: " + precio +
                ", Existencia: " + existencia +
                '}';
    }
}
