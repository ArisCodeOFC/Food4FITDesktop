package br.com.food4fit.controller;

import java.util.EventListener;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ToggleSwitch extends HBox {
	private final Label label = new Label();
	private final Button button = new Button();

	private SimpleBooleanProperty switchedOn = new SimpleBooleanProperty(false);
	private SwitchListener listener;

	public SimpleBooleanProperty switchOnProperty() {
		return switchedOn;
	}

	public void setListener(SwitchListener listener) {
		this.listener = listener;
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

		setWidth(50);
		bindProperties();
	}



	private void bindProperties() {
		label.prefWidthProperty().bind(widthProperty().divide(2));
		label.prefHeightProperty().bind(heightProperty());
		button.prefWidthProperty().bind(widthProperty().divide(2));
		button.prefHeightProperty().bind(heightProperty());
	}

	public ToggleSwitch(boolean status) {
		init();
		setStyle(status);
		switchedOn.set(status);
		switchedOn.addListener((a, b, c) -> {
			setStyle(c);
			if (listener != null) {
				listener.onChange(switchedOn.get());
			}
		});
	}

	public void setStyle(boolean status) {
		if (status) {
			//System.out.println(button.getUserData());
			//label.setText("ON");
			setStyle("-fx-background-color: #282828; -fx-background-radius: 40; -fx-cursor:hand;");
			button.setStyle("-fx-background-radius: 80;");
			button.toFront();
		} else {

			setStyle("-fx-background-color: #7f7f7f; -fx-background-radius: 40; -fx-cursor:hand;");
			label.toFront();
			button.setStyle("-fx-background-radius: 80;");
			//System.out.println(button.getUserData());
			//label.setText("OFF");
		}
	}

	public static interface SwitchListener {
		public void onChange(boolean status);
	}
}
