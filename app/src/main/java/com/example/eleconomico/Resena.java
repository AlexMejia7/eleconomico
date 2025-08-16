package com.example.eleconomico;

public class Resena {
    private String nombre;
    private String comentario;
    private float calificacion;

    public Resena(String nombre, String comentario, float calificacion) {
        this.nombre = nombre;
        this.comentario = comentario;
        this.calificacion = calificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getComentario() {
        return comentario;
    }

    public float getCalificacion() {
        return calificacion;
    }
}
