package com.example.eleconomico;

import java.util.List;

public class Pedido {
    private String idPedido;
    private String idUsuario;
    private String nombreUsuario;   // Nuevo campo para el nombre del usuario
    private List<Producto> productos;
    private String estado;
    private String fecha;
    private double total;

    public Pedido() {}

    public Pedido(String idPedido, String idUsuario, String nombreUsuario, List<Producto> productos, String estado, String fecha, double total) {
        this.idPedido = idPedido;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.productos = productos;
        this.estado = estado;
        this.fecha = fecha;
        this.total = total;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
