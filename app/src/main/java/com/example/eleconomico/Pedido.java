package com.example.eleconomico;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Pedido {

    @SerializedName("id_pedido")
    private String idPedido;

    @SerializedName("id_usuario")
    private String idUsuario;

    @SerializedName("nombre_usuario")
    private String nombreUsuario; // Nombre del usuario

    @SerializedName("productos")
    private List<Producto> productos; // Lista de productos en el pedido

    @SerializedName("estado")
    private String estado; // "pendiente", "en camino", "entregado", etc.

    @SerializedName("fecha_pedido")
    private String fecha; // Fecha del pedido

    @SerializedName("total")
    private double total; // Total del pedido

    @SerializedName("id_repartidor")
    private String idRepartidor; // ID del repartidor asignado (puede ser null)

    // Constructor vacío
    public Pedido() {}

    // Constructor completo
    public Pedido(String idPedido, String idUsuario, String nombreUsuario, List<Producto> productos,
                  String estado, String fecha, double total, String idRepartidor) {
        this.idPedido = idPedido;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.productos = productos;
        this.estado = estado;
        this.fecha = fecha;
        this.total = total;
        this.idRepartidor = idRepartidor;
    }

    // Constructor simple
    public Pedido(String idPedido, String estado, double total) {
        this.idPedido = idPedido;
        this.estado = estado;
        this.total = total;
    }

    // Getters y setters
    public String getIdPedido() { return idPedido != null ? idPedido : "N/A"; }
    public void setIdPedido(String idPedido) { this.idPedido = idPedido; }

    public String getIdUsuario() { return idUsuario; }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreUsuario() { return nombreUsuario != null ? nombreUsuario : "Desconocido"; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public List<Producto> getProductos() { return productos; }
    public void setProductos(List<Producto> productos) { this.productos = productos; }

    public String getEstado() { return estado != null ? estado : "Pendiente"; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getIdRepartidor() { return idRepartidor; }
    public void setIdRepartidor(String idRepartidor) { this.idRepartidor = idRepartidor; }

    @Override
    public String toString() {
        return "Pedido{" +
                "idPedido='" + getIdPedido() + '\'' +
                ", idUsuario='" + idUsuario + '\'' +
                ", nombreUsuario='" + getNombreUsuario() + '\'' +
                ", estado='" + getEstado() + '\'' +
                ", total=" + total +
                ", idRepartidor='" + idRepartidor + '\'' +
                ", fecha='" + fecha + '\'' +
                ", productos=" + productos +
                '}';
    }
}
