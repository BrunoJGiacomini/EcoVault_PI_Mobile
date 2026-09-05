package com.example.ecovault_pi_mobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SucessoActivity extends AppCompatActivity {

    private static final String EXTRA_POINTS_EARNED = "extra_points_earned";
    private static final String EXTRA_NEW_TOTAL = "extra_new_total";
    private static final String EXTRA_LEVEL_PROGRESS = "extra_level_progress";
    private static final String EXTRA_LEVEL_UP = "extra_level_up";

    public static void start(Context context, int pointsEarned, int newTotal,
                             int levelProgress, boolean levelUp) {
        Intent intent = new Intent(context, SucessoActivity.class);
        intent.putExtra(EXTRA_POINTS_EARNED, pointsEarned);
        intent.putExtra(EXTRA_NEW_TOTAL, newTotal);
        intent.putExtra(EXTRA_LEVEL_PROGRESS, levelProgress);
        intent.putExtra(EXTRA_LEVEL_UP, levelUp);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucesso);

        Intent intent = getIntent();
        int pointsEarned = intent.getIntExtra(EXTRA_POINTS_EARNED, 0);
        int newTotal = intent.getIntExtra(EXTRA_NEW_TOTAL, 0);
        int levelProgress = intent.getIntExtra(EXTRA_LEVEL_PROGRESS, 0);
        boolean levelUp = intent.getBooleanExtra(EXTRA_LEVEL_UP, false);

        TextView txtPointsEarned = findViewById(R.id.txtPointsEarned);
        TextView txtNewTotal = findViewById(R.id.txtNewTotal);
        TextView txtProgressoLabel = findViewById(R.id.txtProgressoLabel);
        TextView txtProgressoPercent = findViewById(R.id.txtProgressoPercent);
        ProgressBar progressBar = findViewById(R.id.progressNivelSucesso);

        txtPointsEarned.setText("+" + pointsEarned);
        txtNewTotal.setText(newTotal + " pontos no seu EcoSaldo");

        int progress = Math.max(0, Math.min(100, levelProgress));
        progressBar.setProgress(progress);
        txtProgressoPercent.setText(progress + "%");
        txtProgressoLabel.setText(levelUp ? "Novo nível desbloqueado!" : "Progresso no nível atual");

        Button btnVoltar = findViewById(R.id.btnVoltarInicio);
        btnVoltar.setOnClickListener(v -> {
            Intent home = new Intent(SucessoActivity.this, MainActivity.class);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(home);
            finish();
        });
    }
}
