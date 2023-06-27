/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.agendamento.controllers;

import com.mycompany.agendamento.Conexao;
import com.mycompany.agendamento.models.ModelCliente;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author trindade
 */
public class ListaClientesController implements Initializable {

    @FXML
    private Button btn_incluir;
    @FXML
    private Button btn_sair;
    @FXML
    private Button btn_alterar;
    @FXML
    private Button btn_excluir;
    @FXML
    private TableView<ModelCliente> tb_clientes;
    @FXML
    private TableColumn<ModelCliente, String> tb_clientes_id_cliente;
    @FXML
    private TableColumn<ModelCliente, String> tb_clientes_nome;
    @FXML
    private TableColumn<ModelCliente, String> tb_clientes_telefone;
    @FXML
    private TableColumn<ModelCliente, String> tb_clientes_email;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    private void handleIncluir() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/telas/CadastroClientesController.fxml"));
            Stage cadastro = new Stage();
            cadastro.setScene(new Scene(loader.load()));
            cadastro.setTitle("Cadastrar Clientes");

            CadastroClientesController cadastroHorario = new CadastroClientesController();
//            cadastroHorario.showAndWaitRetorno(cadastro, 0, dp_data.getValue().toString());

//            cadastro.showAndWait();
        } catch (IOException ex) {
            Mensagem.erro("Erro: ", ex);
        }
    }

    private void handleAlterar() {

        if (tb_clientes.getSelectionModel().getSelectedItem() == null) {
            System.out.println("SELECAO NULO");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/telas/CadastroHorario.fxml"));
            Stage cadastro = new Stage();
            cadastro.setScene(new Scene(loader.load()));
            cadastro.setTitle("Cadastrar Horario");

            CadastroHorarioController cadastroHorario = new CadastroHorarioController();

//            cadastroHorario.showAndWaitRetorno(cadastro, id_agendamento, dp_data.getValue().toString());
//            cadastro.showAndWait();
        } catch (IOException ex) {
            Mensagem.erro("Erro: ", ex);
        }
    }

    private void handleExcluir() {

        if (tb_clientes.getSelectionModel() == null) {
            Mensagem.aviso("Nada selecionado.");
        }

        if(selectClientesComHorarios(tb_clientes.getSelectionModel().getSelectedItem().getId_cliente())){
            Mensagem.aviso("Não é possível excluir o cliente pois o mesmo tem agendamentos cadastrados no sistema.");
        }
        
        try {
            Conexao.conectar();
            Conexao.begin();

            try (PreparedStatement statement = Conexao.get("DELETE FROM clientes WHERE id_cliente = ?")) {
                statement.setObject(1, tb_clientes.getSelectionModel().getSelectedItem().getId_cliente());
                statement.executeUpdate();
            } catch (Exception ex) {
                Mensagem.erro("Erro: ", ex);
            }

            Conexao.commit();
        } catch (Exception ex) {
            Conexao.rollback();
        }
    }

    private boolean selectClientesComHorarios(int codCliente) {

        boolean clienteComPendencia = false;

        try (PreparedStatement statement = Conexao.get("select * from agendamentos where id_cliente = ?")) {
            statement.setObject(1, codCliente);
            try (ResultSet resultset = statement.executeQuery()) {
                while (resultset.next()) {
                    clienteComPendencia = true;
                }
            }
        } catch (Exception ex) {
            Mensagem.erro("Erro: ", ex);
        }
        
        return clienteComPendencia;
    }
}
