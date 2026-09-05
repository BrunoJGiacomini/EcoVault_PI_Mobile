package com.example.ecovault_pi_mobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecovault_pi_mobile.adapter.AcceptedItemAdapter;
import com.example.ecovault_pi_mobile.model.CollectionPoint;

public class PontoDetailActivity extends AppCompatActivity {

    private static CollectionPoint currentPoint; // simples: guarda em memória (mock)

    public static void start(Context context, CollectionPoint point) {
        currentPoint = point;
        context.startActivity(new Intent(context, PontoDetailActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ponto_detail);

        ImageButton btnVoltar = findViewById(R.id.btnVoltarDetail);
        btnVoltar.setOnClickListener(v -> finish());

        if (currentPoint == null) {
            finish();
            return;
        }

        TextView txtName = findViewById(R.id.txtDetailName);
        TextView txtAddress = findViewById(R.id.txtDetailAddress);
        TextView txtDistance = findViewById(R.id.txtDetailDistance);
        TextView txtStatus = findViewById(R.id.txtDetailStatus);

        txtName.setText(currentPoint.getName());
        txtAddress.setText(currentPoint.getAddress());
        txtDistance.setText(currentPoint.getDistance() + " de você");
        txtStatus.setText(currentPoint.getOpenStatus());

        if (currentPoint.isOpen()) {
            txtStatus.setBackgroundResource(R.drawable.status_badge_open);
            txtStatus.setTextColor(getColor(R.color.status_success_text));
        } else {
            txtStatus.setBackgroundResource(R.drawable.status_badge_closed);
            txtStatus.setTextColor(getColor(R.color.status_danger_text));
        }

        RecyclerView recycler = findViewById(R.id.recyclerAcceptedItems);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new AcceptedItemAdapter(this, currentPoint.getAcceptedItems()));

        Button btnConfirmar = findViewById(R.id.btnConfirmarDescarte);
        btnConfirmar.setOnClickListener(v -> ConfirmarDescarteActivity.start(this, currentPoint));
    }
}
