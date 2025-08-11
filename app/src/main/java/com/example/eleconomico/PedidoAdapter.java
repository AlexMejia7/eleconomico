package com.example.eleconomico;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.ViewHolder> {

    public interface OnPedidoClickListener {
        void onPedidoClick(Pedido pedido, Repartidor repartidorSeleccionado);
    }

    private List<Pedido> pedidos;
    private List<Repartidor> repartidores;
    private OnPedidoClickListener listener;
    private Context context;

    public PedidoAdapter(Context context, List<Pedido> pedidos, List<Repartidor> repartidores, OnPedidoClickListener listener) {
        this.context = context;
        this.pedidos = pedidos;
        this.repartidores = repartidores;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pedido, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pedido pedido = pedidos.get(position);
        holder.bind(pedido, repartidores, listener);
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIdPedido, tvCliente, tvEstado, tvTotal;
        Spinner spinnerRepartidor;

        private boolean spinnerInitialized = false; // Para evitar disparos al inicializar

        ViewHolder(View itemView) {
            super(itemView);
            tvIdPedido = itemView.findViewById(R.id.tvIdPedido);
            tvCliente = itemView.findViewById(R.id.tvCliente);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            spinnerRepartidor = itemView.findViewById(R.id.spinnerRepartidor);
        }

        void bind(final Pedido pedido, List<Repartidor> repartidores, final OnPedidoClickListener listener) {
            tvIdPedido.setText("Pedido #" + pedido.getIdPedido());
            tvCliente.setText("Cliente: " + pedido.getNombreUsuario());
            tvEstado.setText("Estado: " + pedido.getEstado());
            tvTotal.setText(String.format("Total: L %.2f", pedido.getTotal()));

            // Configurar el spinner con la lista de repartidores
            ArrayAdapter<Repartidor> adapter = new ArrayAdapter<>(
                    itemView.getContext(),
                    android.R.layout.simple_spinner_item,
                    repartidores
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerRepartidor.setAdapter(adapter);

            // Seleccionar repartidor asignado por defecto en spinner
            int index = findRepartidorIndex(repartidores, pedido.getIdRepartidor());
            spinnerRepartidor.setSelection(index);

            spinnerInitialized = false; // Reset para evitar trigger inicial

            spinnerRepartidor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (!spinnerInitialized) {
                        spinnerInitialized = true; // Primera vez ignorar
                        return;
                    }
                    Repartidor repartidorSeleccionado = repartidores.get(position);
                    if (listener != null) {
                        listener.onPedidoClick(pedido, repartidorSeleccionado);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // No hacer nada
                }
            });

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPedidoClick(pedido, (Repartidor) spinnerRepartidor.getSelectedItem());
                }
            });
        }

        // Busca índice del repartidor asignado para seleccionar en el spinner
        private int findRepartidorIndex(List<Repartidor> repartidores, String idRepartidor) {
            for (int i = 0; i < repartidores.size(); i++) {
                if (repartidores.get(i).getIdRepartidor().equals(idRepartidor)) {
                    return i;
                }
            }
            return 0; // si no encuentra, devuelve índice 0
        }
    }
}
