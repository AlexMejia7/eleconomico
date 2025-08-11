package com.example.eleconomico;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);

        // Verificar si ya hay sesión guardada usando SessionManager
        String nombreGuardado = sessionManager.getUserName();

        if (nombreGuardado != null) {
            // Ya hay sesión activa → saltamos login
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

            Call<JsonObject> call = apiService.loginRaw(json);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        JsonObject body = response.body();

                        if (body.has("usuario")) {
                            JsonObject usuarioJson = body.getAsJsonObject("usuario");
                            String nombre = usuarioJson.get("nombre").getAsString();
                            String id = usuarioJson.get("id").getAsString();

                            // Guardar en SessionManager
                            sessionManager.saveUserName(nombre);
                            sessionManager.saveUserId(id);

                            Toast.makeText(LoginActivity.this, "Bienvenido " + nombre, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                            finish();

                        } else {
                            Toast.makeText(LoginActivity.this, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}
