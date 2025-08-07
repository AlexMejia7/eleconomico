package com.example.eleconomico;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    private Button btnLogin, btnRegister, btnDashboard, btnPedido, btnPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnDashboard = findViewById(R.id.btnDashboard);
        btnPedido = findViewById(R.id.btnPedido);
        btnPerfil = findViewById(R.id.btnPerfil);

        btnLogin.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
        btnRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
        btnDashboard.setOnClickListener(v -> startActivity(new Intent(this, DashboardActivity.class)));
        btnPedido.setOnClickListener(v -> startActivity(new Intent(this, PedidoActivity.class)));
        btnPerfil.setOnClickListener(v -> startActivity(new Intent(this, PerfilActivity.class)));
    }
}
