/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.agendamento.controllers;

import com.mycompany.agendamento.Conexao;
import com.mycompany.agendamento.objetos.AgendamentoObjeto;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author trindade
 */
public class HorariosController implements Initializable {

    @FXML
    private Button btn_incluir;
    @FXML
    private Button btn_sair;
    @FXML
    private Button btn_alterar;
    @FXML
    private Button btn_excluir;
    @FXML
    private Button btn_listaClientes;
    @FXML
    private Button btn_cadastroHorariosDisponiveis;
    @FXML
    private ComboBox<String> cb_filtro;
    @FXML
    private DatePicker dp_data;
    @FXML
    private TableView<AgendamentoObjeto> tb_horario;
    @FXML
    private TableColumn<AgendamentoObjeto, Integer> tb_horario_id_horario;
    @FXML
    private TableColumn<AgendamentoObjeto, String> tb_horario_data;
    @FXML
    private TableColumn<AgendamentoObjeto, String> tb_horario_horario;
    @FXML
    private TableColumn<AgendamentoObjeto, String> tb_horario_nomeCliente;
    @FXML
    private TableColumn<AgendamentoObjeto, String> tb_horario_idCliente;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btn_incluir.setOnAction(event -> handleIncluir());
        btn_alterar.setOnAction(event -> handleAlterar());
        btn_excluir.setOnAction(event -> handleExcluir());
        btn_sair.setOnAction(event -> handleSair());
        btn_listaClientes.setOnAction(event -> handleListaClientes());
        btn_cadastroHorariosDisponiveis.setOnAction(event -> handleCadastraHorariosDisponiveis());
        dp_data.setValue(LocalDate.now());

