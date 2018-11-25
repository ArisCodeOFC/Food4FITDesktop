package br.com.food4fit.controller;

import java.io.IOException;
import java.util.Optional;

import org.omg.CORBA.INITIALIZE;

import br.com.food4fit.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class PadraoLayoutController {

	@FXML
	private Pane main;


    @FXML
    private Pane blackMode;

	@FXML
	private Label nomeTela;

    @FXML
    private AnchorPane raiz;

    @FXML
    private Label emailUser;

    @FXML
    private Label nomeUser;

    @FXML
    private ImageView fotoUser;


	@FXML
	public void initialize() {
		mudarTela("Dashboard", "Dashboard");

		raiz.getStylesheets().add("br/com/food4fit/view/white.css");

		ToggleSwitch botao = new ToggleSwitch(false);

		blackMode.getChildren().add(botao);

		botao.setListener(status -> {
			raiz.getStylesheets().clear();
			if (status) {
				raiz.getStylesheets().add("br/com/food4fit/view/black.css");
			} else {
				raiz.getStylesheets().add("br/com/food4fit/view/white.css");
			}
		});


		//nomeUser.setText(Main.getPerfil().getNomeCompleto());
		//emailUser.setText(Main.getPerfil().getEmail());
		//fotoUser.setImage(new Image("http://localhost/inf4t/Allan/Food-4FitWEB-Procedure-master/" + Main.getPerfil().getAvatar()));
		//fotoUser.setImage(new Image("http://localhost/arisCodeProcedural/" + Main.getPerfil().getAvatar()));

	}

	@FXML
	void menuHome() {
		mudarTela("Dashboard", "Dashboard");

	}

	@FXML
	void menuEstatisticas() {
		mudarTela("Funcionarios", "Estatísticas");
	}

	@FXML
	void menuFornecedor() {
		mudarTela("Fornecedor", "Fornecedores");
	}

	@FXML
	void menuFuncionario() {
		mudarTela("Funcionarios", "Funcionários");
	}

	@FXML
	void menuUsuario() {
		mudarTela("Usuario", "Usuários");
	}

	@FXML
	void menuNivel() {
		mudarTela("Funcionarios", "Nível de Acesso");
	}

	@FXML
	void menuDespesa() {
		mudarTela("Funcionarios", "Despesas");
	}

	@FXML
	void menuRelatorio() {
		mudarTela("Funcionarios", "Relários");
	}

	@FXML
	void menuBanco() {
		mudarTela("Banco", "Banco");
	}

	@FXML
	void menuDepartamento() {
		mudarTela("Departamento", "Departamento");
	}

	@FXML
	void menuFaturamento() {
		mudarTela("Funcionarios", "Faturamento");
	}

	@FXML
	void menuUnidade() {
		mudarTela("UnidadeDeMedida", "Unidade de Medida");
	}

	@FXML
	void menuPerfil() {
		mudarTela("Perfil", "Meu Perfil");
	}

	@FXML
	void menuAjuda() {
		mudarTela("Funcionarios", "Ajuda");
	}

	@FXML
	void sair() {

		int result = Main.showConfirmDialog("Sim", "Sair", "Deseja sair da sua conta?", Alert.AlertType.WARNING);

		if (result == 1) {
			Main.abrirTela("Login");
		}

	}

	void mudarTela(String conteudo, String nome) {
		Pane xml;
		try {
			xml = FXMLLoader.load(Main.class.getResource("view/" + conteudo + ".fxml"));

			main.getChildren().clear();
			main.getChildren().add(xml);

			nomeTela.setText(nome);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}