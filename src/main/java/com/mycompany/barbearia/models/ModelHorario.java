package com.mycompany.agendamento.models;

/**
 *
 * @author trindade
 */
public class ModelHorario extends Model{
    int id_horario = 0;
    int id_usuario = 0;
    String data_disponivel = "";
    String hora_disponivel = "";
    int id_agendamento = 0;

    public int getId_horario() {
        return id_horario;
    }

    public void setId_horario(int id_horario) {
        this.id_horario = id_horario;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getData_disponivel() {
        return data_disponivel;
    }

    public void setData_disponivel(String data_disponivel) {
        this.data_disponivel = data_disponivel;
    }

    public String getHora_disponivel() {
        return hora_disponivel;
    }

    public void setHora_disponivel(String hora_disponivel) {
        this.hora_disponivel = hora_disponivel;
    }

    public int getId_agendamento() {
        return id_agendamento;
    }

    public void setId_agendamento(int id_agendamento) {
        this.id_agendamento = id_agendamento;
    }
    
    
}
