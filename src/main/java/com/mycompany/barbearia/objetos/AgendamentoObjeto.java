/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.agendamento.objetos;

/**
 *
 * @author trindade
 */
public class AgendamentoObjeto {
    int id_horario = 0;
    int id_agendamento = 0;
    String id_cliente = "";
    int id_usuario = 0;
    String nomeResponsavel = "";
    String data_agenda = "";
    String hora_agenda = "";
    int status_agenda = 0;

    public int getId_horario() {
        return id_horario;
    }

    public void setId_horario(int id_horario) {
        this.id_horario = id_horario;
    }

    
    public int getId_agendamento() {
        return id_agendamento;
    }

    public void setId_agendamento(int id_agendamento) {
        this.id_agendamento = id_agendamento;
    }

    public String getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(String id_cliente) {
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
    
    
    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }   
}
