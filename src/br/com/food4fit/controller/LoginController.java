package br.com.food4fit.controller;

import br.com.food4fit.Main;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField txt;

    @FXML
    private PasswordField txtSenha;

    @FXML
    void semConta() {
    	Main.abrirTela("MensagemLogin");
    }

    @FXML
    void login() {
    	Main.abrirTela("Dashboard");
    }
}
