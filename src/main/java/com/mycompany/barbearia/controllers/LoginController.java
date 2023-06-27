package com.mycompany.agendamento.controllers;

import com.mycompany.agendamento.Conexao;
import com.mycompany.agendamento.Globais;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author trindade
 */
public class LoginController implements Initializable {

    @FXML
    private TextField tf_usuario;
    @FXML
    private Button btn_salvar;
    @FXML
    private Button btn_sair;
    @FXML
    private TextField tf_senha;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tf_usuario.setText("");
        tf_senha.setText("");
        btn_salvar.setOnAction(event -> handleEntrar());
        btn_sair.setOnAction(event -> handleSair());
    }

    private void handleEntrar() {

        boolean usuarioValido = false;
        int id_usuario = 0;

        try (PreparedStatement statement = Conexao.get("select * from usuarios where nome = ? and senha = ?")) {
            statement.setObject(1, tf_usuario.getText());
            statement.setObject(2, tf_senha.getText());
            try (ResultSet resultset = statement.executeQuery()) {
                while (resultset.next()) {
                    id_usuario = resultset.getInt("id_usuario");
                    usuarioValido = true;
                }
            }
        } catch (Exception ex) {
            Mensagem.erro("Erro select cliente: ", ex);
        }

        if (!usuarioValido) {
            Mensagem.aviso("Usuario inválido");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/telas/Horarios.fxml"));
            Stage cadastro = new Stage();
            cadastro.setScene(new Scene(loader.load()));
            cadastro.setTitle("Horarios");
            Conexao.conectar(); // conexao principal            
            Globais.setUsuarioLogin(id_usuario);
            Globais.setNomeUsuario(tf_usuario.getText());
            cadastro.show();
            handleSair();
        } catch (Exception ex) {
            Mensagem.erro("Erro: ", ex);
        }
    }

    private void handleSair() {
        Stage stage = (Stage) btn_sair.getScene().getWindow();
        stage.close();
    }
}
