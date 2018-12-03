package br.com.food4fit.controller;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;

import br.com.food4fit.Main;
import br.com.food4fit.config.RetrofitConfig;
import br.com.food4fit.helper.ColumnFormatter;
import br.com.food4fit.model.Banco;
import br.com.food4fit.model.ConciliacaoBancaria;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConciliacaoBancariaController {
	private final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	private final NumberFormat NUMBER_FORMAT = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
	private @FXML ComboBox<Banco> comboBanco;
	private @FXML TableView<ConciliacaoBancaria> tblConciliacao;
	private @FXML TableColumn<ConciliacaoBancaria, Date> colunaData;
	private @FXML TableColumn<ConciliacaoBancaria, String> colunaTipo;
	private @FXML TableColumn<ConciliacaoBancaria, Double> colunaValor;
	private @FXML TableColumn<ConciliacaoBancaria, Pane> colunaAcoes;
	private @FXML Label lblMovimentoContabil, lblMovimentoBancario;
	private BooleanProperty refresh = new SimpleBooleanProperty();
	
	private @FXML void initialize() {
		listarBancos();
		
		DoubleBinding total = Bindings.createDoubleBinding(() ->
		tblConciliacao.getItems().stream().collect(Collectors.summingDouble(ConciliacaoBancaria::getSaldo)),
	    tblConciliacao.getItems(), refresh);
		
		lblMovimentoContabil.textProperty().bind(Bindings.createStringBinding(() -> {
			return NUMBER_FORMAT.format(total.doubleValue());
		}, total));

		DoubleBinding total2 = Bindings.createDoubleBinding(() ->
		tblConciliacao.getItems().stream().filter(c -> c.isConciliado()).collect(Collectors.summingDouble(ConciliacaoBancaria::getSaldo)),
	    tblConciliacao.getItems(), refresh);
		
		lblMovimentoBancario.textProperty().bind(Bindings.createStringBinding(() -> {
			return NUMBER_FORMAT.format(total2.doubleValue());
		}, total2));
		
		comboBanco.valueProperty().addListener(e -> listarConciliacao());
	}
    
    private void listarBancos() {
    	new RetrofitConfig().getBancoService().listar().enqueue(
    		new Callback<Banco[]>() {
				@Override
				public void onResponse(Call<Banco[]> call, Response<Banco[]> response) {
					if (response.code() == 500) {
						Main.showErrorDialog("Erro", "Erro ao obter lista de bancos", "Não foi possível obter a lista de bancos, tente novamente mais tarde.", AlertType.ERROR);
					} else {
						comboBanco.setItems(FXCollections.observableArrayList(response.body()));
					}
				}
				
				@Override
				public void onFailure(Call<Banco[]> call, Throwable t) {
					t.printStackTrace();
					Platform.runLater(() -> {
						Main.showErrorDialog("Erro", "Erro ao obter lista de bancos", "Não foi possível obter a lista de bancos, tente novamente mais tarde.", AlertType.ERROR);
					});
				}
			}
		);
    }
    
    private void listarConciliacao() {
    	new RetrofitConfig().getConciliacaoBancariaService().listar(comboBanco.getValue().getId()).enqueue(new Callback<ConciliacaoBancaria[]>() {
			@Override
			public void onResponse(Call<ConciliacaoBancaria[]> call, Response<ConciliacaoBancaria[]> response) {
				Platform.runLater(() -> {
					if (response.code() == 500) {
						Main.showErrorDialog("Erro", "Erro ao obter movimentação bancária", "Não foi possível obter a movimentação bancária, tente novamente mais tarde.", AlertType.ERROR);
					} else {
						montarTabela(response.body());
					}
				});
			}
			
			@Override
			public void onFailure(Call<ConciliacaoBancaria[]> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> {
					Main.showErrorDialog("Erro", "Erro ao obter movimentação bancária", "Não foi possível obter a movimentação bancária, tente novamente mais tarde.", AlertType.ERROR);
				});
			}
		});
    }
    
    private void montarPainel(ConciliacaoBancaria conciliacao) {
    	Image imgDesativado = new Image(Main.class.getResource("/br/com/food4fit/assets/icons/confirmar-c.png").toString());
    	Image imgAtivado = new Image(Main.class.getResource("/br/com/food4fit/assets/icons/confirmar-v.png").toString());
    	
    	ImageView imgConciliar = new ImageView(imgDesativado);
    	imgConciliar.prefHeight(15);
    	imgConciliar.prefWidth(15);
    	imgConciliar.setCursor(Cursor.HAND);
    	imgConciliar.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
    		conciliar(conciliacao);
		});
    	
    	imgConciliar.imageProperty().bind(Bindings.createObjectBinding(() -> {
    		if (conciliacao.isConciliado()) {
    			return imgAtivado;
    		}
    		
    		return imgDesativado;
    	}, refresh));

		HBox hBox = new HBox(imgConciliar);
		hBox.setSpacing(10);
		hBox.setAlignment(Pos.CENTER);
		conciliacao.setPaneOpcoes(hBox);
    }
    
    private void montarTabela(ConciliacaoBancaria[] movimentacao) {
    	for (ConciliacaoBancaria conciliacao : movimentacao) {
    		montarPainel(conciliacao);
    	}
    	
    	colunaData.setCellValueFactory(new PropertyValueFactory<>("data"));
    	colunaTipo.setCellValueFactory(new PropertyValueFactory<>("tipoExtrato"));
    	colunaValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
    	colunaAcoes.setCellValueFactory(new PropertyValueFactory<>("paneOpcoes"));
    	
    	tblConciliacao.getItems().clear();
    	tblConciliacao.getItems().addAll(FXCollections.observableArrayList(movimentacao));
    	
    	tblConciliacao.setOnKeyReleased(t -> {
    	    KeyCode key = t.getCode();
    	    if (key == KeyCode.SPACE) {
    	    	if (tblConciliacao.getSelectionModel().getSelectedItem() != null) {
    	    		conciliar(tblConciliacao.getSelectionModel().getSelectedItem());
    	    	}
    	    }
    	});
    	
		colunaData.setCellFactory(new ColumnFormatter<>(DATE_FORMAT));
		colunaValor.setCellFactory(new ColumnFormatter<>(NUMBER_FORMAT));
    }
    
    private void conciliar(ConciliacaoBancaria conciliacao) {
    	new RetrofitConfig().getConciliacaoBancariaService().conciliar(conciliacao.getTipo(), conciliacao.getId()).enqueue(new Callback<Void>() {
			@Override
			public void onResponse(Call<Void> call, Response<Void> response) {
				Platform.runLater(() -> {
					if (response.code() == 500) {
						Main.showErrorDialog("Erro", "Erro ao conciliar", "Não foi possível conciliar, tente novamente mais tarde.", AlertType.ERROR);
					} else {
						conciliacao.setConciliado(!conciliacao.isConciliado());
						refresh.set(!refresh.get());
					}
				});
			}
			
			@Override
			public void onFailure(Call<Void> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> {
					Main.showErrorDialog("Erro", "Erro ao conciliar", "Não foi possível conciliar, tente novamente mais tarde.", AlertType.ERROR);
				});
			}
		});
    }
}
