package com.example.eleconomico;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {

    private List<Producto> productos;
    private OnProductoClickListener listener;

    public interface OnProductoClickListener {
        void onProductoClick(Producto producto);
    }

    public ProductoAdapter(List<Producto> productos, OnProductoClickListener listener) {
        this.productos = productos;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.bind(producto, listener);
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvPrecio;
        Button btnAgregar;

        ViewHolder(View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            btnAgregar = itemView.findViewById(R.id.btnAgregar);
        }

        void bind(Producto producto, OnProductoClickListener listener) {
            tvNombre.setText(producto.getNombre());
            tvPrecio.setText(String.format("L %.2f", producto.getPrecio()));
            btnAgregar.setOnClickListener(v -> listener.onProductoClick(producto));
        }
    }
}
