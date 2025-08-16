package com.example.eleconomico;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private SessionManager sessionManager;
    private ApiService apiService;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);

        if (sessionManager.getUserName() != null) {
            startActivity(new Intent(LoginActivity.this, MenuActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        apiService = ApiClient.getClient().create(ApiService.class);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(v -> {
            String correo = etEmail.getText().toString().trim();
            String contrasena = etPassword.getText().toString();

            if (correo.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa correo y contraseña", Toast.LENGTH_SHORT).show();
                return;
            }

            JsonObject json = new JsonObject();
            json.addProperty("correo", correo);
            json.addProperty("contrasena", contrasena);

            Log.d(TAG, "Enviando login para: " + correo);

            Call<JsonObject> call = apiService.loginRaw(json);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d(TAG, "Respuesta recibida. Código: " + response.code());

                    if (response.isSuccessful() && response.body() != null) {
                        JsonObject body = response.body();
                        Log.d(TAG, "Body: " + body.toString());

                        if (body.has("usuario")) {
                            JsonObject usuarioJson = body.getAsJsonObject("usuario");
                            String nombre = usuarioJson.get("nombre").getAsString();
                            String id = usuarioJson.get("id").getAsString();

                            Log.d(TAG, "Usuario: " + nombre + " ID: " + id);

                            sessionManager.saveUserName(nombre);
                            sessionManager.saveUserId(id);

                            Toast.makeText(LoginActivity.this, "Bienvenido " + nombre, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                            finish();

                        } else {
                            Toast.makeText(LoginActivity.this, "⚠ Credenciales inválidas", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        String mensajeError;
                        switch (response.code()) {
                            case 401:
                                mensajeError = "⚠ Usuario o contraseña incorrectos";
                                break;
                            case 404:
                                mensajeError = "❌ Endpoint no encontrado en el servidor";
                                break;
                            case 500:
                                mensajeError = "💥 Error interno del servidor";
                                break;
                            default:
                                mensajeError = "⚠ Error desconocido: " + response.code();
                                break;
                        }
                        Toast.makeText(LoginActivity.this, mensajeError, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG, "Error conexión: " + t.getMessage(), t);
                    Toast.makeText(LoginActivity.this, "🌐 No se pudo conectar: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}

