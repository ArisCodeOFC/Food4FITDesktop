package br.com.food4fit;

import java.util.Optional;

import br.com.food4fit.model.Funcionario;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

public class Main extends Application {

	static Stage primaryStage;
	static Funcionario perfil;


	@Override
	public void start(Stage primaryStage) {
		Main.primaryStage = primaryStage;

		// Screen screen = Screen.getPrimary();
		// Rectangle2D bounds = screen.getVisualBounds();
		// primaryStage.setX(bounds.getMinX());
		// primaryStage.setY(bounds.getMinY());
		// primaryStage.setMaxWidth(bounds.getWidth());
		// primaryStage.setMinWidth(bounds.getWidth());
		// primaryStage.setMaxHeight(bounds.getHeight());
		// primaryStage.setMinHeight(bounds.getHeight());

		Font.loadFont(Main.class.getResourceAsStream("/br/com/food4fit/assets/fonts/Roboto-Regular.ttf"), 10);
		Font.loadFont(Main.class.getResourceAsStream("/br/com/food4fit/assets/fonts/Roboto-Medium.ttf"), 10);
		Font.loadFont(Main.class.getResourceAsStream("/br/com/food4fit/assets/fonts/Roboto-Black.ttf"), 10);

		Main.abrirTela("Login");
	}

	public static void abrirTela(String nomeArquivo) {
		Parent tela;

		try {
			// Carregando xml no objeto
			tela = FXMLLoader.load(Main.class.getResource("view/" + nomeArquivo + ".fxml"));

			// Criando o objeto de "cena" com xml
			Scene scene = new Scene(tela);

			// Colocando o objeto de cena no Stage
			primaryStage.setScene(scene);

			primaryStage.setResizable(false);

			primaryStage.setTitle("FOOD 4FIT");

			primaryStage.getIcons().add(new Image(Main.class.getResource("assets/icons/favicon.png").openStream()));
			// Exibindo na tela
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void voltarLogin() {
		Main.abrirTela("Login");
	}

	public static void main(String[] args) {
		launch(args);
	}

	// Metodos para dialogs

	public static int showConfirmDialog(String bt, String titulo, String texto, AlertType estilo) {
		int resultado = 0;
		Alert dialogo = new Alert(Alert.AlertType.WARNING);
		ButtonType btnSim = new ButtonType(bt);
		ButtonType btnNao = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

		dialogo.setTitle(titulo);
		dialogo.setHeaderText(texto);
		dialogo.getButtonTypes().setAll(btnSim, btnNao);

		Stage stage = (Stage) dialogo.getDialogPane().getScene().getWindow();

		// Adiciona uma imagen no icone do dialog
		stage.getIcons().add(new Image(Main.class.getResource("assets/icons/favicon.png").toString()));

		Optional<ButtonType> result = dialogo.showAndWait();

		if (result.get() == btnSim) {
			resultado = 1;
		}

		return resultado;
	}

	public static void showErrorDialog(String titulo, String texto, String contexto, AlertType estilo) {
		Alert dialogo = new Alert(estilo);

		dialogo.setTitle(titulo);
		dialogo.setHeaderText(texto);
		dialogo.setContentText(contexto);

		Stage stage = (Stage) dialogo.getDialogPane().getScene().getWindow();

		stage.getIcons().add(new Image(Main.class.getResource("assets/icons/favicon.png").toString()));

		dialogo.showAndWait();

	}

	public static void showInfDialog(String titulo, String texto, String contexto) {
		Alert dialogo = new Alert(Alert.AlertType.INFORMATION);

		dialogo.setTitle(titulo);
		dialogo.setHeaderText(texto);
		dialogo.setContentText(contexto);

		Stage stage = (Stage) dialogo.getDialogPane().getScene().getWindow();

		stage.getIcons().add(new Image(Main.class.getResource("assets/icons/favicon.png").toString()));

		dialogo.showAndWait();

	}

	public static  Funcionario getPerfil() {
		return perfil;
	}

	public static void setPerfil(Funcionario perfil) {
		Main.perfil = perfil;
	}

}
