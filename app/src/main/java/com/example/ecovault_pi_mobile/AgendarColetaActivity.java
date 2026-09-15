package com.example.ecovault_pi_mobile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecovault_pi_mobile.database.UsuarioRepository;
import com.example.ecovault_pi_mobile.utils.SessaoUsuario;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AgendarColetaActivity extends AppCompatActivity {

    private final Map<String, Boolean> itensSelecionados = new LinkedHashMap<>();
    private LinearLayout containerItens;
    private TextView txtBonusPontos;
    private TextView txtBonusDesc;
    private TextView txtDataSelecionada;

    // pontos mock por item (só pra exibir o bônus, igual ao React)
    private final Map<String, Integer> pontosPorItem = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_coleta);

        ImageButton btnVoltar = findViewById(R.id.btnVoltarAgendar);
        btnVoltar.setOnClickListener(v -> finish());

        pontosPorItem.put("tv", 120);
        pontosPorItem.put("computador", 150);
        pontosPorItem.put("geladeira", 200);
        pontosPorItem.put("outro", 80);

        containerItens = findViewById(R.id.containerItensColeta);
        txtBonusPontos = findViewById(R.id.txtBonusPontos);
        txtBonusDesc = findViewById(R.id.txtBonusDesc);
        txtDataSelecionada = findViewById(R.id.txtDataSelecionada);

        itensSelecionados.put("tv", true); // "tv" já vem selecionado, igual ao React

        montarItens();
        atualizarBonus();
        setupDatePicker();
        setupSpinnerPeriodo();

        Button btnConfirmar = findViewById(R.id.btnConfirmarAgendamento);
        btnConfirmar.setOnClickListener(v -> {
            if (!SessaoUsuario.estaLogado(this)) {
                Toast.makeText(this, "Faça login para agendar uma coleta.", Toast.LENGTH_SHORT).show();
                return;
            }

            int usuarioId = SessaoUsuario.getUsuarioId(this);
            int totalPontos = calcularTotalPontos();

            UsuarioRepository repository = new UsuarioRepository(this);

            for (Map.Entry<String, Boolean> entry : itensSelecionados.entrySet()) {
                if (Boolean.TRUE.equals(entry.getValue())) {
                    Integer pontosItem = pontosPorItem.get(entry.getKey());
                    if (pontosItem != null) {
                        repository.registrarDescarte(usuarioId, entry.getKey(), "Coleta em Casa", pontosItem * 2);
                    }
                }
            }

            SucessoActivity.start(this, totalPontos, totalPontos, 65, false);
        });
    }

    private void montarItens() {
        String[][] itens = {
                {"tv", "TV / Monitor", "ic_monitor"},
                {"computador", "Computador", "ic_cpu"},
                {"geladeira", "Geladeira", "ic_monitor"},
                {"outro", "Outro", "ic_plus"}
        };

        for (int i = 0; i < itens.length; i += 2) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            View toggle1 = criarToggleItem(itens[i][0], itens[i][1], itens[i][2]);
            LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            p1.setMarginEnd(6);
            p1.bottomMargin = 10;
            row.addView(toggle1, p1);

            if (i + 1 < itens.length) {
                View toggle2 = criarToggleItem(itens[i + 1][0], itens[i + 1][1], itens[i + 1][2]);
                LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                p2.setMarginStart(6);
                p2.bottomMargin = 10;
                row.addView(toggle2, p2);
            }

            containerItens.addView(row);
        }
    }

    private View criarToggleItem(String id, String label, String iconName) {
        View toggleView = LayoutInflater.from(this).inflate(R.layout.item_toggle, containerItens, false);

        ImageView imgIcon = toggleView.findViewById(R.id.imgToggleIcon);
        TextView txtLabel = toggleView.findViewById(R.id.txtToggleLabel);
        ImageView imgCheck = toggleView.findViewById(R.id.imgToggleCheck);

        int iconRes = getResources().getIdentifier(iconName, "drawable", getPackageName());
        imgIcon.setImageResource(iconRes);
        txtLabel.setText(label);

        boolean selecionadoInicial = Boolean.TRUE.equals(itensSelecionados.get(id));
        aplicarEstiloToggle(toggleView, imgIcon, txtLabel, imgCheck, selecionadoInicial);

        toggleView.setOnClickListener(v -> {
            boolean novoEstado = !Boolean.TRUE.equals(itensSelecionados.get(id));
            itensSelecionados.put(id, novoEstado);
            aplicarEstiloToggle(toggleView, imgIcon, txtLabel, imgCheck, novoEstado);
            atualizarBonus();
        });

        return toggleView;
    }

    private void aplicarEstiloToggle(View toggleView, ImageView imgIcon, TextView txtLabel, ImageView imgCheck, boolean selecionado) {
        if (selecionado) {
            toggleView.setBackgroundResource(R.drawable.item_toggle_bg_selected);
            imgIcon.setColorFilter(getColor(R.color.primary));
            txtLabel.setTextColor(getColor(R.color.primary));
            imgCheck.setVisibility(View.VISIBLE);
        } else {
            toggleView.setBackgroundResource(R.drawable.item_toggle_bg_unselected);
            imgIcon.setColorFilter(getColor(R.color.muted_foreground));
            txtLabel.setTextColor(getColor(R.color.muted_foreground));
            imgCheck.setVisibility(View.GONE);
        }
    }

    private int calcularTotalPontos() {
        int total = 0;
        for (Map.Entry<String, Boolean> entry : itensSelecionados.entrySet()) {
            if (Boolean.TRUE.equals(entry.getValue())) {
                Integer pontos = pontosPorItem.get(entry.getKey());
                if (pontos != null) total += pontos * 2; // dobrado, coleta em casa
            }
        }
        return total;
    }

    private void atualizarBonus() {
        int total = calcularTotalPontos();
        txtBonusPontos.setText("+" + total + " pontos dobrados");

        StringBuilder desc = new StringBuilder();
        boolean primeiro = true;
        for (Map.Entry<String, Boolean> entry : itensSelecionados.entrySet()) {
            if (Boolean.TRUE.equals(entry.getValue())) {
                Integer pontos = pontosPorItem.get(entry.getKey());
                if (pontos != null) {
                    if (!primeiro) desc.append(" + ");
                    desc.append(entry.getKey()).append(" = ").append(pontos).append("pts × 2");
                    primeiro = false;
                }
            }
        }
        txtBonusDesc.setText(desc.length() > 0 ? desc.toString() + " por ser coleta em casa" : "Selecione um item");
    }

    private void setupDatePicker() {
        Calendar calendar = Calendar.getInstance();
        txtDataSelecionada.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                String dataFormatada = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                txtDataSelecionada.setText(dataFormatada);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        });
    }

    private void setupSpinnerPeriodo() {
        Spinner spinner = findViewById(R.id.spinnerPeriodo);
        List<String> periodos = new ArrayList<>();
        periodos.add("Manhã (8h–12h)");
        periodos.add("Tarde (13h–18h)");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, periodos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