        tb_horario_id_horario.setCellValueFactory(param -> new SimpleObjectProperty<Integer>(param.getValue().getId_horario()));
        tb_horario_data.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getData_agenda()));
        tb_horario_horario.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getHora_agenda()));
        tb_horario_idCliente.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getId_cliente()));
        tb_horario_nomeCliente.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getNomeResponsavel()));

        atualizaTabela();
    }

    private void atualizaTabela() {
        try {
            tb_horario.getItems().clear();
            List<AgendamentoObjeto> lista = new ArrayList<>();
            System.out.println("teste");
            try (PreparedStatement statement = Conexao.get("select * from horarios a inner join agendamentos b on a.id_agendamento = b.id_agendamento")) {
                System.out.println("teste2");
                try (ResultSet resultset = statement.executeQuery()) {
                    System.out.println("1");
                    while (resultset.next()) {
                        System.out.println("2");
                        AgendamentoObjeto agendamento = new AgendamentoObjeto();

                        agendamento.setId_horario(resultset.getInt("id_horario"));
                        agendamento.setData_agenda(resultset.getString("data_disponivel"));
                        agendamento.setHora_agenda(resultset.getString("hora_disponivel"));
                        agendamento.setId_cliente(resultset.getString("id_cliente"));
                        agendamento.setNomeResponsavel(selectCliente(resultset.getInt("id_cliente")));
                        lista.add(agendamento);
                    }
                }
            } catch (Exception ex) {
                Mensagem.erro("Erro atualizar tabela de horarios: ", ex);
                System.out.println(ex.getMessage());
                System.out.println(Arrays.toString(ex.getStackTrace()));
            }

            tb_horario.getItems().addAll(lista);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private String selectCliente(int idCliente) {

        String nome = "";

        try (PreparedStatement statement = Conexao.get("select nome from clientes where id_cliente = ?")) {
            statement.setObject(1, idCliente);
            try (ResultSet resultset = statement.executeQuery()) {
                while (resultset.next()) {
                    nome = resultset.getString("nome");
                }
            }
        } catch (Exception ex) {
            Mensagem.erro("Erro select cliente: ", ex);
        }

        return nome;
    }

    private void handleIncluir() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/telas/CadastroHorario.fxml"));
            Stage cadastro = new Stage();
            cadastro.setScene(new Scene(loader.load()));
            cadastro.setTitle("Cadastrar Horario");

            CadastroHorarioController cadastroHorario = loader.getController();
            cadastroHorario.showAndWaitRetorno(cadastro, 0, dp_data.getValue().toString());
            atualizaTabela();
//            cadastro.showAndWait();
        } catch (IOException ex) {
            Mensagem.erro("Erro: ", ex);
        }
    }

    private void handleAlterar() {

        if (tb_horario.getSelectionModel().getSelectedItem() == null) {
            System.out.println("SELECAO NULO");
            return;
        }

        int id_agendamento = selectAgendamentoDoHorario(tb_horario.getSelectionModel().getSelectedItem().getId_horario());

        if (id_agendamento == 0) {
            System.out.println("horario sem agendamento");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/telas/CadastroHorario.fxml"));
            Stage cadastro = new Stage();
            cadastro.setScene(new Scene(loader.load()));
            cadastro.setTitle("Cadastrar Horario");

            CadastroHorarioController cadastroHorario = loader.getController();

            cadastroHorario.showAndWaitRetorno(cadastro, id_agendamento, dp_data.getValue().toString());
            atualizaTabela();
//            cadastro.showAndWait();
        } catch (IOException ex) {
            Mensagem.erro("Erro: ", ex);
        }
    }

    private void handleExcluir() {

        if (tb_horario.getSelectionModel() == null) {
            Mensagem.aviso("Nada selecionado.");
        }

        int id_agendamento = selectAgendamentoDoHorario(tb_horario.getSelectionModel().getSelectedItem().getId_horario());
        try {
            Conexao.begin();
            try (PreparedStatement statement = Conexao.get("DELETE FROM agendamentos WHERE id_agendamento = ?")) {
                statement.setObject(1, id_agendamento);
                statement.executeUpdate();
            } catch (Exception ex) {
                Mensagem.erro("Erro: ", ex);
            }

            try (PreparedStatement statement = Conexao.get("UPDATE horarios set id_agendamento = 0 WHERE id_horario = ?")) {
                statement.setObject(1, tb_horario.getSelectionModel().getSelectedItem().getId_horario());
                statement.executeUpdate();
            } catch (Exception ex) {
                Mensagem.erro("Erro: ", ex);
            }

            Conexao.commit();
            atualizaTabela();
        } catch (Exception ex) {
            Conexao.rollback();
        }
    }

    private int selectAgendamentoDoHorario(int id_horario) {
        int id_agendamento = 0;

        try (PreparedStatement statement = Conexao.get("select id_agendamento from horarios where id_horario = ?")) {
            statement.setObject(1, id_horario);
            try (ResultSet resultset = statement.executeQuery()) {
                while (resultset.next()) {
                    id_agendamento = resultset.getInt("id_agendamento");
                }
            }
        } catch (Exception ex) {
            Mensagem.erro("Erro: ", ex);
        }

        return id_agendamento;
    }

    private void handleListaClientes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/telas/ListaClientes.fxml"));
            Stage cadastro = new Stage();
            cadastro.setScene(new Scene(loader.load()));
            cadastro.setTitle("Lista Clientes");

            CadastroHorarioController cadastroHorario = new CadastroHorarioController();

            cadastro.showAndWait();
        } catch (IOException ex) {
            Mensagem.erro("Erro: ", ex);
        }
    }

    private void handleCadastraHorariosDisponiveis() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/telas/CadastroHorariosDisponiveis.fxml"));
            Stage cadastro = new Stage();
            cadastro.setScene(new Scene(loader.load()));
            cadastro.setTitle("Lista Clientes");

            CadastroHorarioController cadastroHorario = new CadastroHorarioController();

            cadastro.showAndWait();
        } catch (IOException ex) {
            Mensagem.erro("Erro: ", ex);
        }
    }

    private void handleSair() {
        Stage stage = (Stage) btn_sair.getScene().getWindow();
        stage.close();
        Conexao.fechar(); //fechamento principal
    }
}
