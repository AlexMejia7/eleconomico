package com.example.eleconomico;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private Button btnNuevoPedido, btnPerfil, btnCerrarSesion;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sessionManager = new SessionManager(this);

        btnNuevoPedido = findViewById(R.id.btnNuevoPedido);
        btnPerfil = findViewById(R.id.btnPerfil);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        btnNuevoPedido.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, PedidoActivity.class);
            startActivity(intent);
        });

        btnPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, PerfilActivity.class);
            startActivity(intent);
        });

        btnCerrarSesion.setOnClickListener(v -> {
            sessionManager.clearSession();

            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
