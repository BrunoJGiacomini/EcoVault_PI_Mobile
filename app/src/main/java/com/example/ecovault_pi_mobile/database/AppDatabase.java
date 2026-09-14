package com.example.ecovault_pi_mobile.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ecovault_pi_mobile.database.DAO.DescarteDao;
import com.example.ecovault_pi_mobile.database.DAO.UsuarioDao;
import com.example.ecovault_pi_mobile.database.entity.DescarteEntity;
import com.example.ecovault_pi_mobile.database.entity.UsuarioEntity;

@Database(entities = {UsuarioEntity.class, DescarteEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase instancia;

    public abstract UsuarioDao usuarioDao();
    public abstract DescarteDao descarteDao();

    public static AppDatabase getInstance(Context context) {
        if (instancia == null) {
            synchronized (AppDatabase.class) {
                if (instancia == null) {
                    instancia = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "ecovault_db"
                    ).build();
                }
            }
        }
        return instancia;
    }
}
