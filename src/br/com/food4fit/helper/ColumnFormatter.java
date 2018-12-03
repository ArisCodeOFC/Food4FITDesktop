package br.com.food4fit.helper;

import java.text.Format;

import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class ColumnFormatter<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {
    private Format format;

    public ColumnFormatter(Format format) {
        super();
        this.format = format;
    }
    
    @Override
    public TableCell<S, T> call(TableColumn<S, T> column) {
        return new TableCell<S, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new Label(format.format(item)));
                }
            }
        };
    }
}