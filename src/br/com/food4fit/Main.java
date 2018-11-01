package br.com.food4fit;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;


public class Main extends Application {

	static Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		Main.primaryStage = primaryStage;



		//Screen screen = Screen.getPrimary();
		//Rectangle2D bounds = screen.getVisualBounds();
		//primaryStage.setX(bounds.getMinX());
		//primaryStage.setY(bounds.getMinY());
		//primaryStage.setMaxWidth(bounds.getWidth());
		//primaryStage.setMinWidth(bounds.getWidth());
		//primaryStage.setMaxHeight(bounds.getHeight());
		//primaryStage.setMinHeight(bounds.getHeight());


		Font.loadFont(Main.class.getResourceAsStream("assets/fonts/Roboto-Regular.ttf"), 10);
		Font.loadFont(Main.class.getResourceAsStream("assets/fonts/Roboto-Medium.ttf"), 10);
		Font.loadFont(Main.class.getResourceAsStream("assets/fonts/Roboto-Black.ttf"), 10);

		Main.abrirTela("PadraoLayout");
	}


	public static void abrirTela(String nomeArquivo){
		Parent tela;

		try {
			//Carregando xml no objeto
			tela = FXMLLoader.load(Main.class.getResource("view/"+ nomeArquivo + ".fxml"));

			//Criando o objeto de "cena" com xml
			Scene scene = new Scene(tela);

			//Colocando o objeto fde cena no Stage
			primaryStage.setScene(scene);

			primaryStage.setResizable(false);


			primaryStage.setTitle("FOOD 4FIT");

			primaryStage.getIcons().add(new Image(Main.class.getResource("assets/icons/favicon.png").openStream()));
			//Exibindo na tela
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}



	}


	@FXML
	public void voltarLogin(){
		Main.abrirTela("Login");
	}


	public static void main(String[] args) {
		launch(args);
	}



}
