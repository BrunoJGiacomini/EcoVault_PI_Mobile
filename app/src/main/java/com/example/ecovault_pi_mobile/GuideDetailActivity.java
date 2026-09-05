package com.example.ecovault_pi_mobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GuideDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TITULO = "extra_titulo";
    public static final String EXTRA_SUBTITULO = "extra_subtitulo";
    public static final String EXTRA_CORPO = "extra_corpo";
    public static final String EXTRA_ICON_BG = "extra_icon_bg";
    public static final String EXTRA_ICON_RES = "extra_icon_res";
    public static final String EXTRA_ICON_TINT = "extra_icon_tint";

    public static void start(Context context, String titulo, String subtitulo, String corpo,
                             int iconBgRes, int iconRes, int iconTintColorRes) {
        Intent intent = new Intent(context, GuideDetailActivity.class);
        intent.putExtra(EXTRA_TITULO, titulo);
        intent.putExtra(EXTRA_SUBTITULO, subtitulo);
        intent.putExtra(EXTRA_CORPO, corpo);
        intent.putExtra(EXTRA_ICON_BG, iconBgRes);
        intent.putExtra(EXTRA_ICON_RES, iconRes);
        intent.putExtra(EXTRA_ICON_TINT, iconTintColorRes);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_detail);

        ImageButton btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(v -> finish());

        TextView txtTitulo = findViewById(R.id.txtDetailTitulo);
        TextView txtSubtitulo = findViewById(R.id.txtDetailSubtitulo);
        TextView txtCorpo = findViewById(R.id.txtDetailCorpo);
        FrameLayout frameIconBg = findViewById(R.id.frameIconGrande);
        ImageView imgIcon = findViewById(R.id.imgIconGrande);

        Intent intent = getIntent();
        txtTitulo.setText(intent.getStringExtra(EXTRA_TITULO));
        txtSubtitulo.setText(intent.getStringExtra(EXTRA_SUBTITULO));
        txtCorpo.setText(intent.getStringExtra(EXTRA_CORPO));

        int iconBgRes = intent.getIntExtra(EXTRA_ICON_BG, R.drawable.icon_bg_red);
        int iconRes = intent.getIntExtra(EXTRA_ICON_RES, R.drawable.ic_warning);
        int iconTint = intent.getIntExtra(EXTRA_ICON_TINT, R.color.destructive);

        frameIconBg.setBackgroundResource(iconBgRes);
        imgIcon.setImageResource(iconRes);
        imgIcon.setColorFilter(getColor(iconTint));
    }
}
