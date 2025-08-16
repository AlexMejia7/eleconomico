package com.example.eleconomico;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    private Button btnLogin, btnRegister, btnDashboard, btnPedido, btnPerfil, btnLogout, btnRepartidor, btnResenas;
    private TextView tvWelcome;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        sessionManager = new SessionManager(this);

        tvWelcome = findViewById(R.id.tvWelcome);

        String nombre = sessionManager.getUserName();
        if (nombre == null) {
            nombre = "Repartidor";  // Valor por defecto si no hay nombre guardado
        }

        tvWelcome.setText("Bienvenido, " + nombre);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnDashboard = findViewById(R.id.btnDashboard);
        btnPedido = findViewById(R.id.btnPedido);
        btnPerfil = findViewById(R.id.btnPerfil);
        btnLogout = findViewById(R.id.btnLogout);
        btnRepartidor = findViewById(R.id.btnRepartidor); // NUEVO botón repartidor
        btnResenas = findViewById(R.id.btnResenas); // NUEVO botón Reseñas

        btnLogin.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
        btnRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
        btnDashboard.setOnClickListener(v -> startActivity(new Intent(this, DashboardActivity.class)));
        btnPedido.setOnClickListener(v -> startActivity(new Intent(this, PedidoActivity.class)));
        btnPerfil.setOnClickListener(v -> startActivity(new Intent(this, PerfilActivity.class)));

        btnRepartidor.setOnClickListener(v -> startActivity(new Intent(this, RepartidorActivity.class))); // ABRIR actividad repartidor

        btnResenas.setOnClickListener(v -> startActivity(new Intent(this, ResenasActivity.class))); // ABRIR actividad reseñas

        btnLogout.setOnClickListener(v -> {
            sessionManager.clearSession();
            startActivity(new Intent(MenuActivity.this, LoginActivity.class));
            finish();
        });
    }
}
