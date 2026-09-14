package com.example.ecovault_pi_mobile.database.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ecovault_pi_mobile.database.entity.UsuarioEntity;

@Dao
public interface UsuarioDao {

    @Insert
    long inserir(UsuarioEntity usuario);

    @Update
    void atualizar(UsuarioEntity usuario);

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    UsuarioEntity buscarPorEmail(String email);

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    UsuarioEntity buscarPorId(int id);

    @Query("UPDATE usuarios SET total_pontos = total_pontos + :pontos WHERE id = :usuarioId")
    void adicionarPontos(int usuarioId, int pontos);
}
