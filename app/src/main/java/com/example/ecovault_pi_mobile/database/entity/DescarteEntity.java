package com.example.ecovault_pi_mobile.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "descartes")
public class DescarteEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "usuario_id")
    private int usuarioId;

    @ColumnInfo(name = "item_label")
    private String itemLabel;

    @ColumnInfo(name = "ponto_coleta_nome")
    private String pontoColetaNome;

    @ColumnInfo(name = "pontos_ganhos")
    private int pontosGanhos;

    @ColumnInfo(name = "data_hora")
    private long dataHora;

    public DescarteEntity(int usuarioId, String itemLabel, String pontoColetaNome,
                          int pontosGanhos, long dataHora) {
        this.usuarioId = usuarioId;
        this.itemLabel = itemLabel;
        this.pontoColetaNome = pontoColetaNome;
        this.pontosGanhos = pontosGanhos;
        this.dataHora = dataHora;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuarioId() { return usuarioId; }
    public String getItemLabel() { return itemLabel; }
    public String getPontoColetaNome() { return pontoColetaNome; }
    public int getPontosGanhos() { return pontosGanhos; }
    public long getDataHora() { return dataHora; }
}
