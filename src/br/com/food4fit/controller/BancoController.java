package br.com.food4fit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class BancoController {
	@FXML
    private TextField txtAgencia;

    @FXML
    private TextField txtConta;

    @FXML
    private TableColumn colunaOpc;

    @FXML
    private TableColumn colunaBanco;

    @FXML
    private TableColumn colunaAgencia;

    @FXML
    private TableView tblBanco;

    @FXML
    private Pane paneConteudo;

    @FXML
    private TextField txtBanco;

    @FXML
    private TableColumn colunaConta;


    public void initialize(){
    	paneConteudo.setStyle("visibility: false");
    }


    @FXML
    void salvar() {

    }

 // Abrir o panel oculto
 	@FXML
 	void abrirConteudo() {
 		paneConteudo.setStyle("visibility: true; -fx-background-color: #dcdcdc");
 	}

 	// Fecha o panel que foi aberto
 	@FXML
 	void fechaConteudo() {
 		paneConteudo.setStyle("visibility: false");
 		txtBanco.clear();
 		txtAgencia.clear();
 		txtConta.clear();

 	}
}
