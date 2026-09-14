package com.example.ecovault_pi_mobile.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessaoUsuario {

    private static final String PREFS_NAME = "ecovault_sessao";
    private static final String KEY_USUARIO_ID = "usuario_id";
    private static final String KEY_USUARIO_NOME = "usuario_nome";
    private static final String KEY_USUARIO_EMAIL = "usuario_email";

    public static void salvar(Context context, int id, String nome, String email) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .putInt(KEY_USUARIO_ID, id)
                .putString(KEY_USUARIO_NOME, nome)
                .putString(KEY_USUARIO_EMAIL, email)
                .apply();
    }

    public static int getUsuarioId(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getInt(KEY_USUARIO_ID, -1);
    }

    public static String getNome(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getString(KEY_USUARIO_NOME, null);
    }

    public static boolean estaLogado(Context context) {
        return getUsuarioId(context) != -1;
    }

    public static void limpar(Context context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().clear().apply();
    }
}
