package com.example.ecovault_pi_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.example.ecovault_pi_mobile.adapter.CategoryPillsAdapter;
import com.example.ecovault_pi_mobile.model.CategoryItem;

import java.util.ArrayList;
import java.util.List;

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

        setupCategoryPills();
        setupGuideCards();

        TextView txtVerGuia = findViewById(R.id.txtVerGuia);
        txtVerGuia.setOnClickListener(v -> startActivity(new Intent(this, GuiaDescarteActivity.class)));
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
            // navegar pra tela de Pontos
            // startActivity(new Intent(this, PontosActivity.class));
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
}