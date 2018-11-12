package br.com.food4fit.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ToggleSwitch extends HBox {

	private final Label label = new Label();
	private final Button button = new Button();

	private SimpleBooleanProperty switchedOn = new SimpleBooleanProperty(false);

	public SimpleBooleanProperty switchOnProperty() {
		return switchedOn;
	}

	private void init() {
		//button.
		//label.setText("OFF");

		getChildren().addAll(label, button);
		button.setOnAction((e) -> {
			switchedOn.set(!switchedOn.get());
		});
		label.setOnMouseClicked((e) -> {
			switchedOn.set(!switchedOn.get());
		});
		setStyle();
		bindProperties();
	}

	private void setStyle() {
		// Default Width
		setWidth(50);
		label.setAlignment(Pos.CENTER);
		setStyle("-fx-background-color: #9cc283; -fx-text-fill:black; -fx-background-radius: 40; -fx-cursor:hand;");
		setAlignment(Pos.CENTER_LEFT);
		button.setStyle("-fx-background-radius: 40;");
	}

	private void bindProperties() {
		label.prefWidthProperty().bind(widthProperty().divide(2));
		label.prefHeightProperty().bind(heightProperty());
		button.prefWidthProperty().bind(widthProperty().divide(2));
		button.prefHeightProperty().bind(heightProperty());
	}

	public ToggleSwitch() {
		init();
		switchedOn.addListener((a, b, c) -> {
			if (c) {
				button.setUserData(false);
				//System.out.println(button.getUserData());
				//label.setText("ON");
				setStyle("-fx-background-color: #7f7f7f; -fx-background-radius: 40; -fx-cursor:hand;");
				label.toFront();
				button.setStyle("-fx-background-radius: 80;");
			} else {
				button.setUserData(true);
				//System.out.println(button.getUserData());
				//label.setText("OFF");
				setStyle("-fx-background-color: #9cc283; -fx-background-radius: 40; -fx-cursor:hand;");
				button.setStyle("-fx-background-radius: 80;");
				button.toFront();
			}
		});
	}

}
