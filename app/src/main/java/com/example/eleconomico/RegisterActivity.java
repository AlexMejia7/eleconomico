package com.example.eleconomico;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnRegister;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        apiService = ApiClient.getClient().create(ApiService.class);

        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Completa los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setCorreo(email);
            nuevoUsuario.setContrasena(password);

            Call<Mensaje> call = apiService.registrarUsuario(nuevoUsuario);
            call.enqueue(new Callback<Mensaje>() {
                @Override
                public void onResponse(Call<Mensaje> call, Response<Mensaje> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(RegisterActivity.this, response.body().getMensaje(), Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Error en la respuesta", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Mensaje> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
