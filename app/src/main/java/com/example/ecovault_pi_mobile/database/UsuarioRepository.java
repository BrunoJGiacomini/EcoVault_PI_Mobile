package com.example.ecovault_pi_mobile.database;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.ecovault_pi_mobile.database.DAO.DescarteDao;
import com.example.ecovault_pi_mobile.database.DAO.UsuarioDao;
import com.example.ecovault_pi_mobile.database.entity.DescarteEntity;
import com.example.ecovault_pi_mobile.database.entity.UsuarioEntity;


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UsuarioRepository {

    public interface AuthCallback {
        void onSucesso(UsuarioEntity usuario);
        void onErro(String mensagem);
    }

    public interface HistoricoCallback {
        void onResultado(List<DescarteEntity> descartes);
    }

    private final UsuarioDao usuarioDao;
    private final DescarteDao descarteDao;
    private final ExecutorService executor;
    private final Handler mainHandler;

    public UsuarioRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.usuarioDao = db.usuarioDao();
        this.descarteDao = db.descarteDao();
        this.executor = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }



    public void registrarDescarte(int usuarioId, String itemLabel, String pontoColetaNome, int pontos) {
        executor.execute(() -> {
            DescarteEntity descarte = new DescarteEntity(
                    usuarioId, itemLabel, pontoColetaNome, pontos, System.currentTimeMillis());
            descarteDao.inserir(descarte);
            usuarioDao.adicionarPontos(usuarioId, pontos);
        });
    }

    public void buscarHistorico(int usuarioId, HistoricoCallback callback) {
        executor.execute(() -> {
            List<DescarteEntity> lista = descarteDao.listarPorUsuario(usuarioId);
            mainHandler.post(() -> callback.onResultado(lista));
        });
    }
}
