package com.example.ecovault_pi_mobile.database.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.ecovault_pi_mobile.database.entity.DescarteEntity;

import java.util.List;

@Dao
public interface DescarteDao {

    @Insert
    long inserir(DescarteEntity descarte);

    @Query("SELECT * FROM descartes WHERE usuario_id = :usuarioId ORDER BY data_hora DESC")
    List<DescarteEntity> listarPorUsuario(int usuarioId);

    @Query("SELECT COUNT(*) FROM descartes WHERE usuario_id = :usuarioId")
    int contarDescartes(int usuarioId);
}
