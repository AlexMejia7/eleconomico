package com.example.eleconomico;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    private Button btnLogin, btnRegister, btnDashboard, btnPedido, btnPerfil, btnLogout;
    private TextView tvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        tvWelcome = findViewById(R.id.tvWelcome);

        SharedPreferences prefs = getSharedPreferences("EconómicoPrefs", MODE_PRIVATE);
        String nombre = prefs.getString("nombre_usuario", "Repartidor");

        tvWelcome.setText("Bienvenido, " + nombre);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnDashboard = findViewById(R.id.btnDashboard);
        btnPedido = findViewById(R.id.btnPedido);
        btnPerfil = findViewById(R.id.btnPerfil);
        btnLogout = findViewById(R.id.btnLogout);

        btnLogin.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
        btnRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
        btnDashboard.setOnClickListener(v -> startActivity(new Intent(this, DashboardActivity.class)));
        btnPedido.setOnClickListener(v -> startActivity(new Intent(this, PedidoActivity.class)));
        btnPerfil.setOnClickListener(v -> startActivity(new Intent(this, PerfilActivity.class)));

        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            startActivity(new Intent(MenuActivity.this, LoginActivity.class));
            finish();
        });
    }
}
