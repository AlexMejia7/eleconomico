package com.example.eleconomico;

public class Usuario {

    private String idUsuario;          // id_usuario
    private String nombre;             // nombre
    private String correo;             // correo
    private transient String contrasena;  // contrasena, transient para no serializar
    private String fotoPerfilBase64;  // foto_perfil_base64
    private String descripcion;        // descripcion
    private String ubicacionGps;       // ubicacion_gps
    private String codigoVerificacion; // codigo_verificacion
    private boolean verificado;        // verificado
    private String apiKey;             // api_key
    private String fechaRegistro;      // fecha_registro
    private String rol;                // rol

    public Usuario() {}

    // Getters y setters
    public String getIdUsuario() { return idUsuario; }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getFotoPerfilBase64() { return fotoPerfilBase64; }
    public void setFotoPerfilBase64(String fotoPerfilBase64) { this.fotoPerfilBase64 = fotoPerfilBase64; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getUbicacionGps() { return ubicacionGps; }
    public void setUbicacionGps(String ubicacionGps) { this.ubicacionGps = ubicacionGps; }

    public String getCodigoVerificacion() { return codigoVerificacion; }
    public void setCodigoVerificacion(String codigoVerificacion) { this.codigoVerificacion = codigoVerificacion; }

    public boolean isVerificado() { return verificado; }
    public void setVerificado(boolean verificado) { this.verificado = verificado; }

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }

    public String getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
