package com.example.ecovault_pi_mobile;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecovault_pi_mobile.adapter.PointCardAdapter;
import com.example.ecovault_pi_mobile.model.AcceptedItem;
import com.example.ecovault_pi_mobile.model.CollectionPoint;
import com.example.ecovault_pi_mobile.utils.LocalizacaoHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PontosActivity extends AppCompatActivity {

    private LocalizacaoHelper localizacaoHelper;
    private LinearLayout bannerAviso;
    private TextView txtMensagemAviso;

    private List<CollectionPoint> listaPontos = new ArrayList<>();
    private PointCardAdapter adaptadorPontos;

    private final ActivityResultLauncher<String[]> solicitadorPermissao =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                boolean concedida = false;
                for (Map.Entry<String, Boolean> entry : result.entrySet()) {
                    if (entry.getValue()) {
                        concedida = true;
                        break;
                    }
                }

                if (concedida) {
                    iniciarBuscaLocalizacao();
                } else {
                    mostrarAvisoLocalizacao("Ative a localização para ver a distância");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pontos);

        localizacaoHelper = new LocalizacaoHelper(this);
        bannerAviso = findViewById(R.id.bannerAvisoLocalizacao);
        txtMensagemAviso = findViewById(R.id.txtMensagemAviso);

        ImageButton btnVoltar = findViewById(R.id.btnVoltarPontos);
        btnVoltar.setOnClickListener(v -> finish());

        configurarListaPontos();
        verificarPermissoesECarregar();
    }

    private void verificarPermissoesECarregar() {
        if (localizacaoHelper.temPermissaoLocalizacao()) {
            iniciarBuscaLocalizacao();
        } else {
            solicitadorPermissao.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    private void iniciarBuscaLocalizacao() {
        localizacaoHelper.obterUltimaLocalizacao(new LocalizacaoHelper.OnLocalizacaoResult() {
            @Override
            public void aoEncontrarLocalizacao(Location localizacao) {
                processarLocalizacaoEOrdenarPontos(localizacao);
                ocultarAvisoLocalizacao();
            }

            @Override
            public void aoFalharLocalizacao() {
                mostrarAvisoLocalizacao("Não foi possível obter sua localização agora");
            }
        });
    }

    private void mostrarAvisoLocalizacao(String mensagem) {
        if (bannerAviso != null && txtMensagemAviso != null) {
            txtMensagemAviso.setText(mensagem);
            bannerAviso.setVisibility(View.VISIBLE);
        }
    }

    private void ocultarAvisoLocalizacao() {
        if (bannerAviso != null) {
            bannerAviso.setVisibility(View.GONE);
        }
    }

    private void configurarListaPontos() {
        listaPontos = obterPontosMock();

        RecyclerView recycler = findViewById(R.id.recyclerPontos);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adaptadorPontos = new PointCardAdapter(this, listaPontos, ponto -> {
            PontoDetailActivity.start(this, ponto);
        });
        recycler.setAdapter(adaptadorPontos);
    }

    private void processarLocalizacaoEOrdenarPontos(Location localizacaoUsuario) {
        if (localizacaoUsuario == null || listaPontos == null || listaPontos.isEmpty()) {
            return;
        }

        double latitudeUsuario = localizacaoUsuario.getLatitude();
        double longitudeUsuario = localizacaoUsuario.getLongitude();

        for (CollectionPoint ponto : listaPontos) {
            float distanciaEmMetros = LocalizacaoHelper.calcularDistancia(
                    latitudeUsuario,
                    longitudeUsuario,
                    ponto.getLatitude(),
                    ponto.getLongitude()
            );

            String distanciaFormatada = LocalizacaoHelper.formatarDistancia(distanciaEmMetros);

            ponto.setDistanciaEmMetros(distanciaEmMetros);
            ponto.setDistanciaExibida(distanciaFormatada);
        }

        ordenarPontosPorDistancia();

        if (adaptadorPontos != null) {
            adaptadorPontos.notifyDataSetChanged();
        }
    }

    private void ordenarPontosPorDistancia() {
        if (listaPontos != null) {
            Collections.sort(listaPontos, (ponto1, ponto2) ->
                    Float.compare(ponto1.getDistanciaEmMetros(), ponto2.getDistanciaEmMetros()));
        }
    }

    private List<CollectionPoint> obterPontosMock() {
        List<CollectionPoint> pontos = new ArrayList<>();

        pontos.add(new CollectionPoint(
                "1", "EcoPonto Centro", "Rua das Flores, 123 - Centro",
                "1.2km", "Aberto agora", true,
                Arrays.asList(
                        new AcceptedItem("item_celular", "Celular", 80, R.drawable.ic_smartphone),
                        new AcceptedItem("item_pilhas", "Pilhas", 30, R.drawable.ic_battery)
                ), -23.0911, -47.2185));

        pontos.add(new CollectionPoint(
                "2", "Recicla Fácil", "Av. Brasil, 456 - Jardim América",
                "2.5km", "Fechado", false,
                Arrays.asList(
                        new AcceptedItem("item_notebook", "Notebook", 120, R.drawable.ic_cpu),
                        new AcceptedItem("item_lampadas", "Lâmpadas", 20, R.drawable.ic_lightbulb)
                ), -23.0850, -47.2010));

        pontos.add(new CollectionPoint(
                "3", "Descarte Verde Vila Vitória", "Rua das Palmeiras, 789 - Vila Vitória",
                "3.8km", "Aberto agora", true,
                Arrays.asList(
                        new AcceptedItem("item_impressora", "Impressora", 100, R.drawable.ic_printer),
                        new AcceptedItem("item_tablet", "Tablet", 90, R.drawable.ic_tablet)
                ), -23.1025, -47.2250));

        return pontos;
    }
}
