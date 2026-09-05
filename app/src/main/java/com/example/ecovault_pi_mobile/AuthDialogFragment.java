package com.example.ecovault_pi_mobile;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AuthDialogFragment extends DialogFragment {

    public interface AuthListener {
        void onAuthSuccess(String nome, String email);
    }

    private AuthListener listener;

    public void setAuthListener(AuthListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_auth);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x99000000));
        }

        setupViews(dialog);

        return dialog;
    }

    private void setupViews(Dialog dialog) {
        TextView tabEntrar = dialog.findViewById(R.id.tabEntrar);
        TextView tabCadastrar = dialog.findViewById(R.id.tabCadastrar);
        LinearLayout formLogin = dialog.findViewById(R.id.formLogin);
        LinearLayout formCadastro = dialog.findViewById(R.id.formCadastro);
        TextView txtTitulo = dialog.findViewById(R.id.txtDialogTitulo);
        ImageButton btnFechar = dialog.findViewById(R.id.btnFecharDialog);

        btnFechar.setOnClickListener(v -> dismiss());

        tabEntrar.setOnClickListener(v -> {
            tabEntrar.setBackgroundResource(R.drawable.tab_active_bg);
            tabEntrar.setTextColor(getResources().getColor(R.color.primary, null));
            tabCadastrar.setBackground(null);
            tabCadastrar.setTextColor(getResources().getColor(R.color.muted_foreground, null));
            formLogin.setVisibility(View.VISIBLE);
            formCadastro.setVisibility(View.GONE);
            txtTitulo.setText("Bem-vindo de volta");
        });

        tabCadastrar.setOnClickListener(v -> {
            tabCadastrar.setBackgroundResource(R.drawable.tab_active_bg);
            tabCadastrar.setTextColor(getResources().getColor(R.color.primary, null));
            tabEntrar.setBackground(null);
            tabEntrar.setTextColor(getResources().getColor(R.color.muted_foreground, null));
            formCadastro.setVisibility(View.VISIBLE);
            formLogin.setVisibility(View.GONE);
            txtTitulo.setText("Crie sua conta");
        });

        // LOGIN
        EditText edtLoginEmail = dialog.findViewById(R.id.edtLoginEmail);
        EditText edtLoginSenha = dialog.findViewById(R.id.edtLoginSenha);
        TextView txtErroLogin = dialog.findViewById(R.id.txtErroLogin);
        Button btnLogin = dialog.findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String email = edtLoginEmail.getText().toString().trim();
            String senha = edtLoginSenha.getText().toString().trim();

            if (email.isEmpty() || senha.isEmpty()) {
                txtErroLogin.setText("Preencha todos os campos.");
                txtErroLogin.setVisibility(View.VISIBLE);
                return;
            }

            // Aqui depois entra a chamada de API real (Retrofit) pra /auth/login
            if (listener != null) listener.onAuthSuccess(email, email);
            dismiss();
        });

        // CADASTRO
        EditText edtCadastroNome = dialog.findViewById(R.id.edtCadastroNome);
        EditText edtCadastroEmail = dialog.findViewById(R.id.edtCadastroEmail);
        EditText edtCadastroSenha = dialog.findViewById(R.id.edtCadastroSenha);
        TextView txtErroCadastro = dialog.findViewById(R.id.txtErroCadastro);
        Button btnCadastrar = dialog.findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(v -> {
            String nome = edtCadastroNome.getText().toString().trim();
            String email = edtCadastroEmail.getText().toString().trim();
            String senha = edtCadastroSenha.getText().toString().trim();

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                txtErroCadastro.setText("Preencha todos os campos.");
                txtErroCadastro.setVisibility(View.VISIBLE);
                return;
            }

            if (senha.length() < 6) {
                txtErroCadastro.setText("A senha deve ter no mínimo 6 caracteres.");
                txtErroCadastro.setVisibility(View.VISIBLE);
                return;
            }

            // Aqui depois entra a chamada de API real (Retrofit) pra /auth/register
            if (listener != null) listener.onAuthSuccess(nome, email);
            dismiss();
        });
    }
}
