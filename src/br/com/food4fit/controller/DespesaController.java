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
import br.com.food4fit.model.BaixaDespesa;
import br.com.food4fit.model.Banco;
import br.com.food4fit.model.Despesa;
import br.com.food4fit.model.Fornecedor;
import br.com.food4fit.model.Funcionario;
import br.com.food4fit.model.RelatorioDespesaPaga;
import br.com.food4fit.model.RelatorioDespesaVencida;
import br.com.food4fit.model.TipoPagamento;
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
import javafx.scene.control.TextArea;
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

public class DespesaController {
	private final NumberFormat NUMBER_FORMAT = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
	private final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	private final NumberFormat DECIMAL_FORMAT = NumberFormat.getInstance(new Locale("pt", "br"));
	
	private FormHelper formHelper = FormHelper.getInstance();
	private FormHelper formHelperBaixa = FormHelper.getInstance();
	private FormHelper formHelperRelatorio = FormHelper.getInstance();
	private @FXML Pane paneConteudo, paneBaixa;
	private @FXML RadioButton rdoFuncionario, rdoFornecedor, rdoValorNormal, rdoValorDesconto, rdoValorJuros;
	private @FXML ComboBox<Funcionario> comboFuncionario;
	private @FXML ComboBox<Fornecedor> comboFornecedor;
	private @FXML ComboBox<TipoPagamento> comboFormaPagamento;
	private @FXML ComboBox<Banco> comboBanco;
	private @FXML TableView<Despesa> tblDespesa;
	private @FXML TableColumn<Despesa, String> colunaTipoDespesa, colunaNome, colunaValor;
	private @FXML TableColumn<Despesa, Date>  colunaDataEmissao, colunaDataVencimento;
	private @FXML TableColumn<Despesa, Pane> colunaOpcoes;
	private @FXML ToggleGroup toggleTipo;
	private @FXML DatePicker txtDataEmissao, txtDataVencimento, txtDataPagamento;
	private @FXML TextField txtValor, txtJuros;
	private @FXML AnchorPane anchorPane;
	private @FXML TextArea txtDetalhes;
	private @FXML ToggleGroup toggleTipoValor;
	private @FXML Label lblBaixaCodigo, lblBaixaTipo, lblBaixaNome, lblBaixaEmissao, lblBaixaVencimento, lblBaixaValor, lblBaixaDescricao, lblValor;
	private @FXML DatePicker dpDataInicial, dpDataFinal;
	private BooleanProperty refresh = new SimpleBooleanProperty();
	private Pane paneBackground;
	private FileChooser fileChooser;

    private @FXML void initialize() {
    	paneBaixa.setVisible(false);
		paneConteudo.setVisible(false);
		
		paneBackground = new Pane();
		paneBackground.setPrefWidth(anchorPane.getPrefWidth());
		paneBackground.setPrefHeight(anchorPane.getPrefHeight());
		paneBackground.setLayoutX(0);
		paneBackground.setLayoutY(0);
		paneBackground.setVisible(false);
		paneBackground.managedProperty().bind(paneBackground.visibleProperty());
		paneBackground.setBackground(new Background(new BackgroundFill(new Color(0, 0, 0, 0.5), CornerRadii.EMPTY, Insets.EMPTY)));
		anchorPane.getChildren().add(paneBackground);
		
		formHelper.addValidation(toggleTipo, FormHelper.REQUIRED);
		formHelper.addValidation(comboFornecedor, FormHelper.REQUIRED | FormHelper.VISIBLE_ONLY);
		formHelper.addValidation(comboFuncionario, FormHelper.REQUIRED | FormHelper.VISIBLE_ONLY);
		formHelper.addValidation(txtDataEmissao, FormHelper.REQUIRED);
		formHelper.addValidation(txtDataVencimento, FormHelper.REQUIRED);
		formHelper.addValidation(txtValor, FormHelper.REQUIRED | FormHelper.VALID_DOUBLE);
		formHelper.addValidation(txtDetalhes, FormHelper.REQUIRED);
		
		formHelperBaixa.addValidation(toggleTipoValor, FormHelper.REQUIRED);
		formHelperBaixa.addValidation(comboBanco, FormHelper.REQUIRED);
		formHelperBaixa.addValidation(comboFormaPagamento, FormHelper.REQUIRED);
		formHelperBaixa.addValidation(txtDataPagamento, FormHelper.REQUIRED);
		formHelperBaixa.addValidation(txtJuros, FormHelper.REQUIRED | FormHelper.VALID_DOUBLE | FormHelper.VISIBLE_ONLY);

    	formHelperRelatorio.addValidation(dpDataInicial, FormHelper.REQUIRED);
    	formHelperRelatorio.addValidation(dpDataFinal, FormHelper.REQUIRED);
    	
		comboFornecedor.managedProperty().bind(comboFornecedor.visibleProperty());
		comboFornecedor.visibleProperty().bind(rdoFornecedor.selectedProperty());
		comboFuncionario.managedProperty().bind(comboFuncionario.visibleProperty());
		comboFuncionario.visibleProperty().bind(rdoFuncionario.selectedProperty());
		
		lblValor.visibleProperty().bind(rdoValorJuros.selectedProperty().or(rdoValorDesconto.selectedProperty()));
		txtJuros.visibleProperty().bind(rdoValorJuros.selectedProperty().or(rdoValorDesconto.selectedProperty()));
		lblValor.textProperty().bind(Bindings.createStringBinding(() -> {
			return rdoValorDesconto.isSelected() ? "Valor do desconto (reais):" : "Valor do juros (reais):";
		}, rdoValorDesconto.selectedProperty(), rdoValorJuros.selectedProperty()));
		
		fileChooser = new FileChooser();
		fileChooser.setTitle("Salvar Relatório");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Documentos PDF", "*.pdf"));
		
		listarFornecedores();
		listarFuncionarios();
		listarDespesas();
		listarTiposPagamento();
		listarBancos();
    }

