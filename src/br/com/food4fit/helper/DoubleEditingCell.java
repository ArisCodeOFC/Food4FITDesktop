package br.com.food4fit.helper;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import br.com.food4fit.model.Produto;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class DoubleEditingCell extends TableCell<Produto, Double> {
    private TextField textField ;
    private TextFormatter<Double> textFormatter;	
    
    private static final Pattern DOUBLE_PATTERN = Pattern.compile("\\d*|\\d+\\,\\d*");
	private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance(new Locale("pt", "br"));

    public DoubleEditingCell() {
        textField = new TextField();
        textFormatter = new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return DOUBLE_PATTERN.matcher(change.getControlNewText()).matches() ? change : null;
		});
        
        textField.setTextFormatter(textFormatter);
        textField.addEventFilter(KeyEvent.KEY_RELEASED, e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });

        textField.setOnAction(e -> {
        	String string = textField.getText();
        	if (DOUBLE_PATTERN.matcher(string).matches()) {
                try {
                	commitEdit(NUMBER_FORMAT.parse(string).doubleValue());
                } catch (ParseException ex) {
                	commitEdit(getItem());
                }
                
            } else {
            	commitEdit(getItem());
            }
        });

        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    @Override
    protected void updateItem(Double value, boolean empty) {
        super.updateItem(value, empty);
        if (isEditing()) {
            textField.requestFocus();
            textField.selectAll();
            setGraphic(textField);
        } else {
            format(value, empty);
        }
    }

    @Override
    public void startEdit() {
        super.startEdit();
        setGraphic(textField);
        textField.requestFocus();
        textField.selectAll();
    }

    @Override
    public void commitEdit(Double newValue) {
        super.commitEdit(newValue);
        setItem(newValue);
        format(newValue, false);
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        format(getItem(), false);
    }

    private void format(Double value, boolean empty) {
    	if (value != null) {
        	setGraphic(new Label("R$ " + NUMBER_FORMAT.format(value.doubleValue())));
    	} else if (!empty) {
    		setGraphic(new Label("R$ 0,00"));
        } else {
        	setGraphic(new Label(""));
        }
    }
}