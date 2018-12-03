package br.com.food4fit.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import br.com.food4fit.Main;
import br.com.food4fit.config.RetrofitConfig;
import br.com.food4fit.helper.ColumnFormatter;
import br.com.food4fit.helper.FormHelper;
import br.com.food4fit.model.BaixaPagamento;
import br.com.food4fit.model.Banco;
import br.com.food4fit.model.Pagamento;
import br.com.food4fit.model.RelatorioReceitaReceber;
import br.com.food4fit.model.RelatorioReceitaRecebida;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FaturamentoController {
	private final NumberFormat NUMBER_FORMAT = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
	private final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	
	private FormHelper formHelper = FormHelper.getInstance();
	private FormHelper formHelperRelatorio = FormHelper.getInstance();
	private @FXML TableView<Pagamento> tblPagamento;
	private @FXML TableColumn<Pagamento, Integer> colunaCodigo;
	private @FXML TableColumn<Pagamento, String> colunaNome, colunaValor, colunaNotaFiscal;
	private @FXML TableColumn<Pagamento, Date> colunaData;
	private @FXML TableColumn<Pagamento, Pane> colunaOpcoes;
	private @FXML AnchorPane anchorPane;
	private @FXML Pane paneBaixa;
	private @FXML Label lblCodigo, lblNome, lblRecebimento, lblBaixaValor, lblValor, lblNotaFiscal;
	private @FXML DatePicker txtDataPagamento;
	private @FXML ComboBox<Banco> comboBanco;
	private @FXML RadioButton rdoValorNormal, rdoValorDesconto, rdoValorJuros;
	private @FXML ToggleGroup toggleTipoValor;
	private @FXML TextField txtJuros;
	private @FXML DatePicker dpDataInicial, dpDataFinal;
	private Pane paneBackground;
	private FileChooser fileChooser;
	private BooleanProperty refresh = new SimpleBooleanProperty();
	
	private @FXML void initialize() {
		paneBaixa.setVisible(false);
		
		paneBackground = new Pane();
		paneBackground.setPrefWidth(anchorPane.getPrefWidth());
		paneBackground.setPrefHeight(anchorPane.getPrefHeight());
		paneBackground.setLayoutX(0);
		paneBackground.setLayoutY(0);
		paneBackground.setVisible(false);
		paneBackground.managedProperty().bind(paneBackground.visibleProperty());
		paneBackground.setBackground(new Background(new BackgroundFill(new Color(0, 0, 0, 0.5), CornerRadii.EMPTY, Insets.EMPTY)));
		anchorPane.getChildren().add(paneBackground);
		
		formHelper.addValidation(toggleTipoValor, FormHelper.REQUIRED);
		formHelper.addValidation(comboBanco, FormHelper.REQUIRED);
		formHelper.addValidation(txtDataPagamento, FormHelper.REQUIRED);
		formHelper.addValidation(txtJuros, FormHelper.REQUIRED | FormHelper.VALID_DOUBLE | FormHelper.VISIBLE_ONLY);

    	formHelperRelatorio.addValidation(dpDataInicial, FormHelper.REQUIRED);
    	formHelperRelatorio.addValidation(dpDataFinal, FormHelper.REQUIRED);
    	
		lblValor.visibleProperty().bind(rdoValorJuros.selectedProperty().or(rdoValorDesconto.selectedProperty()));
		txtJuros.visibleProperty().bind(rdoValorJuros.selectedProperty().or(rdoValorDesconto.selectedProperty()));
		lblValor.textProperty().bind(Bindings.createStringBinding(() -> {
			return rdoValorDesconto.isSelected() ? "Valor do desconto (reais):" : "Valor do juros (reais):";
		}, rdoValorDesconto.selectedProperty(), rdoValorJuros.selectedProperty()));
		
		listarPagamentos();
		listarBancos();
		
		fileChooser = new FileChooser();
		fileChooser.setTitle("Salvar Relatório");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Documentos PDF", "*.pdf"));
		
		colunaCodigo.setMaxWidth(1f * Integer.MAX_VALUE * 10);
		colunaNotaFiscal.setMaxWidth(1f * Integer.MAX_VALUE * 10);
		colunaNome.setMaxWidth(1f * Integer.MAX_VALUE * 35);
		colunaValor.setMaxWidth(1f * Integer.MAX_VALUE * 15);
		colunaData.setMaxWidth(1f * Integer.MAX_VALUE * 15);
		colunaOpcoes.setMaxWidth(1f * Integer.MAX_VALUE * 10);
	}
	
	private void listarPagamentos() {
		new RetrofitConfig().getPagamentoService().listar().enqueue(new Callback<Pagamento[]>() {
			@Override
			public void onResponse(Call<Pagamento[]> call, Response<Pagamento[]> response) {
				if (response.code() == 500) {
					Platform.runLater(() -> 
						Main.showErrorDialog("Erro", "Erro ao obter lista de pagamentos", "Não foi possível obter a lista de pagamentos, tente novamente mais tarde.", AlertType.ERROR)
					);
					
				} else {
					montarTabela(response.body());
				}
			}
			
			@Override
			public void onFailure(Call<Pagamento[]> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> 
					Main.showErrorDialog("Erro", "Erro ao obter lista de pagamentos", "Não foi possível obter a lista de pagamentos, tente novamente mais tarde.", AlertType.ERROR)
				);
			}
		});
	}
	
	private void montarPainel(Pagamento pagamento) {
		Image imgDesativado = new Image(Main.class.getResource("/br/com/food4fit/assets/icons/confirmar-c.png").toString());
    	Image imgAtivado = new Image(Main.class.getResource("/br/com/food4fit/assets/icons/confirmar-v.png").toString());
    	
		ImageView imgRealizarBaixa = new ImageView(imgDesativado);
		imgRealizarBaixa.prefHeight(15);
		imgRealizarBaixa.prefWidth(15);
		imgRealizarBaixa.setCursor(Cursor.HAND);
		imgRealizarBaixa.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			abrirPainelBaixa(pagamento);
		});
    	
		imgRealizarBaixa.imageProperty().bind(Bindings.createObjectBinding(() -> {
    		if (pagamento.isBaixa()) {
    			return imgAtivado;
    		}
    		
    		return imgDesativado;
    	}, refresh, tblPagamento.itemsProperty()));

		HBox hBox = new HBox(imgRealizarBaixa);
		hBox.setSpacing(10);
		hBox.setAlignment(Pos.CENTER);
		pagamento.setPaneOpcoes(hBox);
	}
	
	private void montarTabela(Pagamento[] pagamentos) {
		for (Pagamento pagamento : pagamentos) {
			montarPainel(pagamento);
		}

		colunaCodigo.setCellValueFactory(new PropertyValueFactory<>("id"));
		colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		colunaData.setCellValueFactory(new PropertyValueFactory<>("data"));
		colunaOpcoes.setCellValueFactory(new PropertyValueFactory<>("paneOpcoes"));
		colunaValor.setCellValueFactory(new PropertyValueFactory<>("valorFormatado"));
		colunaNotaFiscal.setCellValueFactory(new PropertyValueFactory<>("notaFiscal"));
		tblPagamento.setItems(FXCollections.observableArrayList(pagamentos));
		colunaData.setCellFactory(new ColumnFormatter<>(DATE_FORMAT));
	}
	
	private @FXML void fecharBaixa() {
		paneBaixa.setVisible(false);
		paneBackground.setVisible(false);
		formHelper.setObjectData(null);
		txtDataPagamento.setValue(null);
		comboBanco.setValue(null);
		txtJuros.clear();
		rdoValorNormal.setSelected(true);
	}
	
	private @FXML void salvarBaixa() {
		if (formHelper.validate()) {
			BaixaPagamento baixa = new BaixaPagamento();
			baixa.setBanco(comboBanco.getValue());
			baixa.setDataPagamento(Date.from(txtDataPagamento.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			if (rdoValorNormal.isSelected()) {
				baixa.setDesconto(0);
				baixa.setJuros(0);
			} else if (rdoValorJuros.isSelected()) {
				baixa.setDesconto(0);
				try {
					baixa.setJuros(NUMBER_FORMAT.parse(txtJuros.getText()).doubleValue());
				} catch (Exception e) {}
			} else if (rdoValorDesconto.isSelected()) {
				baixa.setJuros(0);
				try {
					baixa.setJuros(NUMBER_FORMAT.parse(txtJuros.getText()).doubleValue());
				} catch (Exception e) {}
			}
			
			Pagamento pagamento = (Pagamento) formHelper.getObjectData();
			new RetrofitConfig().getPagamentoService().darBaixa(pagamento.getId(), baixa).enqueue(new Callback<Void>() {
				@Override
				public void onResponse(Call<Void> call, Response<Void> response) {
					Platform.runLater(() -> {
						if (response.code() == 500) {
							Main.showErrorDialog("Erro", "Erro ao dar baixa", "Não foi possível realizar a baixa, tente novamente mais tarde.", AlertType.ERROR);
						} else {
							pagamento.setBaixa(true);
							refresh.set(!refresh.get());
							Main.showInfDialog("Sucesso", "", "Baixa realizada com sucesso!!!");
							fecharBaixa();
						}
					});
				}
				
				@Override
				public void onFailure(Call<Void> call, Throwable t) {
					t.printStackTrace();
					Platform.runLater(() -> {
						Main.showErrorDialog("Erro", "Erro ao dar baixa", "Não foi possível realizar a baixa, tente novamente mais tarde.", AlertType.ERROR);
					});
				}
			});
		}
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
	
	private void abrirPainelBaixa(Pagamento pagamento) {
		if (pagamento.isBaixa()) {
			Main.showErrorDialog("Erro", "Proibido", "Você não pode modificar ou excluir uma receita em que já foi realizado baixa.", AlertType.ERROR);
			return;
		}
		
		lblCodigo.setText(String.valueOf(pagamento.getId()));
		lblRecebimento.setText(new SimpleDateFormat("dd/MM/yyyy").format(pagamento.getData()));
		lblNome.setText(pagamento.getNome());
		lblBaixaValor.setText(pagamento.getValorFormatado());
		lblNotaFiscal.setText(pagamento.getNotaFiscal());
		paneBackground.setVisible(true);
		paneBaixa.setVisible(true);
		paneBaixa.toFront();
		formHelper.setObjectData(pagamento);
	}
	
	private @FXML void emitirRelatorio() {
		if (formHelperRelatorio.validate()) {
			Date dataInicial = Date.from(dpDataInicial.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dataInicial);
			
			Date dataFinal = Date.from(dpDataFinal.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
			Calendar calendarFinal = Calendar.getInstance();
			calendarFinal.setTime(dataFinal);
			
			if (calendarFinal.before(calendar)) {
				Main.showErrorDialog("Erro", "Erro", "A data final não pode ser superior a data inicial.", AlertType.ERROR);
			} else {
				obterRelatorioReceber(dataInicial.getTime(), dataFinal.getTime());
			}
		}
	}
	
	private void obterRelatorioReceber(long dataInicial, long dataFinal) {
		new RetrofitConfig().getRelatorioService().listarReceitasReceber(dataInicial, dataFinal).enqueue(new Callback<RelatorioReceitaReceber[]>() {
			@Override
			public void onResponse(Call<RelatorioReceitaReceber[]> call, Response<RelatorioReceitaReceber[]> response) {
				Platform.runLater(() -> {
					if (response.code() == 500) {
						Main.showErrorDialog("Erro", "Erro ao obter relatório", "Não foi possível obter o relatório, tente novamente mais tarde.", AlertType.ERROR);
					} else {
						obterRelatorioRecebidas(dataInicial, dataFinal, response.body());
					}
				});
			}
			
			@Override
			public void onFailure(Call<RelatorioReceitaReceber[]> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> {
					Main.showErrorDialog("Erro", "Erro ao obter relatório", "Não foi possível obter o relatório, tente novamente mais tarde.", AlertType.ERROR);
				});
			}
		});
	}
	
	private void obterRelatorioRecebidas(long dataInicial, long dataFinal, RelatorioReceitaReceber[] receber) {
		new RetrofitConfig().getRelatorioService().listarReceitasRecebidas(dataInicial, dataFinal).enqueue(new Callback<RelatorioReceitaRecebida[]>() {
			@Override
			public void onResponse(Call<RelatorioReceitaRecebida[]> call, Response<RelatorioReceitaRecebida[]> response) {
				Platform.runLater(() -> {
					if (response.code() == 500) {
						Main.showErrorDialog("Erro", "Erro ao obter relatório", "Não foi possível obter o relatório, tente novamente mais tarde.", AlertType.ERROR);
					} else {
						montarRelatorio(receber, response.body());
					}
				});
			}
			
			@Override
			public void onFailure(Call<RelatorioReceitaRecebida[]> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> {
					Main.showErrorDialog("Erro", "Erro ao obter relatório", "Não foi possível obter o relatório, tente novamente mais tarde.", AlertType.ERROR);
				});
			}
		});
	}
	
	private void montarRelatorio(RelatorioReceitaReceber[] receber, RelatorioReceitaRecebida[] recebidas) {
		try {
			File file = fileChooser.showSaveDialog(anchorPane.getScene().getWindow());
			if (file != null) {
	            Document document = new Document();
	            PdfWriter.getInstance(document, new FileOutputStream(file));
	            document.open();
	            
	            document.addTitle("Relatório");
	        	document.addSubject("Relatório de Duplicatas");
	            document.addAuthor("Food4FIT");
	
	            Paragraph titulo = new Paragraph();
	            titulo.add(new Paragraph("Duplicatas a receber", new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD)));
	            titulo.add(new Paragraph(" "));
	            titulo.add(new Paragraph(" "));
	            document.add(titulo);
	            
	            PdfPTable tabela = new PdfPTable(4);
	
	            PdfPCell coluna1 = new PdfPCell(new Phrase("Código"));
	            coluna1.setHorizontalAlignment(Element.ALIGN_CENTER);
	            tabela.addCell(coluna1);
	            
	            PdfPCell coluna2 = new PdfPCell(new Phrase("NF"));
	            coluna2.setHorizontalAlignment(Element.ALIGN_CENTER);
	            tabela.addCell(coluna2);
	            
	            PdfPCell coluna3 = new PdfPCell(new Phrase("Data"));
	            coluna3.setHorizontalAlignment(Element.ALIGN_CENTER);
	            tabela.addCell(coluna3);
	            
	            PdfPCell coluna4 = new PdfPCell(new Phrase("Valor"));
	            coluna4.setHorizontalAlignment(Element.ALIGN_CENTER);
	            tabela.addCell(coluna4);
	            
	            for (RelatorioReceitaReceber relatorio : receber) {
	            	tabela.addCell(String.valueOf(relatorio.getId()));
	            	tabela.addCell(relatorio.getNotaFiscal());
	            	tabela.addCell(DATE_FORMAT.format(relatorio.getData()));
	            	tabela.addCell(NUMBER_FORMAT.format(relatorio.getValor()));
	            }
	            
	            document.add(tabela);
	            document.newPage();
	
	            Paragraph titulo2 = new Paragraph();
	            titulo2.add(new Paragraph("Duplicatas recebidas", new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD)));
	            titulo2.add(new Paragraph(" "));
	            titulo2.add(new Paragraph(" "));
	            document.add(titulo2);
	            
	            PdfPTable tabela2 = new PdfPTable(5);
	
	            PdfPCell coluna5 = new PdfPCell(new Phrase("Código"));
	            coluna5.setHorizontalAlignment(Element.ALIGN_CENTER);
	            tabela2.addCell(coluna5);
	            
	            PdfPCell coluna6 = new PdfPCell(new Phrase("NF"));
	            coluna6.setHorizontalAlignment(Element.ALIGN_CENTER);
	            tabela2.addCell(coluna6);

	            PdfPCell coluna7 = new PdfPCell(new Phrase("Data"));
	            coluna7.setHorizontalAlignment(Element.ALIGN_CENTER);
	            tabela2.addCell(coluna7);
	            
	            PdfPCell coluna8 = new PdfPCell(new Phrase("Baixa"));
	            coluna8.setHorizontalAlignment(Element.ALIGN_CENTER);
	            tabela2.addCell(coluna8);

	            PdfPCell coluna9 = new PdfPCell(new Phrase("Valor"));
	            coluna9.setHorizontalAlignment(Element.ALIGN_CENTER);
	            tabela2.addCell(coluna9);
	            
	            for (RelatorioReceitaRecebida relatorio : recebidas) {
	            	tabela2.addCell(String.valueOf(relatorio.getId()));
	            	tabela2.addCell(relatorio.getNotaFiscal());
	            	tabela2.addCell(DATE_FORMAT.format(relatorio.getData()));
	            	tabela2.addCell(DATE_FORMAT.format(relatorio.getDataBaixa()));
	            	tabela2.addCell(NUMBER_FORMAT.format(relatorio.getValor()));
	            }
	            
	            document.add(tabela2);
	            document.close();
	            
	            Main.showInfDialog("Sucesso", "", "O relatório foi salvo com sucesso!");
			}
			
        } catch (Exception e) {
        	Main.showErrorDialog("Erro", "Erro ao obter relatório", "Não foi possível obter o relatório, tente novamente mais tarde.", AlertType.ERROR);
        }
	}
}
