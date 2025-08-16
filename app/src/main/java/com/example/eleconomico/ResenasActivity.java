package com.example.eleconomico;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResenasActivity extends AppCompatActivity {

    private RecyclerView rvResenas;
    private EditText etComentario;
    private RatingBar rbCalificacion;
    private Button btnEnviarResena;

    private ArrayList<Resena> resenaList;
    private ResenasAdapter adapter;

    private String URL_RESEÑAS = "http://34.31.145.38/reseñas.php"; // Ajusta según tu servidor

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resenas);

        rvResenas = findViewById(R.id.rvResenas);
        etComentario = findViewById(R.id.etComentario);
        rbCalificacion = findViewById(R.id.rbCalificacion);
        btnEnviarResena = findViewById(R.id.btnEnviarResena);

        resenaList = new ArrayList<>();
        adapter = new ResenasAdapter(resenaList);

        rvResenas.setLayoutManager(new LinearLayoutManager(this));
        rvResenas.setAdapter(adapter);

        cargarResenas();

        btnEnviarResena.setOnClickListener(v -> enviarResena());
    }

    private void cargarResenas() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL_RESEÑAS,
                null,
                response -> {
                    resenaList.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            Resena resena = new Resena(
                                    obj.getString("id_usuario"), // o nombre si lo tienes
                                    obj.getString("comentario"),
                                    (float) obj.optDouble("calificacion", 0)
                            );
                            resenaList.add(resena);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                },
                error -> Toast.makeText(ResenasActivity.this, "Error al cargar reseñas", Toast.LENGTH_SHORT).show()
        );

        queue.add(jsonArrayRequest);
    }

    private void enviarResena() {
        String comentario = etComentario.getText().toString().trim();
        int calificacion = (int) rbCalificacion.getRating();

        if (comentario.isEmpty()) {
            Toast.makeText(this, "Escribe un comentario", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        try {
            JSONObject json = new JSONObject();
            json.put("id_usuario", 1); // Ajusta según SessionManager si quieres el id real
            json.put("comentario", comentario);
            json.put("calificacion", calificacion);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    URL_RESEÑAS,
                    json,
                    response -> {
                        Toast.makeText(ResenasActivity.this, "Reseña enviada", Toast.LENGTH_SHORT).show();
                        etComentario.setText("");
                        rbCalificacion.setRating(5);
                        cargarResenas();
                    },
                    error -> Toast.makeText(ResenasActivity.this, "Error al enviar reseña", Toast.LENGTH_SHORT).show()
            );

            queue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
