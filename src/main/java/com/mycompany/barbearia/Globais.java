/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.agendamento;

/**
 *
 * @author trindade
 */
public class Globais {
    
    private static int usuarioLogin = 0;
    private static String nomeUsuario = "";

    public static int getUsuarioLogin() {
        return usuarioLogin;
    }

    public static void setUsuarioLogin(int usuarioLogin) {
        Globais.usuarioLogin = usuarioLogin;
    }

    public static String getNomeUsuario() {
        return nomeUsuario;
    }

    public static void setNomeUsuario(String nomeUsuario) {
        Globais.nomeUsuario = nomeUsuario;
    }
}
