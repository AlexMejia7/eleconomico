package com.example.eleconomico;


import java.util.List;

public class Pedido {
    private String id;
    private String idUsuario;
    private List<Producto> productos;
    private String estado;
    private String fecha;
    private double total;

    public Pedido() {}

    public Pedido(String id, String idUsuario, List<Producto> productos, String estado, String fecha, double total) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.productos = productos;
        this.estado = estado;
        this.fecha = fecha;
        this.total = total;
    }

    // getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getIdUsuario() { return idUsuario; }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }

    public List<Producto> getProductos() { return productos; }
    public void setProductos(List<Producto> productos) { this.productos = productos; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}
