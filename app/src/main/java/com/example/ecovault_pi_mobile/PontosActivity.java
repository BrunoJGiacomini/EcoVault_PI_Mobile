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
    private TextView txtQuantidadePontos;

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
        txtQuantidadePontos = findViewById(R.id.txtQtdPontos);

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
        atualizarTextoQuantidadePontos();

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
        atualizarTextoQuantidadePontos();

        if (adaptadorPontos != null) {
            adaptadorPontos.notifyDataSetChanged();
        }
    }

    private void atualizarTextoQuantidadePontos() {
        if (txtQuantidadePontos == null) {
            return;
        }

        int quantidade = listaPontos != null ? listaPontos.size() : 0;
        String texto;

        if (quantidade == 0) {
            texto = "Nenhum ponto encontrado · Indaiatuba, SP";
        } else if (quantidade == 1) {
            texto = "1 ponto encontrado · Indaiatuba, SP";
        } else {
            texto = quantidade + " pontos encontrados · Indaiatuba, SP";
        }

        txtQuantidadePontos.setText(texto);
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
                "1",
                "Ecoponto João Pioli",
                "Av. Artes e Ofícios - Conj. Hab. João Pioli, Indaiatuba - SP, 13348-635",
                "1.2km",
                "Aberto todos os dias, das 07:00 às 18:50",
                true,
                Arrays.asList(
                        new AcceptedItem("item_eletronicos", "Eletrônicos", 80, R.drawable.ic_cpu),
                        new AcceptedItem("item_pilhas", "Pilhas e Baterias", 30, R.drawable.ic_battery),
                        new AcceptedItem("item_entulho", "Entulho e Madeira", 50, R.drawable.ic_recycle),
                        new AcceptedItem("item_reciclaveis", "Recicláveis", 40, R.drawable.ic_leaf)
                ),
                -23.106166432447065, -47.24108297701529
        ));

        pontos.add(new CollectionPoint(
                "2",
                "Ecoponto Quintal - Jardim Eldorado",
                "R. Rev. Eliseu Narciso, 709 - Jardim Eldorado, Indaiatuba - SP, 13343-800",
                "2.5km",
                "Aberto todos os dias, das 07:00 às 19:00",
                true,
                Arrays.asList(
                        new AcceptedItem("item_vidro_metal", "Vidro e Metal", 40, R.drawable.ic_recycle),
                        new AcceptedItem("item_papel_plastico", "Papel e Plástico", 30, R.drawable.ic_leaf),
                        new AcceptedItem("item_oleo", "Óleo de Cozinha", 50, R.drawable.ic_plug)
                ),
                -23.107647828318886, -47.20820557697205
        ));

        pontos.add(new CollectionPoint(
                "3",
                "Ecoponto - Jardim Olinda",
                "R. Valdir Ferrari, 104 - Jardim Olinda, Indaiatuba - SP, 13335-560",
                "3.1km",
                "Aberto todos os dias, das 07:00 às 19:00",
                true,
                Arrays.asList(
                        new AcceptedItem("item_vidro_metal", "Vidro e Metal", 40, R.drawable.ic_recycle),
                        new AcceptedItem("item_papel_plastico", "Papel e Plástico", 30, R.drawable.ic_leaf)
                ),
                -23.076521462229195, -47.193580574832026
        ));

        pontos.add(new CollectionPoint(
                "4",
                "Ecoponto Jardim Nova Veneza",
                "R. José Vilalta, 51, Indaiatuba - SP, 13348-771",
                "3.8km",
                "Aberto todos os dias, das 07:00 às 19:00",
                true,
                Arrays.asList(
                        new AcceptedItem("item_vidro_metal", "Vidro e Metal", 40, R.drawable.ic_recycle),
                        new AcceptedItem("item_papel_plastico", "Papel e Plástico", 30, R.drawable.ic_leaf),
                        new AcceptedItem("item_pilhas_baterias", "Pilhas e Baterias", 30, R.drawable.ic_battery)
                ),
                -23.097133172393214, -47.25000277846388
        ));

        return pontos;
    }
}
