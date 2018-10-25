package br.com.food4fit;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {

	static Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		Main.primaryStage = primaryStage;




		Main.abrirTela("MensagemLogin");

	}


	public static void abrirTela(String nomeArquivo){
		Parent tela;

		try {
			//Carregando xml no objeto
			tela = FXMLLoader.load(Main.class.getResource("view/"+ nomeArquivo + ".fxml"));

			//Criando o objeto de "cena" com xml
			Scene sc = new Scene(tela);

			//Colocando o objeto fde cena no Stage
			primaryStage.setScene(sc);

			//Exibindo na tela
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

		//scene.getStylesheets().add(getClass().getResource("css/application.css").toExternalForm());

	}

	@FXML
	public void voltarLogin(){
		Main.abrirTela("Login");
	}


	public static void main(String[] args) {
		launch(args);
	}



}
