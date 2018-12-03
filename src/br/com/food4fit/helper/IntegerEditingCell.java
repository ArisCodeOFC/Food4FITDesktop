package br.com.food4fit.helper;

import java.util.function.UnaryOperator;

import br.com.food4fit.model.Produto;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class IntegerEditingCell extends TableCell<Produto, Integer> {
    private TextField textField ;
    private TextFormatter<Integer> textFormatter;	
    
    public IntegerEditingCell() {
        textField = new TextField();
        textFormatter = new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return (change.getControlNewText()).matches("\\d+") ? change : null;
		});
        
        textField.setTextFormatter(textFormatter);
        textField.addEventFilter(KeyEvent.KEY_RELEASED, e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });

        textField.setOnAction(e -> {
        	String string = textField.getText();
        	if (string.matches("\\d+")) {
                try {
                	commitEdit(Integer.parseInt(string));
                } catch (NumberFormatException ex) {
                	commitEdit(getItem());
                }
                
            } else {
            	commitEdit(getItem());
            }
        });
        
        setGraphic(textField);
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    protected void updateItem(Integer value, boolean empty) {
        super.updateItem(value, empty);
        if (isEditing()) {
            textField.requestFocus();
            textField.selectAll();
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        } else {
        	setText(!empty && value == null ? "0" : value == null ? "" : value.toString());
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
    }

    @Override
    public void startEdit() {
        super.startEdit();
        //textFormatter.setValue(getItem());
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        textField.requestFocus();
        textField.selectAll();
    }

    @Override
    public void commitEdit(Integer newValue) {
        super.commitEdit(newValue);
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }
}