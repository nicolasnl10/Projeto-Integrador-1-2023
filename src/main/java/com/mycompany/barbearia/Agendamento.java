package com.mycompany.agendamento;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author trindade
 */
public class Agendamento extends Application {

    public static void main(String[] args) {
           launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            
            Parent root = FXMLLoader.load(getClass().getResource("/telas/Login.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login");
            Conexao.conectar();
            primaryStage.show();
        } catch (Exception ex) {
            System.out.println("erro:" + ex);
        }
    }
}
