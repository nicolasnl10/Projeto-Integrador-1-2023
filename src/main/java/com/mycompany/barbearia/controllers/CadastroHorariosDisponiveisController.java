package com.mycompany.agendamento.controllers;

import com.mycompany.agendamento.Conexao;
import com.mycompany.agendamento.Globais;
import java.net.URL;
import java.sql.PreparedStatement;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author trindade
 */
public class CadastroHorariosDisponiveisController implements Initializable {

    @FXML
    private Button btn_salvar;
    @FXML
    private Button btn_sair;
    @FXML
    private DatePicker dp_dataDe;
    @FXML
    private DatePicker dp_dataAte;
    @FXML
    private TextField tf_horaDe;
    @FXML
    private TextField tf_horaAte;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btn_salvar.setOnAction(event -> handleSalvar());
        btn_sair.setOnAction(event -> handleSair());

        dp_dataDe.setValue(LocalDate.now());
        dp_dataAte.setValue(LocalDate.now());
        tf_horaDe.setText("08:00:00");
        tf_horaAte.setText("18:00:00");
    }

    private void handleSalvar() {

        if (!verificar()) {
            return;
        }

        List<LocalDate> listaDatas = defineData();
        List<LocalTime> listaHoras = defineHorario();

        try {
            Conexao.begin();
            for (LocalDate data : listaDatas) {
                for (LocalTime hora : listaHoras) {
                    try (PreparedStatement statement = Conexao.get("insert into horarios (id_usuario, data_disponivel, hora_disponivel, id_agendamento) "
                            + "values (?, ?, ?, 0)")) {
                        statement.setObject(1, Globais.getUsuarioLogin());
                        statement.setObject(2, data);
                        statement.setObject(3, hora);
                        System.out.println("query: " + statement);
                        System.out.println("antes do insert");
                        statement.executeUpdate();
                    } catch (Exception ex) {
                        throw new Exception(ex);
                    }
                }
            }
            Conexao.commit();
        } catch (Exception ex) {
            Conexao.rollback();
            Mensagem.erro("Erro salvamento horarios disponiveis: ", ex);
        }
    }

    private void handleSair() {
        Stage stage = (Stage) btn_sair.getScene().getWindow();
        stage.close();
        Conexao.fechar();
    }

    private boolean verificar() {

        if (tf_horaDe.getText().isEmpty()) {
            Mensagem.aviso("Hora vazia.");
            return false;
        }
        if (tf_horaAte.getText().isEmpty()) {
            Mensagem.aviso("Hora vazia.");
            return false;
        }
        if (dp_dataDe.getValue() == null) {
            Mensagem.aviso("Data vazia.");
            return false;
        }
        if (dp_dataDe.getValue() == null) {
            Mensagem.aviso("Data vazia.");
            return false;
        }

        return true;
    }

    private List<LocalTime> defineHorario() {
        LocalTime horaInicio = LocalTime.parse(tf_horaDe.getText());
        LocalTime horaFim = LocalTime.parse(tf_horaAte.getText());

        List<LocalTime> listaHoras = new ArrayList<>();

        // Adicionar a hora de início à lista
        listaHoras.add(horaInicio);

        LocalTime horaAtual = horaInicio.plusHours(1);
        while (horaAtual.isBefore(horaFim)) {
            listaHoras.add(horaAtual);
            horaAtual = horaAtual.plusHours(1);
        }

        // Adicionar a hora de fim à lista
        listaHoras.add(horaFim);

        // Imprimir a lista de horas
        for (LocalTime hora : listaHoras) {
            System.out.println(hora);
        }

        return listaHoras;
    }

    private List<LocalDate> defineData() {
        LocalDate dataInicio = dp_dataDe.getValue();
        LocalDate dataFim = dp_dataAte.getValue();

        List<LocalDate> listaDatas = new ArrayList<>();

        // Adicionar a data de início à lista
        listaDatas.add(dataInicio);

        // Adicionar as datas intermediárias à lista, excluindo os sábados
        LocalDate dataAtual = dataInicio.plusDays(1);
        while (dataAtual.isBefore(dataFim)) {
            if (dataAtual.getDayOfWeek() != DayOfWeek.SATURDAY) {
                listaDatas.add(dataAtual);
            }
            dataAtual = dataAtual.plusDays(1);
        }

        // Adicionar a data de fim à lista
        listaDatas.add(dataFim);

        return listaDatas;
    }
}
