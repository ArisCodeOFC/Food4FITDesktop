package br.com.food4fit.controller;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.food4fit.Main;
import br.com.food4fit.config.RetrofitConfig;
import br.com.food4fit.helper.ColumnFormatter;
import br.com.food4fit.helper.FormHelper;
import br.com.food4fit.model.FluxoCaixa;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FluxoDeCaixaController {
	private final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	private final NumberFormat NUMBER_FORMAT = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
	private FormHelper formHelper = FormHelper.getInstance();
	private @FXML DatePicker dpDataInicial, dpDataFinal;
	private @FXML TableView<FluxoCaixa> tblFluxoCaixa;
	private @FXML TableColumn<FluxoCaixa, Date> colunaData;
	private @FXML TableColumn<FluxoCaixa, Double> colunaEntrada, colunaSaida, colunaSaldo;
	
	private @FXML void initialize() {
    	formHelper.addValidation(dpDataInicial, FormHelper.REQUIRED);
    	formHelper.addValidation(dpDataFinal, FormHelper.REQUIRED);
    	
		colunaData.setCellValueFactory(new PropertyValueFactory<>("data"));
		colunaEntrada.setCellValueFactory(new PropertyValueFactory<>("entrada"));
		colunaSaida.setCellValueFactory(new PropertyValueFactory<>("saida"));
		colunaSaldo.setCellValueFactory(new PropertyValueFactory<>("saldo"));
		
		colunaData.setCellFactory(new ColumnFormatter<>(DATE_FORMAT));
		colunaEntrada.setCellFactory(new ColumnFormatter<>(NUMBER_FORMAT));
		colunaSaida.setCellFactory(new ColumnFormatter<>(NUMBER_FORMAT));
		colunaSaldo.setCellFactory(new ColumnFormatter<>(NUMBER_FORMAT));
	}
	
	private @FXML void buscarFluxo() {
		if (formHelper.validate()) {
			Date dataInicial = Date.from(dpDataInicial.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dataInicial);
			
			Date dataFinal = Date.from(dpDataFinal.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
			Calendar calendarFinal = Calendar.getInstance();
			calendarFinal.setTime(dataFinal);
			
			if (calendarFinal.before(calendar)) {
				Main.showErrorDialog("Erro", "Erro", "A data final não pode ser superior a data inicial.", AlertType.ERROR);
			} else {
				tblFluxoCaixa.getItems().clear();
				List<FluxoCaixa> lista = new ArrayList<>();
				while (calendar.before(calendarFinal)) {
					FluxoCaixa fluxo = new FluxoCaixa();
					fluxo.setData(calendar.getTime());
					lista.add(fluxo);
					
					calendar.set(Calendar.HOUR_OF_DAY, 0);
					calendar.add(Calendar.DAY_OF_MONTH, 1);
				}
				
				new RetrofitConfig().getFluxoCaixaService().listar(dataInicial.getTime(), dataFinal.getTime()).enqueue(new Callback<FluxoCaixa[]>() {
					@Override
					public void onResponse(Call<FluxoCaixa[]> call, Response<FluxoCaixa[]> response) {
						Platform.runLater(() -> {
							if (response.code() == 500) {
								Main.showErrorDialog("Erro", "Erro ao obter informações", "Não foi possível obter o fluxo de caixa, tente novamente mais tarde.", AlertType.ERROR);
							} else {
								for (FluxoCaixa fluxo : response.body()) {
									int index = lista.indexOf(fluxo);
									if (index >= 0) {
										FluxoCaixa entry = lista.get(index);
										if (entry != null) {
											entry.setEntrada(fluxo.getEntrada());
											entry.setSaida(fluxo.getSaida());
										}
									}
								}
								
								tblFluxoCaixa.setItems(FXCollections.observableArrayList(lista));
							}
						});
					}
					
					@Override
					public void onFailure(Call<FluxoCaixa[]> call, Throwable t) {
						t.printStackTrace();
						Platform.runLater(() -> {
							Main.showErrorDialog("Erro", "Erro ao obter informações", "Não foi possível obter o fluxo de caixa, tente novamente mais tarde.", AlertType.ERROR);
						});
					}
				});
			}
		}
	}
}
