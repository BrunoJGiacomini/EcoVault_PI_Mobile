package com.example.ecovault_pi_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecovault_pi_mobile.adapter.PointCardAdapter;
import com.example.ecovault_pi_mobile.model.AcceptedItem;
import com.example.ecovault_pi_mobile.model.CollectionPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PontosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pontos);

        ImageButton btnVoltar = findViewById(R.id.btnVoltarPontos);
        btnVoltar.setOnClickListener(v -> finish());

        setupPontos();
    }

    private void setupPontos() {
        // MOCK - depois substituímos por chamada de API real (Retrofit/Volley)
        List<CollectionPoint> pontos = new ArrayList<>();

        pontos.add(new CollectionPoint(
                "1", "EcoPonto Centro", "Rua das Flores, 123 - Centro",
                "1.2km", "Aberto agora", true,
                Arrays.asList(
                        new AcceptedItem("item_celular", "Celular", 80, R.drawable.ic_smartphone),
                        new AcceptedItem("item_pilhas", "Pilhas", 30, R.drawable.ic_battery)
                )));

        pontos.add(new CollectionPoint(
                "2", "Recicla Fácil", "Av. Brasil, 456 - Jardim América",
                "2.5km", "Fechado", false,
                Arrays.asList(
                        new AcceptedItem("item_notebook", "Notebook", 120, R.drawable.ic_cpu),
                        new AcceptedItem("item_lampadas", "Lâmpadas", 20, R.drawable.ic_lightbulb)
                )));

        RecyclerView recycler = findViewById(R.id.recyclerPontos);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new PointCardAdapter(this, pontos, point -> {
            PontoDetailActivity.start(this, point);
        }));
    }
}
