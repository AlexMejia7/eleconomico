package com.example.eleconomico;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.ViewHolder> {

    private List<Pedido> pedidos;
    private OnPedidoClickListener listener;

    public interface OnPedidoClickListener {
        void onPedidoClick(Pedido pedido);
    }

    public PedidoAdapter(List<Pedido> pedidos, OnPedidoClickListener listener) {
        this.pedidos = pedidos;
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
        holder.bind(pedido, listener);
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIdPedido, tvCliente, tvEstado, tvTotal;

        ViewHolder(View itemView) {
            super(itemView);
            tvIdPedido = itemView.findViewById(R.id.tvIdPedido);
            tvCliente = itemView.findViewById(R.id.tvCliente);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            tvTotal = itemView.findViewById(R.id.tvTotal);
        }

        void bind(final Pedido pedido, final OnPedidoClickListener listener) {
            tvIdPedido.setText("Pedido #" + pedido.getIdPedido());
            tvCliente.setText("Cliente: " + pedido.getNombreUsuario());
            tvEstado.setText("Estado: " + pedido.getEstado());
            tvTotal.setText(String.format("Total: L %.2f", pedido.getTotal()));

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPedidoClick(pedido);
                }
            });
        }
    }
}
