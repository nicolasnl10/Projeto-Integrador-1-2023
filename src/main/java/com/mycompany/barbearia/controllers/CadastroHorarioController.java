package com.mycompany.agendamento.controllers;

import com.mycompany.agendamento.Conexao;
import com.mycompany.agendamento.Globais;
import com.mycompany.agendamento.models.ModelAgendamento;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author trindade
 */
public class CadastroHorarioController implements Initializable {

    @FXML
    private Button btn_cliente;
    @FXML
    private TextField tf_cliente;
    @FXML
    private ComboBox<String> cb_horarios;
    @FXML
    private Button btn_salvar;
    @FXML
    private Button btn_sair;
    @FXML
    private Label lb_data;

    int codRetorno = 0;
    String datahorario = "";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btn_salvar.setOnAction(event -> handleSalvar());
        btn_sair.setOnAction(event -> handleSair());
        System.out.println("datahorario: " + datahorario);
        lb_data.setText("Data: " + datahorario);

        cb_horarios.setItems(
                FXCollections.observableArrayList(
                        "Valor 1",
                        "Valor 2",
                        "Valor 3"
                ));
    }

    private void handleSalvar() {

        if (!verificar()) {
            return;
        }
        int chaveGerada = 0;

        try {
            Conexao.begin();
            if (codRetorno == 0) {

                try (PreparedStatement statement = Conexao.getInserts("insert into agendamentos (id_cliente, id_usuario, data_agenda, hora_agenda, status_agenda) values (?, ?, ?, ?, 0)", "id_agendamento")) {
                    statement.setObject(1, Integer.valueOf(tf_cliente.getText()));
                    statement.setObject(2, Globais.getUsuarioLogin());
                    statement.setObject(3, datahorario);
                    statement.setObject(4, cb_horarios.getValue());
                    statement.executeUpdate();
                    try (ResultSet resultSet = statement.getGeneratedKeys()) {
                        while (resultSet.next()) {
                            chaveGerada = resultSet.getInt(1);
                        }
                    }
                } catch (Exception ex) {
                    throw new Exception(ex);
                }

                int id_horario = selectIdHorario();

                try (PreparedStatement statement = Conexao.get("UPDATE horarios set id_agendamento = ? WHERE id_horario = ?")) {
                    System.out.println("chaveGerada: " + chaveGerada);
                    statement.setObject(1, chaveGerada);
                    statement.setObject(2, id_horario);
                    statement.executeUpdate();
                } catch (Exception ex) {
                    throw new Exception(ex);
                }

            } else {
                try (PreparedStatement statement = Conexao.get("update agendamentos set id_cliente = ? id_usuario = ? "
                        + " data_agenda = ?, hora_agenda = ?, status_agenda = ? ")) {
                    statement.setObject(1, tf_cliente.getText());
                    statement.setObject(2, Globais.getUsuarioLogin());
                    statement.setObject(3, datahorario);
                    statement.setObject(4, cb_horarios.getValue());
                    statement.setObject(5, 0);
                    statement.executeUpdate();
                } catch (Exception ex) {
                    throw new Exception(ex);
                }
            }
            Conexao.commit();
            Mensagem.sucesso("SUCESSO AO SALVAR!");
            handleSair();
        } catch (Exception ex) {
            Mensagem.erro("Erro ao salvar!", ex);
            Conexao.rollback();
        }

    }

    private boolean verificar() {

        //select pra conferir cliente
        if (tf_cliente.getText().isEmpty() || tf_cliente.getText().equals("0")) {
            Mensagem.aviso("Cliente invalido.");
            return false;
        }
        
        if (cb_horarios.getValue() == null) {
            Mensagem.aviso("Horario invalido.");
            return false;
        }

        return true;
    }

    private void handleSair() {
        Stage stage = (Stage) btn_sair.getScene().getWindow();
        stage.close();
    }

    public int showAndWaitRetorno(Stage stage, int codigo, String data) {

        if (codigo <= 0) {
            tf_cliente.setText("0");
            datahorario = data;
            lb_data.setText("Data: " + datahorario);
            List<String> listaHorarios = selectHorariosDisponiveis();
            cb_horarios.setItems((ObservableList<String>) listaHorarios);
            cb_horarios.getSelectionModel().selectFirst();

        } else {
            ModelAgendamento model = selectHorario(codigo);
            codRetorno = codigo;
            tf_cliente.setText(String.valueOf(model.getId_cliente()));
            lb_data.setText("Data: "+model.getData_agenda());
            datahorario = model.getData_agenda();
            List<String> listaHorarios = selectHorariosDisponiveis();
            listaHorarios.add(model.getHora_agenda());
            cb_horarios.setItems((ObservableList<String>) listaHorarios);
            cb_horarios.getSelectionModel().select(model.getHora_agenda());
        }

        stage.showAndWait();
        return codRetorno;
    }

    private List<String> selectHorariosDisponiveis() {

        List<String> lista = FXCollections.observableArrayList();

        try (PreparedStatement statement = Conexao.get("select * from horarios where data_disponivel = ? and id_agendamento = 0")) {
            statement.setObject(1, datahorario);
            try (ResultSet resultset = statement.executeQuery()) {
                while (resultset.next()) {
                    lista.add(resultset.getString("hora_disponivel"));
                }
            }
        } catch (Exception ex) {
            Mensagem.erro("Erro: ", ex);
        }

        return lista;
    }

    private int selectIdHorario() {

        int id_horario = 0;

        try (PreparedStatement statement = Conexao.get("select * from horarios where data_disponivel = ? and hora_disponivel = ?")) {
            statement.setObject(1, datahorario);
            statement.setObject(2, cb_horarios.getValue());
            try (ResultSet resultset = statement.executeQuery()) {
                while (resultset.next()) {
                    id_horario = resultset.getInt("id_horario");
                }
            }
        } catch (Exception ex) {
            Mensagem.erro("Erro: ", ex);
        }

        return id_horario;
    }

    private ModelAgendamento selectHorario(int cod) {

        ModelAgendamento model = new ModelAgendamento();

        try (PreparedStatement statement = Conexao.get("select * from agendamentos where id_agendamento = ? ")) {
            statement.setObject(1, cod);
            try (ResultSet resultset = statement.executeQuery()) {
                while (resultset.next()) {
                    model.setData_agenda(resultset.getString("data_agenda"));
                    model.setHora_agenda(resultset.getString("hora_agenda"));
                    model.setId_agendamento(resultset.getInt("id_agendamento"));
                    model.setId_usuario(resultset.getInt("id_usuario"));
                    model.setId_cliente(resultset.getInt("id_cliente"));
                    model.setStatus_agenda(resultset.getInt("status_agenda"));
                }
            }
        } catch (Exception ex) {
            Mensagem.erro("Erro: ", ex);
        }

        return model;
    }
}

// insert into table (data, hora) values (15/06, 08:00), (15/06, 09:00), (15/06, 10:00)
// insert into table (data, hora) values (16/06, 08:00), (16/06, 09:00), (16/06, 10:00)