	private @FXML void salvar() {
		if (formHelper.validate()) {
			Despesa despesa;
			if (formHelper.getObjectData() != null) {
				despesa = (Despesa) formHelper.getObjectData();
			} else {
				despesa = new Despesa();
			}
			
			if (despesa.isBaixa()) {
				Main.showErrorDialog("Erro", "Proibido", "Você não pode modificar ou excluir uma despesa em que já foi realizado baixa.", AlertType.ERROR);
				return;
			}
			
			despesa.setDataEmissao(Date.from(txtDataEmissao.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			despesa.setDataVencimento(Date.from(txtDataVencimento.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			
			try {
				despesa.setValor(DECIMAL_FORMAT.parse(txtValor.getText()).doubleValue());
			} catch (Exception e) {}
			
			despesa.setDescricao(txtDetalhes.getText());
			
			if (rdoFornecedor.isSelected()) {
				despesa.setFuncionario(null);
				despesa.setFornecedor(comboFornecedor.getValue());
			} else {
				despesa.setFornecedor(null);
				despesa.setFuncionario(comboFuncionario.getValue());
			}
			
			if (formHelper.getObjectData() == null) {
				new RetrofitConfig().getDespesaService().inserir(despesa).enqueue(new Callback<Despesa>() {
					@Override
					public void onResponse(Call<Despesa> call, Response<Despesa> response) {
						Platform.runLater(() -> {
							if (response.code() == 500) {
								Main.showErrorDialog("Erro", "Erro ao inserir despesa", "Não foi possível inserir a despesa, tente novamente mais tarde.", AlertType.ERROR);
							} else {
								montarPainel(response.body());
								tblDespesa.getItems().add(response.body());
								Main.showInfDialog("Sucesso", "", "Despesa cadastrada com sucesso!!!");
								fecharConteudo();
							}
						});
					}
					
					@Override
					public void onFailure(Call<Despesa> call, Throwable t) {
						t.printStackTrace();
						Platform.runLater(() -> 
							Main.showErrorDialog("Erro", "Erro ao inserir despesa", "Não foi possível inserir a despesa, tente novamente mais tarde.", AlertType.ERROR)
						);
					}
				});
				
			} else {
				new RetrofitConfig().getDespesaService().atualizar(despesa.getId(), despesa).enqueue(new Callback<Void>() {
					@Override
					public void onResponse(Call<Void> call, Response<Void> response) {
						Platform.runLater(() -> {
							if (response.code() == 500) {
								Main.showErrorDialog("Erro", "Erro ao atualizar despesa", "Não foi possível atualizar a despesa, tente novamente mais tarde.", AlertType.ERROR);
							} else {
								formHelper.setObjectData(null);
								tblDespesa.refresh();
								Main.showInfDialog("Sucesso", "", "Despesa atualizada com sucesso!!!");
								fecharConteudo();
							}
						});
					}
					
					@Override
					public void onFailure(Call<Void> call, Throwable t) {
						t.printStackTrace();
						Platform.runLater(() -> 
							Main.showErrorDialog("Erro", "Erro ao atualizar despesa", "Não foi possível atualizar a despesa, tente novamente mais tarde.", AlertType.ERROR)
						);
					}
				});
			}
		}
	}

	private @FXML void abrirConteudo() {
		paneConteudo.setVisible(true);
		paneBackground.setVisible(true);
		paneConteudo.toFront();
	}

	private @FXML void fecharConteudo() {
		formHelper.setObjectData(null);
		paneConteudo.setVisible(false);
		paneBackground.setVisible(false);
		rdoFuncionario.setSelected(true);
		comboFornecedor.setValue(null);
		comboFuncionario.setValue(null);
		txtDataEmissao.setValue(null);
		txtDataVencimento.setValue(null);
		txtDetalhes.clear();
		txtValor.clear();
	}
	
	private void listarFornecedores() {
		new RetrofitConfig().getFornecedorService().listar().enqueue(new Callback<Fornecedor[]>() {
			@Override
			public void onResponse(Call<Fornecedor[]> call, Response<Fornecedor[]> response) {
				if (response.code() == 500) {
					Platform.runLater(() -> 
						Main.showErrorDialog("Erro", "Erro ao obter lista de fornecedores", "Não foi possível obter a lista de fornecedores, tente novamente mais tarde.", AlertType.ERROR)
					);
					
				} else {
					comboFornecedor.setItems(FXCollections.observableArrayList(response.body()));
				}
			}
			
			@Override
			public void onFailure(Call<Fornecedor[]> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> 
					Main.showErrorDialog("Erro", "Erro ao obter lista de fornecedores", "Não foi possível obter a lista de fornecedores, tente novamente mais tarde.", AlertType.ERROR)
				);
			}
		});
	}

	private void listarFuncionarios() {
		Call<Funcionario[]> retorno = new RetrofitConfig().getFuncionarioService().lista();
		retorno.enqueue(new Callback<Funcionario[]>() {
			@Override
			public void onResponse(Call<Funcionario[]> call, Response<Funcionario[]> response) {
				if (response.code() == 500) {
					Platform.runLater(() -> 
						Main.showErrorDialog("Erro", "Erro ao obter lista de funcionários", "Não foi possível obter a lista de funcionários, tente novamente mais tarde.", AlertType.ERROR)
					);
					
				} else {
					comboFuncionario.setItems(FXCollections.observableArrayList(response.body()));
				}
			}
			
			@Override
			public void onFailure(Call<Funcionario[]> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> 
					Main.showErrorDialog("Erro", "Erro ao obter lista de funcionários", "Não foi possível obter a lista de funcionários, tente novamente mais tarde.", AlertType.ERROR)
				);
			}
		});
	}
	
	private void listarDespesas() {
		new RetrofitConfig().getDespesaService().listar().enqueue(new Callback<Despesa[]>() {
			@Override
			public void onResponse(Call<Despesa[]> call, Response<Despesa[]> response) {
				Platform.runLater(() -> {
					if (response.code() == 500) {
						Main.showErrorDialog("Erro", "Erro ao obter lista de despesas", "Não foi possível obter a lista de despesas, tente novamente mais tarde.", AlertType.ERROR);
					} else {
						montarTabela(response.body());
					}
				});
			}
			
			@Override
			public void onFailure(Call<Despesa[]> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> 
					Main.showErrorDialog("Erro", "Erro ao obter lista de despesas", "Não foi possível obter a lista de despesas, tente novamente mais tarde.", AlertType.ERROR)
				);
			}
		});
	}
	
	private void listarTiposPagamento() {
		new RetrofitConfig().getTipoPagamentoService().listar().enqueue(new Callback<TipoPagamento[]>() {
			@Override
			public void onResponse(Call<TipoPagamento[]> call, Response<TipoPagamento[]> response) {
				Platform.runLater(() -> {
					if (response.code() == 500) {
						Main.showErrorDialog("Erro", "Erro ao obter lista de tipos de pagamento", "Não foi possível obter a lista de tipos de pagamento, tente novamente mais tarde.", AlertType.ERROR);
					} else {
						comboFormaPagamento.setItems(FXCollections.observableArrayList(response.body()));
					}
				});
			}
			
			@Override
			public void onFailure(Call<TipoPagamento[]> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> 
					Main.showErrorDialog("Erro", "Erro ao obter lista de tipos de pagamento", "Não foi possível obter a lista de tipos de pagamento, tente novamente mais tarde.", AlertType.ERROR)
				);
			}
		});
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
	
	private void montarPainel(Despesa despesa) {
		Image imgDesativado = new Image(Main.class.getResource("/br/com/food4fit/assets/icons/confirmar-c.png").toString());
    	Image imgAtivado = new Image(Main.class.getResource("/br/com/food4fit/assets/icons/confirmar-v.png").toString());
    	
		ImageView imgRealizarBaixa = new ImageView(imgDesativado);
		imgRealizarBaixa.prefHeight(15);
		imgRealizarBaixa.prefWidth(15);
		imgRealizarBaixa.setCursor(Cursor.HAND);
		imgRealizarBaixa.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			abrirPainelBaixa(despesa);
		});
    	
