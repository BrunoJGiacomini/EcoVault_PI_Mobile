package com.example.ecovault_pi_mobile;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecovault_pi_mobile.adapter.AcceptedItemAdapter;
import com.example.ecovault_pi_mobile.model.CollectionPoint;
import com.example.ecovault_pi_mobile.utils.LocalizacaoHelper;

public class PontoDetailActivity extends AppCompatActivity {

    private static CollectionPoint pontoAtual;
    private static CollectionPoint currentPoint;

    private LocalizacaoHelper localizacaoHelper;
    private TextView txtDistanciaPonto;

    public static void start(Context context, CollectionPoint ponto) {
        currentPoint = ponto;
        pontoAtual = ponto;
        context.startActivity(new Intent(context, PontoDetailActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ponto_detail);

        localizacaoHelper = new LocalizacaoHelper(this);

        ImageButton btnVoltar = findViewById(R.id.btnVoltarDetail);
        btnVoltar.setOnClickListener(v -> finish());

        if (pontoAtual == null && currentPoint != null) {
            pontoAtual = currentPoint;
        }

        if (pontoAtual == null) {
            finish();
            return;
        }

        TextView txtNomePonto = findViewById(R.id.txtDetailName);
        TextView txtEnderecoPonto = findViewById(R.id.txtDetailAddress);
        txtDistanciaPonto = findViewById(R.id.txtDetailDistance);
        TextView txtStatusPonto = findViewById(R.id.txtDetailStatus);

        txtNomePonto.setText(pontoAtual.getName());
        txtEnderecoPonto.setText(pontoAtual.getAddress());

        configurarStatusPonto(txtStatusPonto);
        configurarDistanciaInicial();
        carregarLocalizacaoECalcularDistancia();

        RecyclerView recycler = findViewById(R.id.recyclerAcceptedItems);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        if (pontoAtual.getAcceptedItems() != null) {
            recycler.setAdapter(new AcceptedItemAdapter(this, pontoAtual.getAcceptedItems()));
        }

        Button btnConfirmar = findViewById(R.id.btnConfirmarDescarte);
        btnConfirmar.setOnClickListener(v -> ConfirmarDescarteActivity.start(this, pontoAtual));
    }

    private void configurarStatusPonto(TextView txtStatus) {
        if (pontoAtual.isOpen()) {
            txtStatus.setText(pontoAtual.getOpenStatus() != null ? pontoAtual.getOpenStatus() : "Aberto agora");
            txtStatus.setBackgroundResource(R.drawable.status_badge_open);
            txtStatus.setTextColor(getColor(R.color.status_success_text));
        } else {
            txtStatus.setText(pontoAtual.getOpenStatus() != null ? pontoAtual.getOpenStatus() : "Fechado");
            txtStatus.setBackgroundResource(R.drawable.status_badge_closed);
            txtStatus.setTextColor(getColor(R.color.status_danger_text));
        }
    }

    private void configurarDistanciaInicial() {
        if (pontoAtual.getDistanciaExibida() != null && !pontoAtual.getDistanciaExibida().isEmpty()) {
            txtDistanciaPonto.setText(pontoAtual.getDistanciaExibida() + " de você");
        } else {
            txtDistanciaPonto.setText("Distância indisponível");
        }
    }

    private void carregarLocalizacaoECalcularDistancia() {
        if (localizacaoHelper.temPermissaoLocalizacao()) {
            localizacaoHelper.obterUltimaLocalizacao(new LocalizacaoHelper.OnLocalizacaoResult() {
                @Override
                public void aoEncontrarLocalizacao(Location localizacaoUsuario) {
                    atualizarDistanciaPonto(localizacaoUsuario);
                }

                @Override
                public void aoFalharLocalizacao() {
                    tratarLocalizacaoIndisponivel();
                }
            });
        } else {
            tratarLocalizacaoIndisponivel();
        }
    }

    private void atualizarDistanciaPonto(Location localizacaoUsuario) {
        if (localizacaoUsuario == null || pontoAtual == null) {
            tratarLocalizacaoIndisponivel();
            return;
        }

        float distanciaEmMetros = LocalizacaoHelper.calcularDistancia(
                localizacaoUsuario.getLatitude(),
                localizacaoUsuario.getLongitude(),
                pontoAtual.getLatitude(),
                pontoAtual.getLongitude()
        );

        String distanciaFormatada = LocalizacaoHelper.formatarDistancia(distanciaEmMetros);
        pontoAtual.setDistanciaEmMetros(distanciaEmMetros);
        pontoAtual.setDistanciaExibida(distanciaFormatada);

        if (txtDistanciaPonto != null) {
            txtDistanciaPonto.setText(distanciaFormatada + " de você");
        }
    }

    private void tratarLocalizacaoIndisponivel() {
        if (txtDistanciaPonto != null) {
            if (pontoAtual != null && pontoAtual.getDistanciaExibida() != null && !pontoAtual.getDistanciaExibida().isEmpty()) {
                txtDistanciaPonto.setText(pontoAtual.getDistanciaExibida() + " de você");
            } else {
                txtDistanciaPonto.setText("Distância indisponível");
            }
        }
    }
}
