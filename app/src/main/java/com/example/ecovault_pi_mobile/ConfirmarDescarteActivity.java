package com.example.ecovault_pi_mobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecovault_pi_mobile.database.UsuarioRepository;
import com.example.ecovault_pi_mobile.model.AcceptedItem;
import com.example.ecovault_pi_mobile.model.CollectionPoint;
import com.example.ecovault_pi_mobile.utils.SessaoUsuario;

import java.util.ArrayList;
import java.util.List;

public class ConfirmarDescarteActivity extends AppCompatActivity {

    private static CollectionPoint currentPoint;
    private final List<AcceptedItem> selecionados = new ArrayList<>();

    private LinearLayout containerToggles;
    private LinearLayout containerSelecionados;
    private TextView txtTotalPoints;
    private TextView txtEquivalenteReais;
    private TextView txtErrorMsg;

    public static void start(Context context, CollectionPoint point) {
        currentPoint = point;
        context.startActivity(new Intent(context, ConfirmarDescarteActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_descarte);

        ImageButton btnVoltar = findViewById(R.id.btnVoltarConfirmar);
        btnVoltar.setOnClickListener(v -> finish());

        if (currentPoint == null) {
            finish();
            return;
        }

        containerToggles = findViewById(R.id.containerToggles);
        containerSelecionados = findViewById(R.id.containerSelecionados);
        txtTotalPoints = findViewById(R.id.txtTotalPoints);
        txtEquivalenteReais = findViewById(R.id.txtEquivalenteReais);
        txtErrorMsg = findViewById(R.id.txtErrorMsg);

        montarToggles();

        Button btnRegistrar = findViewById(R.id.btnRegistrarDescarte);
        btnRegistrar.setOnClickListener(v -> confirmarDescarte());
    }

    private void montarToggles() {
        List<AcceptedItem> itens = currentPoint.getAcceptedItems();

        // Monta em pares (2 colunas), igual ao grid-cols-2 do React
        for (int i = 0; i < itens.size(); i += 2) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            AcceptedItem item1 = itens.get(i);
            View toggle1 = criarToggle(item1);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            params1.setMarginEnd(6);
            params1.bottomMargin = 10;
            row.addView(toggle1, params1);

            if (i + 1 < itens.size()) {
                AcceptedItem item2 = itens.get(i + 1);
                View toggle2 = criarToggle(item2);
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                params2.setMarginStart(6);
                params2.bottomMargin = 10;
                row.addView(toggle2, params2);
            }

            containerToggles.addView(row);
        }
    }

    private View criarToggle(AcceptedItem item) {
        View toggleView = LayoutInflater.from(this).inflate(R.layout.item_toggle, containerToggles, false);

        ImageView imgIcon = toggleView.findViewById(R.id.imgToggleIcon);
        TextView txtLabel = toggleView.findViewById(R.id.txtToggleLabel);
        ImageView imgCheck = toggleView.findViewById(R.id.imgToggleCheck);

        imgIcon.setImageResource(item.getIconRes());
        txtLabel.setText(item.getLabel());

        toggleView.setTag(item);

        toggleView.setOnClickListener(v -> {
            boolean isSelected = selecionados.contains(item);
            if (isSelected) {
                selecionados.remove(item);
                toggleView.setBackgroundResource(R.drawable.item_toggle_bg_unselected);
                imgIcon.setColorFilter(getColor(R.color.muted_foreground));
                txtLabel.setTextColor(getColor(R.color.muted_foreground));
                imgCheck.setVisibility(View.GONE);
            } else {
                selecionados.add(item);
                toggleView.setBackgroundResource(R.drawable.item_toggle_bg_selected);
                imgIcon.setColorFilter(getColor(R.color.primary));
                txtLabel.setTextColor(getColor(R.color.primary));
                imgCheck.setVisibility(View.VISIBLE);
            }
            atualizarResumo();
        });

        return toggleView;
    }

    private void atualizarResumo() {
        containerSelecionados.removeAllViews();
        int total = 0;

        for (AcceptedItem item : selecionados) {
            total += item.getPointsPerDisposal();

            View row = LayoutInflater.from(this).inflate(R.layout.item_selected_disposal, containerSelecionados, false);
            ImageView imgIcon = row.findViewById(R.id.imgSelIcon);
            TextView txtLabel = row.findViewById(R.id.txtSelLabel);
            TextView txtPoints = row.findViewById(R.id.txtSelPoints);

            imgIcon.setImageResource(item.getIconRes());
            txtLabel.setText(item.getLabel());
            txtPoints.setText("+" + item.getPointsPerDisposal() + " pts");

            containerSelecionados.addView(row);
        }

        txtTotalPoints.setText("+" + total);
        double reais = total / 100.0;
        txtEquivalenteReais.setText(String.format("≈ R$ %.2f em parceiros", reais).replace(".", ","));

        txtErrorMsg.setVisibility(View.GONE);
    }

    private void confirmarDescarte() {
        if (selecionados.isEmpty()) {
            txtErrorMsg.setText("Selecione ao menos um item para descartar.");
            txtErrorMsg.setVisibility(View.VISIBLE);
            return;
        }

        if (!SessaoUsuario.estaLogado(this)) {
            txtErrorMsg.setText("Cadastre-se ou faça login para registrar o descarte.");
            txtErrorMsg.setVisibility(View.VISIBLE);
            return;
        }

        int usuarioId = SessaoUsuario.getUsuarioId(this);
        int totalPoints = 0;

        UsuarioRepository repository = new UsuarioRepository(this);

        for (AcceptedItem item : selecionados) {
            totalPoints += item.getPointsPerDisposal();
            repository.registrarDescarte(usuarioId, item.getLabel(), currentPoint.getName(), item.getPointsPerDisposal());
        }

        SucessoActivity.start(this, totalPoints, totalPoints, 65, false);
    }
}
