package com.example.eleconomico;

public class Repartidor {
    private String idRepartidor;
    private String nombre;

    public Repartidor(String idRepartidor, String nombre) {
        this.idRepartidor = idRepartidor;
        this.nombre = nombre;
    }

    public String getIdRepartidor() {
        return idRepartidor;
    }

    public void setIdRepartidor(String idRepartidor) {
        this.idRepartidor = idRepartidor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre; // Esto hace que el Spinner muestre el nombre del repartidor
    }
}
