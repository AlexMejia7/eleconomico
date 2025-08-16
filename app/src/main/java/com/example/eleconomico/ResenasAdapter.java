package com.example.eleconomico;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ResenasAdapter extends RecyclerView.Adapter<ResenasAdapter.ResenaViewHolder> {

    private List<Resena> resenaList;

    public ResenasAdapter(List<Resena> resenaList) {
        this.resenaList = resenaList;
    }

    @NonNull
    @Override
    public ResenaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_resena, parent, false);
        return new ResenaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResenaViewHolder holder, int position) {
        Resena resena = resenaList.get(position);
        holder.tvNombre.setText(resena.getNombre());
        holder.tvComentario.setText(resena.getComentario());
        holder.rbCalificacion.setRating(resena.getCalificacion());
    }

    @Override
    public int getItemCount() {
        return resenaList.size();
    }

    public static class ResenaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvComentario;
        RatingBar rbCalificacion;

        public ResenaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvComentario = itemView.findViewById(R.id.tvComentario);
            rbCalificacion = itemView.findViewById(R.id.rbCalificacion);
        }
    }
}
