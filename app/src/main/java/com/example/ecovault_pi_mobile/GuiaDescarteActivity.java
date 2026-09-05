package com.example.ecovault_pi_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecovault_pi_mobile.adapter.GuideCategoryAdapter;
import com.example.ecovault_pi_mobile.model.GuideCategoryItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiaDescarteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_descarte);

        ImageButton btnVoltar = findViewById(R.id.btnVoltarGuia);
        btnVoltar.setOnClickListener(v -> finish());

        setupCategorias();
        setupDicas();
        setupPassos();

        Button btnEncontrarPontos = findViewById(R.id.btnEncontrarPontos);
        btnEncontrarPontos.setOnClickListener(v -> startActivity(new Intent(this, PontosActivity.class)));
    }

    private void setupCategorias() {
        List<GuideCategoryItem> categorias = new ArrayList<>();

        categorias.add(new GuideCategoryItem(
                R.drawable.ic_battery, R.color.amber, R.drawable.icon_bg_amber,
                getString(R.string.cat1_titulo), getString(R.string.cat1_desc),
                Arrays.asList("Pilhas AA/AAA", "Baterias 9V", "Baterias de lítio", "Baterias de celular", "Baterias de notebook")));

        categorias.add(new GuideCategoryItem(
                R.drawable.ic_lightbulb, R.color.purple, R.drawable.icon_bg_purple,
                getString(R.string.cat2_titulo), getString(R.string.cat2_desc),
                Arrays.asList("Fluorescentes", "LED", "Incandescentes", "Halógenas", "Vapor de sódio")));

        categorias.add(new GuideCategoryItem(
                R.drawable.ic_smartphone, R.color.primary, R.drawable.icon_bg_mint,
                getString(R.string.cat3_titulo), getString(R.string.cat3_desc),
                Arrays.asList("Celulares", "Tablets", "Smartwatches", "Fones bluetooth", "Carregadores")));

        categorias.add(new GuideCategoryItem(
                R.drawable.ic_monitor, R.color.eco_blue, R.drawable.icon_bg_blue,
                getString(R.string.cat4_titulo), getString(R.string.cat4_desc),
                Arrays.asList("TVs CRT", "TVs LCD/LED", "Monitores", "Projetores")));

        categorias.add(new GuideCategoryItem(
                R.drawable.ic_cpu, R.color.primary, R.drawable.icon_bg_mint,
                getString(R.string.cat5_titulo), getString(R.string.cat5_desc),
                Arrays.asList("Desktops", "Notebooks", "Teclados", "Mouses", "Discos rígidos", "Pen drives")));

        categorias.add(new GuideCategoryItem(
                R.drawable.ic_printer, R.color.muted_foreground, R.drawable.icon_bg_amber,
                getString(R.string.cat6_titulo), getString(R.string.cat6_desc),
                Arrays.asList("Impressoras", "Cartuchos", "Toners", "Scanners")));

        RecyclerView recycler = findViewById(R.id.recyclerCategorias);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new GuideCategoryAdapter(this, categorias));
    }

    private void setupDicas() {
        bindDica(R.id.dicaCard1, R.drawable.icon_bg_red, R.drawable.ic_warning, R.color.destructive,
                getString(R.string.guide1_titulo), getString(R.string.guide1_desc),
                getString(R.string.guide1_corpo));

        bindDica(R.id.dicaCard2, R.drawable.icon_bg_amber, R.drawable.ic_smartphone, R.color.amber,
                getString(R.string.guide2_titulo), getString(R.string.guide2_desc),
                getString(R.string.guide2_corpo));

        bindDica(R.id.dicaCard3, R.drawable.icon_bg_mint, R.drawable.ic_shield, R.color.primary,
                getString(R.string.guide3_titulo), getString(R.string.guide3_desc),
                getString(R.string.guide3_corpo));

        bindDica(R.id.dicaCard4, R.drawable.icon_bg_red, R.drawable.ic_thermometer, R.color.destructive,
                getString(R.string.dica4_titulo), getString(R.string.dica4_desc),
                getString(R.string.dica4_corpo));

        bindDica(R.id.dicaCard5, R.drawable.icon_bg_blue, R.drawable.ic_cable, R.color.eco_blue,
                getString(R.string.dica5_titulo), getString(R.string.dica5_desc),
                getString(R.string.dica5_corpo));

        bindDica(R.id.dicaCard6, R.drawable.icon_bg_mint, R.drawable.ic_leaf, R.color.primary,
                getString(R.string.dica6_titulo), getString(R.string.dica6_desc),
                getString(R.string.dica6_corpo));
    }

    private void bindDica(int cardId, int bgRes, int iconRes, int tintColorRes,
                          String title, String desc, String corpo) {
        View cardRoot = findViewById(cardId);

        FrameLayout frameIconBg = cardRoot.findViewById(R.id.frameIconBg);
        ImageView imgIcon = cardRoot.findViewById(R.id.imgGuideIcon);
        TextView txtTitle = cardRoot.findViewById(R.id.txtGuideTitle);
        TextView txtDesc = cardRoot.findViewById(R.id.txtGuideDesc);

        frameIconBg.setBackgroundResource(bgRes);
        imgIcon.setImageResource(iconRes);
        imgIcon.setColorFilter(getColor(tintColorRes));
        txtTitle.setText(title);
        txtDesc.setText(desc);

        cardRoot.setOnClickListener(v -> GuideDetailActivity.start(
                this, title, desc, corpo, bgRes, iconRes, tintColorRes));
    }

    private void setupPassos() {
        bindPasso(R.id.stepCard1, "1", getString(R.string.passo1_titulo), getString(R.string.passo1_desc));
        bindPasso(R.id.stepCard2, "2", getString(R.string.passo2_titulo), getString(R.string.passo2_desc));
        bindPasso(R.id.stepCard3, "3", getString(R.string.passo3_titulo), getString(R.string.passo3_desc));
    }

    private void bindPasso(int cardId, String numero, String titulo, String desc) {
        View cardRoot = findViewById(cardId);
        TextView txtNumber = cardRoot.findViewById(R.id.txtStepNumber);
        TextView txtTitle = cardRoot.findViewById(R.id.txtStepTitle);
        TextView txtDesc = cardRoot.findViewById(R.id.txtStepDesc);

        txtNumber.setText(numero);
        txtTitle.setText(titulo);
        txtDesc.setText(desc);
    }
}
