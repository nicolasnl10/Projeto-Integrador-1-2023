package com.mycompany.agendamento.models;

/**
 *
 * @author trindade
 */
public class ModelAgendamento extends Model{

    int id_agendamento = 0;
    int id_cliente = 0;
    int id_usuario = 0;
    String data_agenda = "";
    String hora_agenda = "";
    int status_agenda = 0;

    public int getId_agendamento() {
        return id_agendamento;
    }

    public void setId_agendamento(int id_agendamento) {
        this.id_agendamento = id_agendamento;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getData_agenda() {
        return data_agenda;
    }

    public void setData_agenda(String data_agenda) {
        this.data_agenda = data_agenda;
    }

    public String getHora_agenda() {
        return hora_agenda;
    }

    public void setHora_agenda(String hora_agenda) {
        this.hora_agenda = hora_agenda;
    }

    public int getStatus_agenda() {
        return status_agenda;
    }

    public void setStatus_agenda(int status_agenda) {
        this.status_agenda = status_agenda;
    }
    
    

}
