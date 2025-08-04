package com.example.eleconomico;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PedidoActivity extends AppCompatActivity {

    private Button btnRealizarPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        btnRealizarPedido = findViewById(R.id.btnRealizarPedido);

        btnRealizarPedido.setOnClickListener(v -> {
            // TODO: Implementar lógica de crear pedido con selección de productos y GPS
            Toast.makeText(this, "Pedido realizado (simulado)", Toast.LENGTH_SHORT).show();
            finish(); // Volver a dashboard
        });
    }
}
