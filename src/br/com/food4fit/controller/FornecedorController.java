package br.com.food4fit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class FornecedorController {
	  @FXML
	    private TableColumn colunaEmail;

	    @FXML
	    private AnchorPane conteudo;

	    @FXML
	    private TableColumn colunaOpc;

	    @FXML
	    private TableColumn colunaRazao;

	    @FXML
	    private TableColumn colunaNome;

	    @FXML
	    private TableColumn colunaCpf;

	    @FXML
	    private TableView tblFornecedor;

	    @FXML
	    private Pane paneConteudo;


	    public void initialize(){
	    	paneConteudo.setStyle("visibility: false");
	    }


	    @FXML
	    void abrirConteudo() {
	    	paneConteudo.setStyle("visibility: true; -fx-background-color: #dcdcdc");
	    }


	    @FXML
	    void fechaConteudo(){
	    	paneConteudo.setStyle("visibility: false");
	    }
}