		imgRealizarBaixa.imageProperty().bind(Bindings.createObjectBinding(() -> {
    		if (despesa.isBaixa()) {
    			return imgAtivado;
    		}
    		
    		return imgDesativado;
    	}, refresh, tblDespesa.itemsProperty()));
		
		ImageView imgEditar = new ImageView(new Image(Main.class.getResource("/br/com/food4fit/assets/icons/editar-c.png").toString()));
		imgEditar.prefHeight(15);
		imgEditar.prefWidth(15);
		imgEditar.setCursor(Cursor.HAND);
		imgEditar.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			editarDespesa(despesa);
		});
		
		ImageView imgExcluir = new ImageView(new Image(Main.class.getResource("/br/com/food4fit/assets/icons/cancelar-c.png").toString()));
		imgExcluir.prefHeight(15);
		imgExcluir.prefWidth(15);
		imgExcluir.setCursor(Cursor.HAND);
		imgExcluir.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			excluirDespesa(despesa);
		});
		
		HBox hBox = new HBox(imgRealizarBaixa, imgEditar, imgExcluir);
		hBox.setSpacing(10);
		hBox.setAlignment(Pos.CENTER);
		despesa.setPaneOpcoes(hBox);
	}
	
	private void montarTabela(Despesa[] despesas) {
		for (Despesa despesa : despesas) {
			montarPainel(despesa);
		}

		colunaTipoDespesa.setCellValueFactory(new PropertyValueFactory<Despesa, String>("tipo"));
		colunaNome.setCellValueFactory(new PropertyValueFactory<Despesa, String>("nome"));
		colunaDataEmissao.setCellValueFactory(new PropertyValueFactory<Despesa, Date>("dataEmissao"));
		colunaDataVencimento.setCellValueFactory(new PropertyValueFactory<Despesa, Date>("dataVencimento"));
		colunaValor.setCellValueFactory(new PropertyValueFactory<Despesa, String>("valorFormatado"));
		colunaOpcoes.setCellValueFactory(new PropertyValueFactory<Despesa, Pane>("paneOpcoes"));
		tblDespesa.setItems(FXCollections.observableArrayList(despesas));

		colunaDataEmissao.setCellFactory(new ColumnFormatter<>(DATE_FORMAT));
		colunaDataVencimento.setCellFactory(new ColumnFormatter<>(DATE_FORMAT));
	}
	
	private void excluirDespesa(Despesa despesa) {
		if (despesa.isBaixa()) {
			Main.showErrorDialog("Erro", "Proibido", "Você não pode modificar ou excluir uma despesa em que já foi realizado baixa.", AlertType.ERROR);
			return;
		}
		
		int resposta = Main.showConfirmDialog("Excluir", "Excluir", "Deseja realmente excluir esta despesa?", AlertType.CONFIRMATION);
		if (resposta == 1) {
			new RetrofitConfig().getDespesaService().excluir(despesa.getId()).enqueue(new Callback<Void>() {
				@Override
				public void onResponse(Call<Void> call, Response<Void> response) {
					Platform.runLater(() -> {
						if (response.code() == 500) {
							Main.showErrorDialog("Erro", "Erro ao excluir despesa", "Não foi possível excluir a despesa, tente novamente mais tarde.", AlertType.ERROR);
						} else {
							tblDespesa.getItems().remove(despesa);
							Main.showInfDialog("Sucesso", "", "Despesa excluída com sucesso!!!");
						}
					});
				}
				
				@Override
				public void onFailure(Call<Void> call, Throwable t) {
					t.printStackTrace();
					Platform.runLater(() -> {
						Main.showErrorDialog("Erro", "Erro ao excluir despesa", "Não foi possível excluir a despesa, tente novamente mais tarde.", AlertType.ERROR);
					});
				}
			});
		}
	}
	
	private void editarDespesa(Despesa despesa) {
		formHelper.setObjectData(despesa);
		
		txtDataEmissao.setValue(despesa.getDataEmissao().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		txtDataVencimento.setValue(despesa.getDataVencimento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		txtValor.setText(DECIMAL_FORMAT.format(despesa.getValor()));
		txtDetalhes.setText(despesa.getDescricao());
		if (despesa.getFuncionario() != null) {
			comboFuncionario.setValue(despesa.getFuncionario());
			rdoFuncionario.setSelected(true);
		} else {
			comboFornecedor.setValue(despesa.getFornecedor());
			rdoFornecedor.setSelected(true);
		}
		
		abrirConteudo();
	}
	
	private void abrirPainelBaixa(Despesa despesa) {
		if (despesa.isBaixa()) {
			Main.showErrorDialog("Erro", "Proibido", "Você não pode modificar ou excluir uma despesa em que já foi realizado baixa.", AlertType.ERROR);
			return;
		}
		
		lblBaixaCodigo.setText(String.valueOf(despesa.getId()));
		lblBaixaEmissao.setText(despesa.getDataEmissaoFormatada());
		lblBaixaVencimento.setText(despesa.getDataVencimentoFormatada());
		lblBaixaNome.setText(despesa.getNome());
		lblBaixaTipo.setText(despesa.getTipo());
		lblBaixaValor.setText(despesa.getValorFormatado());
		lblBaixaDescricao.setText(despesa.getDescricao());
		paneBackground.setVisible(true);
		paneBaixa.setVisible(true);
		paneBaixa.toFront();
		formHelperBaixa.setObjectData(despesa);
	}
	
	private @FXML void fecharBaixa() {
		paneBaixa.setVisible(false);
		paneBackground.setVisible(false);
		formHelperBaixa.setObjectData(null);
		txtDataPagamento.setValue(null);
		comboBanco.setValue(null);
		comboFormaPagamento.setValue(null);
		txtJuros.clear();
		rdoValorNormal.setSelected(true);
	}
	
	private @FXML void salvarBaixa() {
		if (formHelperBaixa.validate()) {
			BaixaDespesa baixa = new BaixaDespesa();
			baixa.setBanco(comboBanco.getValue());
			baixa.setDataPagamento(Date.from(txtDataPagamento.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			baixa.setTipoPagamento(comboFormaPagamento.getValue());
			if (rdoValorNormal.isSelected()) {
				baixa.setDesconto(0);
				baixa.setJuros(0);
			} else if (rdoValorJuros.isSelected()) {
				baixa.setDesconto(0);
				try {
					baixa.setJuros(DECIMAL_FORMAT.parse(txtJuros.getText()).doubleValue());
				} catch (Exception e) {}
			} else if (rdoValorDesconto.isSelected()) {
				baixa.setJuros(0);
				try {
					baixa.setJuros(DECIMAL_FORMAT.parse(txtJuros.getText()).doubleValue());
				} catch (Exception e) {}
			}
			
			Despesa despesa = (Despesa) formHelperBaixa.getObjectData();
			new RetrofitConfig().getDespesaService().darBaixa(despesa.getId(), baixa).enqueue(new Callback<Void>() {
				@Override
				public void onResponse(Call<Void> call, Response<Void> response) {
					Platform.runLater(() -> {
						if (response.code() == 500) {
							Main.showErrorDialog("Erro", "Erro ao dar baixa", "Não foi possível realizar a baixa, tente novamente mais tarde.", AlertType.ERROR);
						} else {
							despesa.setBaixa(true);
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
				obterRelatorioVencidas(dataInicial.getTime(), dataFinal.getTime());
			}
		}
	}
	
	private void obterRelatorioVencidas(long dataInicial, long dataFinal) {
		new RetrofitConfig().getRelatorioService().listarDespesasVencidas(dataInicial, dataFinal).enqueue(new Callback<RelatorioDespesaVencida[]>() {	
			@Override
			public void onResponse(Call<RelatorioDespesaVencida[]> call, Response<RelatorioDespesaVencida[]> response) {
				Platform.runLater(() -> {
					if (response.code() == 500) {
						Main.showErrorDialog("Erro", "Erro ao obter relatório", "Não foi possível obter o relatório, tente novamente mais tarde.", AlertType.ERROR);
					} else {
						obterRelatorioPagas(dataInicial, dataFinal, response.body());
					}
				});
			}
			
			@Override
			public void onFailure(Call<RelatorioDespesaVencida[]> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> {
					Main.showErrorDialog("Erro", "Erro ao obter relatório", "Não foi possível obter o relatório, tente novamente mais tarde.", AlertType.ERROR);
				});
			}
		});
	}
	
	private void obterRelatorioPagas(long dataInicial, long dataFinal, RelatorioDespesaVencida[] vencidas) {
		new RetrofitConfig().getRelatorioService().listarDespesasPagas(dataInicial, dataFinal).enqueue(new Callback<RelatorioDespesaPaga[]>() {
			@Override
			public void onResponse(Call<RelatorioDespesaPaga[]> call, Response<RelatorioDespesaPaga[]> response) {
				Platform.runLater(() -> {
					if (response.code() == 500) {
						Main.showErrorDialog("Erro", "Erro ao obter relatório", "Não foi possível obter o relatório, tente novamente mais tarde.", AlertType.ERROR);
					} else {
						montarRelatorio(vencidas, response.body());
					}
				});
			}
			
			@Override
			public void onFailure(Call<RelatorioDespesaPaga[]> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> {
					Main.showErrorDialog("Erro", "Erro ao obter relatório", "Não foi possível obter o relatório, tente novamente mais tarde.", AlertType.ERROR);
				});
			}
		});
	}
	
	private void montarRelatorio(RelatorioDespesaVencida[] vencidas, RelatorioDespesaPaga[] pagas) {
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
	            titulo.add(new Paragraph("Duplicatas vencidas", new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD)));
	            titulo.add(new Paragraph(" "));
	            titulo.add(new Paragraph(" "));
	            document.add(titulo);
	            
	            PdfPTable tabela = new PdfPTable(4);
	
	            PdfPCell coluna1 = new PdfPCell(new Phrase("Código"));
	            coluna1.setHorizontalAlignment(Element.ALIGN_CENTER);
	            tabela.addCell(coluna1);
	            
	            PdfPCell coluna2 = new PdfPCell(new Phrase("Emissão"));
	            coluna2.setHorizontalAlignment(Element.ALIGN_CENTER);
	            tabela.addCell(coluna2);
	            
	            PdfPCell coluna3 = new PdfPCell(new Phrase("Vencimento"));
	            coluna3.setHorizontalAlignment(Element.ALIGN_CENTER);
	            tabela.addCell(coluna3);
	            
	            PdfPCell coluna4 = new PdfPCell(new Phrase("Valor"));
	            coluna4.setHorizontalAlignment(Element.ALIGN_CENTER);
	            tabela.addCell(coluna4);
	            
	            for (RelatorioDespesaVencida relatorio : vencidas) {
	            	tabela.addCell(String.valueOf(relatorio.getId()));
	            	tabela.addCell(DATE_FORMAT.format(relatorio.getDataEmissao()));
	            	tabela.addCell(DATE_FORMAT.format(relatorio.getDataVencimento()));
	            	tabela.addCell(NUMBER_FORMAT.format(relatorio.getValor()));
	            }
	            
	            document.add(tabela);
	            document.newPage();
	
	            Paragraph titulo2 = new Paragraph();
	            titulo2.add(new Paragraph("Duplicatas pagas", new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD)));
	            titulo2.add(new Paragraph(" "));
	            titulo2.add(new Paragraph(" "));
	            document.add(titulo2);
	            
	            PdfPTable tabela2 = new PdfPTable(4);
	
	            PdfPCell coluna5 = new PdfPCell(new Phrase("Código"));
	            coluna5.setHorizontalAlignment(Element.ALIGN_CENTER);
	            tabela2.addCell(coluna5);
	            
	            PdfPCell coluna6 = new PdfPCell(new Phrase("Emissão"));
	            coluna6.setHorizontalAlignment(Element.ALIGN_CENTER);
	            tabela2.addCell(coluna6);
	            
	            PdfPCell coluna7 = new PdfPCell(new Phrase("Baixa"));
	            coluna7.setHorizontalAlignment(Element.ALIGN_CENTER);
	            tabela2.addCell(coluna7);
	            
	            PdfPCell coluna8 = new PdfPCell(new Phrase("Valor"));
	            coluna8.setHorizontalAlignment(Element.ALIGN_CENTER);
	            tabela2.addCell(coluna8);
	            
	            for (RelatorioDespesaPaga relatorio : pagas) {
	            	tabela2.addCell(String.valueOf(relatorio.getId()));
	            	tabela2.addCell(DATE_FORMAT.format(relatorio.getDataEmissao()));
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
