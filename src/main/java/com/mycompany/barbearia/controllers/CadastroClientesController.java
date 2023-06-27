/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.agendamento.controllers;

import com.mycompany.agendamento.Conexao;
import com.mycompany.agendamento.models.ModelAgendamento;
import com.mycompany.agendamento.models.ModelCliente;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author trindade
 */
public class CadastroClientesController implements Initializable {

    @FXML
    private TextField tf_cliente;
    @FXML
    private Button btn_salvar;
    @FXML
    private Button btn_sair;
    @FXML
    private TextField tf_email;
    @FXML
    private TextField tf_telefone;

    int codRetorno = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btn_salvar.setOnAction(event -> handleSalvar());
        btn_sair.setOnAction(event -> handleSair());
        tf_cliente.setText("");
        tf_email.setText("");
        tf_telefone.setText("");
    }

    private void handleSalvar() {
        if (codRetorno == 0) {
            //insert
        } else {
            //update
        }
    }

    public int showAndWaitRetorno(Stage stage, int codigo, String data) {

        if (codigo <= 0) {
            tf_cliente.setText("");
            tf_email.setText("");
            tf_telefone.setText("");

        } else {
            ModelCliente model = selectCliente(codigo);
            tf_cliente.setText(model.getNome());
            tf_email.setText(model.getNome());
            tf_telefone.setText(model.getNome());
        }

        stage.showAndWait();
        return codRetorno;
    }

    private ModelCliente selectCliente(int codCliente) {
        
        ModelCliente model = new ModelCliente();
        
          try (PreparedStatement statement = Conexao.get("select * from clientes where id_cliente = ?")) {
            statement.setObject(1, codCliente);
            try (ResultSet resultset = statement.executeQuery()) {
                while (resultset.next()) {
                    model.setId_cliente(resultset.getInt("id_cliente"));
                    model.setNome(resultset.getString("nome"));
                    model.setEmail(resultset.getString("email"));
                    model.setTelefone(resultset.getString("telefone"));
                }
            }
        } catch (Exception ex) {
            Mensagem.erro("Erro: ", ex);
        }
        
        return model;
    }

    private void handleSair() {
        Stage stage = (Stage) btn_sair.getScene().getWindow();
        stage.close();
    }

}
