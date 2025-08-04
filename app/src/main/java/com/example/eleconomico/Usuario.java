package com.example.eleconomico;

public class Usuario {

    private String email;
    private String nombre;
    private String descripcion;
    private String fotoUrl;

    public Usuario() {}

    public Usuario(String email, String nombre, String descripcion, String fotoUrl) {
        this.email = email;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fotoUrl = fotoUrl;
    }

    // getters y setters...
}
