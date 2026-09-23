package com.example.ecovault_pi_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecovault_pi_mobile.adapter.CategoryPillsAdapter;
import com.example.ecovault_pi_mobile.model.CategoryItem;
import com.example.ecovault_pi_mobile.utils.SessaoUsuario;
import com.example.ecovault_pi_mobile.utils.ThemeHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        configurarDataAtual();
        setupCategoryPills();
        setupGuideCards();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                return true; // já estamos na Home
            } else if (itemId == R.id.nav_pontos) {
                startActivity(new Intent(this, PontosActivity.class));
                return true;
            } else if (itemId == R.id.nav_agendar) {
                startActivity(new Intent(this, AgendarColetaActivity.class));
                return true;
            } else if (itemId == R.id.nav_guia) {
                startActivity(new Intent(this, GuiaDescarteActivity.class));
                return true;
            } else if (itemId == R.id.nav_perfil) {
                startActivity(new Intent(this, PerfilActivity.class));
                return true;
            }
            return false;
        });

        Button btnBuscarHome = findViewById(R.id.btnBuscar);
        btnBuscarHome.setOnClickListener(v -> startActivity(new Intent(this, PontosActivity.class)));

        View coletaBanner = findViewById(R.id.coletaBannerInclude);
        coletaBanner.setOnClickListener(v -> startActivity(new Intent(this, AgendarColetaActivity.class)));

        View btnEntrar = findViewById(R.id.btnEntrarHome);
        if (btnEntrar != null) {
            btnEntrar.setOnClickListener(v -> {
                AuthDialogFragment authDialog = new AuthDialogFragment();
                authDialog.setAuthListener(usuario -> {
                    SessaoUsuario.salvar(this, usuario.getId(), usuario.getNome(), usuario.getEmail());
                    atualizarSaudacao();
                });
                authDialog.show(getSupportFragmentManager(), "auth_dialog");
            });

            atualizarSaudacao();
        }

        ImageButton btnToggleTheme = findViewById(R.id.btnToggleTheme);
        btnToggleTheme.setOnClickListener(v -> {
            ThemeHelper.toggleTheme(this);
            recreate(); // reconstrói a Activity com o novo tema aplicado
        });

        TextView txtVerGuia = findViewById(R.id.txtVerGuia);
        txtVerGuia.setOnClickListener(v -> startActivity(new Intent(this, GuiaDescarteActivity.class)));
    }

    private void configurarDataAtual() {
        TextView txtData = findViewById(R.id.txtData);
        if (txtData != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMM", new Locale("pt", "BR"));
            String dataFormatada = sdf.format(new Date()).replace(".", "").toUpperCase(new Locale("pt", "BR"));
            txtData.setText(dataFormatada);
        }
    }

    private void setupCategoryPills() {
        List<CategoryItem> categories = new ArrayList<>();
        categories.add(new CategoryItem("pilhas", "Pilhas", "+30pts", R.drawable.ic_battery, true));
        categories.add(new CategoryItem("lampadas", "Lâmpadas", "+20pts", R.drawable.ic_lightbulb, false));
        categories.add(new CategoryItem("smartphones", "Smartphones", "+80pts", R.drawable.ic_smartphone, false));
        categories.add(new CategoryItem("cabos", "Cabos", "+15pts", R.drawable.ic_cable, false));
        categories.add(new CategoryItem("eletronicos", "Eletrônicos", "+120pts", R.drawable.ic_monitor, false));

        RecyclerView recycler = findViewById(R.id.recyclerCategoryPills);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recycler.setAdapter(new CategoryPillsAdapter(this, categories, item -> {
            startActivity(new Intent(this, PontosActivity.class));
        }));
    }

    private void setupGuideCards() {
        View card1 = findViewById(R.id.guideCard1);
        bindGuideCard(card1,
                R.drawable.icon_bg_red,
                R.drawable.ic_warning,
                R.color.destructive,
                getString(R.string.guide1_titulo),
                getString(R.string.guide1_desc));
        card1.setOnClickListener(v -> GuideDetailActivity.start(
                this,
                getString(R.string.guide1_titulo),
                getString(R.string.guide1_desc),
                getString(R.string.guide1_corpo),
                R.drawable.icon_bg_red,
                R.drawable.ic_warning,
                R.color.destructive));

        View card2 = findViewById(R.id.guideCard2);
        bindGuideCard(card2,
                R.drawable.icon_bg_amber,
                R.drawable.ic_smartphone,
                R.color.amber,
                getString(R.string.guide2_titulo),
                getString(R.string.guide2_desc));
        card2.setOnClickListener(v -> GuideDetailActivity.start(
                this,
                getString(R.string.guide2_titulo),
                getString(R.string.guide2_desc),
                getString(R.string.guide2_corpo),
                R.drawable.icon_bg_amber,
                R.drawable.ic_smartphone,
                R.color.amber));

        View card3 = findViewById(R.id.guideCard3);
        bindGuideCard(card3,
                R.drawable.icon_bg_mint,
                R.drawable.ic_shield,
                R.color.primary,
                getString(R.string.guide3_titulo),
                getString(R.string.guide3_desc));
        card3.setOnClickListener(v -> GuideDetailActivity.start(
                this,
                getString(R.string.guide3_titulo),
                getString(R.string.guide3_desc),
                getString(R.string.guide3_corpo),
                R.drawable.icon_bg_mint,
                R.drawable.ic_shield,
                R.color.primary));
    }

    private void bindGuideCard(View cardRoot, int bgRes, int iconRes, int tintColorRes, String title, String desc) {
        FrameLayout frameIconBg = cardRoot.findViewById(R.id.frameIconBg);
        ImageView imgIcon = cardRoot.findViewById(R.id.imgGuideIcon);
        TextView txtTitle = cardRoot.findViewById(R.id.txtGuideTitle);
        TextView txtDesc = cardRoot.findViewById(R.id.txtGuideDesc);

        frameIconBg.setBackgroundResource(bgRes);
        imgIcon.setImageResource(iconRes);
        imgIcon.setColorFilter(getColor(tintColorRes));
        txtTitle.setText(title);
        txtDesc.setText(desc);
    }

    private void atualizarSaudacao() {
        TextView txtSaudacao = findViewById(R.id.txtSaudacao);
        View btnEntrar = findViewById(R.id.btnEntrarHome);

        if (SessaoUsuario.estaLogado(this)) {
            String nome = SessaoUsuario.getNome(this);
            txtSaudacao.setText("Olá, " + nome + " 👋");
            btnEntrar.setVisibility(View.GONE);
        } else {
            txtSaudacao.setText("Olá, usuário 👋");
            btnEntrar.setVisibility(View.VISIBLE);
        }
    }
}
