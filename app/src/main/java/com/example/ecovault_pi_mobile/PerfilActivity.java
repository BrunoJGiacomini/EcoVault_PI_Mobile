package com.example.ecovault_pi_mobile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecovault_pi_mobile.adapter.BadgeAdapter;
import com.example.ecovault_pi_mobile.model.Badge;

import java.util.ArrayList;
import java.util.List;

public class PerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        ImageButton btnVoltar = findViewById(R.id.btnVoltarPerfil);
        btnVoltar.setOnClickListener(v -> finish());

        carregarUsuario();
        setupBadges();
    }

    private void carregarUsuario() {
        // MOCK - depois substituir por dados reais (SharedPreferences/API/Room)
        String nomeUsuario = "Maria Ramos";

        TextView txtNome = findViewById(R.id.txtNomeUsuario);
        TextView txtAvatar = findViewById(R.id.txtAvatarIniciais);

        txtNome.setText(nomeUsuario);
        txtAvatar.setText(gerarIniciais(nomeUsuario));
    }

    private String gerarIniciais(String nomeCompleto) {
        if (nomeCompleto == null || nomeCompleto.trim().isEmpty()) return "??";
        String[] partes = nomeCompleto.trim().split("\\s+");
        StringBuilder iniciais = new StringBuilder();
        for (String parte : partes) {
            if (!parte.isEmpty()) iniciais.append(Character.toUpperCase(parte.charAt(0)));
        }
        return iniciais.length() > 2 ? iniciais.substring(0, 2) : iniciais.toString();
    }

    private void setupBadges() {
        List<Badge> badges = new ArrayList<>();
        badges.add(new Badge("⚡", "Mestre das Pilhas", true));
        badges.add(new Badge("🌱", "Primeiro Passo", true));
        badges.add(new Badge("🔥", "3 em Sequência", true));
        badges.add(new Badge("📱", "Guardião Tech", false));
        badges.add(new Badge("🏆", "Eco Lendário", false));

        RecyclerView recycler = findViewById(R.id.recyclerBadges);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recycler.setAdapter(new BadgeAdapter(this, badges));
    }
}
