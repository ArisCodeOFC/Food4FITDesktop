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
import br.com.food4fit.helper.DoubleEditingCell;
import br.com.food4fit.helper.FormHelper;
import br.com.food4fit.helper.IntegerEditingCell;
import br.com.food4fit.model.Despesa;
import br.com.food4fit.model.Fornecedor;
import br.com.food4fit.model.PedidoCompra;
import br.com.food4fit.model.Produto;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompraController {
	private final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance(new Locale("pt", "br"));
	private final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	private FormHelper formHelper = FormHelper.getInstance();
	private FormHelper formHelperNota = FormHelper.getInstance();

	private @FXML AnchorPane anchorPane;
	private @FXML Pane paneCadastro, paneNotaFiscal;
	private @FXML ComboBox<Produto> comboProduto;
	private @FXML ComboBox<Fornecedor> comboFornecedor;
	private @FXML TableView<Produto> tblProduto;
	private @FXML TableView<PedidoCompra> tblPedido;
	private @FXML TableColumn<Produto, Integer> colunaCodigoProduto, colunaQuantidade, colunaCodigo, colunaProdutos;
	private @FXML TableColumn<Produto, Double> colunaValorUnitarioProduto;
	private @FXML TableColumn<Produto, String> colunaTituloProduto, colunaUnidadeMedidaProduto, colunaTotal, colunaValorTotal;
	private @FXML TableColumn<Produto, Pane> colunaAcoes, colunaOpcoes;
	private @FXML TableColumn<Produto, Fornecedor> colunaFornecedor;
	private @FXML TableColumn<Produto, Date> colunaDataEmissao;
	private @FXML Label lblTotalCompra, lblValorFinal, lblValorParcelado, lblCodigoPedido;
	private @FXML TextField txtFrete, txtNumeroNf;
	private @FXML Spinner<Integer> spParcelas;
	private @FXML DatePicker txtDataEmissao, txtDataRecebimento;
	private Pane paneBackground;
	private double valorTotal;
	
	private @FXML void initialize() {
		paneCadastro.setVisible(false);
		paneNotaFiscal.setVisible(false);

		paneBackground = new Pane();
		paneBackground.setPrefWidth(anchorPane.getPrefWidth());
		paneBackground.setPrefHeight(anchorPane.getPrefHeight());
		paneBackground.setLayoutX(0);
		paneBackground.setLayoutY(0);
		paneBackground.setVisible(false);
		paneBackground.managedProperty().bind(paneBackground.visibleProperty());
		paneBackground.setBackground(new Background(new BackgroundFill(new Color(0, 0, 0, 0.5), CornerRadii.EMPTY, Insets.EMPTY)));
		anchorPane.getChildren().add(paneBackground);
		
		comboProduto.valueProperty().addListener(observable -> {
			if (comboProduto.getValue() != null) {
				Produto produto = comboProduto.getValue();
				tblProduto.getItems().add(produto);
				Platform.runLater(() -> {
					comboProduto.setValue(null);
					comboProduto.getItems().remove(produto);
				});
			}
		});
		
		formHelper.addValidation(comboFornecedor, FormHelper.REQUIRED);
		formHelper.addValidation(txtDataEmissao, FormHelper.REQUIRED);
		formHelper.addValidation(txtFrete, FormHelper.VALID_DOUBLE);
		
		formHelperNota.addValidation(txtDataRecebimento, FormHelper.REQUIRED);
		formHelperNota.addValidation(txtNumeroNf, FormHelper.REQUIRED);
		
		colunaCodigoProduto.setCellValueFactory(new PropertyValueFactory<>("id"));
		colunaTituloProduto.setCellValueFactory(new PropertyValueFactory<>("titulo"));
		colunaUnidadeMedidaProduto.setCellValueFactory(new PropertyValueFactory<>("sigla"));
		colunaValorUnitarioProduto.setCellValueFactory(new PropertyValueFactory<>("valorUnitario"));
		colunaValorUnitarioProduto.setCellFactory(col -> new DoubleEditingCell());
		colunaQuantidade.setCellFactory(col -> new IntegerEditingCell());
		colunaQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
		colunaTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
		colunaAcoes.setCellValueFactory(new PropertyValueFactory<>("paneCompra"));

		colunaValorUnitarioProduto.setOnEditCommit((TableColumn.CellEditEvent<Produto, Double> t) -> {
			t.getTableView().getItems().get(t.getTablePosition().getRow()).setValorUnitario(t.getNewValue());
			atualizarTotal();
		});
		
		colunaQuantidade.setOnEditCommit((TableColumn.CellEditEvent<Produto, Integer> t) -> {
			t.getTableView().getItems().get(t.getTablePosition().getRow()).setQuantidade(t.getNewValue());
			atualizarTotal();
		});

		spParcelas.valueProperty().addListener(observable -> {
			atualizarTotal();
		});
		
		txtFrete.textProperty().addListener(observable -> {
			atualizarTotal();
		});
		
		listarProdutos();
		listarFornecedores();
		listarPedidos();
	}
	
	private @FXML void abrirCadastro() {
		paneCadastro.setVisible(true);
		paneBackground.setVisible(true);
		paneCadastro.toFront();
	}
	
	private void montarPainel(Produto produto) {
		ImageView imgExcluir = new ImageView(new Image(Main.class.getResource("/br/com/food4fit/assets/icons/cancelar-c.png").toString()));
		imgExcluir.prefHeight(15);
		imgExcluir.prefWidth(15);
		imgExcluir.setCursor(Cursor.HAND);
		imgExcluir.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			produto.setQuantidade(0);
			produto.setValorUnitario(0);
			tblProduto.getItems().remove(produto);
			comboProduto.getItems().add(produto);
			atualizarTotal();
		});
		
		HBox hBox = new HBox(imgExcluir);
		hBox.setAlignment(Pos.CENTER);
		hBox.setSpacing(10);
		produto.setPaneCompra(hBox);
	}
	
	private void listarProdutos() {
		new RetrofitConfig().getProdutoService().listar().enqueue(new Callback<Produto[]>() {
			@Override
			public void onResponse(Call<Produto[]> call, Response<Produto[]> response) {
				if (response.code() == 500) {
					Platform.runLater(() -> Main.showErrorDialog("Erro", "Erro ao obter lista de produtos", "Não foi possível obter a lista de produtos, tente novamente mais tarde.", AlertType.ERROR));
				} else {
					for (Produto produto : response.body()) {
						montarPainel(produto);
					}
					
					comboProduto.setItems(FXCollections.observableArrayList(response.body()));
				}
			}
			
			@Override
			public void onFailure(Call<Produto[]> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> Main.showErrorDialog("Erro", "Erro ao obter lista de produtos", "Não foi possível obter a lista de produtos, tente novamente mais tarde.", AlertType.ERROR));
			}
		});
	}
	
	private void atualizarTotal() {
		valorTotal = 0;
		for (Produto produto : tblProduto.getItems()) {
			valorTotal += produto.getQuantidade() * produto.getValorUnitario();
		}
		
		lblTotalCompra.setText("R$ " + NUMBER_FORMAT.format(valorTotal));
		atualizarValorFinal();
	}
	
	private void atualizarValorFinal() {
		double valorFinal = valorTotal;
		if (!txtFrete.getText().isEmpty()) {
			try {
				valorFinal += Double.parseDouble(txtFrete.getText());
			} catch (NumberFormatException e) {}
		}
		
		lblValorFinal.setText("R$ " + NUMBER_FORMAT.format(valorFinal));
		lblValorParcelado.setText(spParcelas.getValue() + "x R$ " + NUMBER_FORMAT.format(valorFinal / spParcelas.getValue()));
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
	
	private @FXML void fecharConteudo() {
		txtFrete.clear();
		txtDataEmissao.setValue(null);
		comboFornecedor.setValue(null);
		comboProduto.setValue(null);
		comboProduto.getItems().addAll(tblProduto.getItems());
		tblProduto.getItems().clear();
		valorTotal = 0;
		spParcelas.getValueFactory().setValue(1);
		paneCadastro.setVisible(false);
		paneBackground.setVisible(false);
		formHelper.setObjectData(null);
	}
	
	private @FXML void salvar() {
		if (formHelper.validate()) {
			if (tblProduto.getItems().isEmpty()) {
				Main.showErrorDialog("Erro", "Erro ao registrar compra", "Selecione, no mínimo, um produto.", AlertType.ERROR);
			} else {
				PedidoCompra pedido;
				if (formHelper.getObjectData() != null) {
					pedido = (PedidoCompra) formHelper.getObjectData();
				} else {
					pedido = new PedidoCompra();
				}
				
				pedido.setFornecedor(comboFornecedor.getValue());
				pedido.setDataEmissao(Date.from(txtDataEmissao.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
				if (!txtFrete.getText().isEmpty()) {
					try {
						pedido.setFrete(Double.parseDouble(txtFrete.getText()));
					} catch (NumberFormatException e) {}
				}
				
				pedido.setParcelas(spParcelas.getValue());
				pedido.setProdutos(new ArrayList<>(tblProduto.getItems()));
				
				if (formHelper.getObjectData() == null) {
					new RetrofitConfig().getPedidoCompraService().inserir(pedido).enqueue(new Callback<PedidoCompra>() {
						@Override
						public void onResponse(Call<PedidoCompra> call, Response<PedidoCompra> response) {
							Platform.runLater(() -> {
								if (response.code() == 500) {
									Main.showErrorDialog("Erro", "Erro ao registrar pedido", "Não foi possível registrar o pedido de compra, tente novamente mais tarde.", AlertType.ERROR);
								} else {
									registrarDespesas(response.body());
								}
							});
						}
						
						@Override
						public void onFailure(Call<PedidoCompra> call, Throwable t) {
							t.printStackTrace();
							Platform.runLater(() -> 
								Main.showErrorDialog("Erro", "Erro ao registrar pedido", "Não foi possível registrar o pedido de compra, tente novamente mais tarde.", AlertType.ERROR)
							);
						}
					});
					
				} else {
					new RetrofitConfig().getPedidoCompraService().atualizar(pedido.getId(), pedido).enqueue(new Callback<Void>() {
						@Override
						public void onResponse(Call<Void> call, Response<Void> response) {
							Platform.runLater(() -> {
								if (response.code() == 500) {
									Main.showErrorDialog("Erro", "Erro ao atualizar pedido", "Não foi possível atualizar o pedido de compra, tente novamente mais tarde.", AlertType.ERROR);
								} else {
									tblPedido.refresh();
									Main.showInfDialog("Sucesso", "", "Pedido atualizado com sucesso!!!");
									fecharConteudo();
								}
							});
						}
						
						@Override
						public void onFailure(Call<Void> call, Throwable t) {
							t.printStackTrace();
							Platform.runLater(() -> 
								Main.showErrorDialog("Erro", "Erro ao atualizar pedido", "Não foi possível atualizar o pedido de compra, tente novamente mais tarde.", AlertType.ERROR)
							);
						}
					});
				}
			}
		}
	}
	
	private void registrarDespesas(PedidoCompra pedido) {
		List<Despesa> despesas = new ArrayList<>();
		double total = pedido.getFrete();
		for (Produto produto : pedido.getProdutos()) {
			total += produto.getQuantidade() * produto.getValorUnitario();
		}
		
		double valorParcela = total / pedido.getParcelas();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pedido.getDataEmissao());
		for (int i = 0; i < pedido.getParcelas(); i++) {
			calendar.add(Calendar.MONTH, 1);
			Despesa despesa = new Despesa();
			despesa.setDataEmissao(pedido.getDataEmissao());
			despesa.setDataVencimento(calendar.getTime());
			despesa.setFornecedor(pedido.getFornecedor());
			despesa.setValor(valorParcela);
			despesa.setDescricao(String.format("Pedido de compra #%d, parcela %d/%d", pedido.getId(), i + 1, pedido.getParcelas()));
			despesas.add(despesa);
		}
		
		new RetrofitConfig().getDespesaService().inserir(despesas).enqueue(new Callback<Void>() {
			@Override
			public void onResponse(Call<Void> call, Response<Void> response) {
				Platform.runLater(() -> {
					if (response.code() == 500) {
						Main.showErrorDialog("Erro", "Erro ao registrar pedido", "Não foi possível registrar o pedido de compra, tente novamente mais tarde.", AlertType.ERROR);
					} else {
						Main.showInfDialog("Sucesso", "", "Pedido de compra realizado com sucesso!!!");
						fecharConteudo();
					}
				});
			}
			
			@Override
			public void onFailure(Call<Void> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> 
					Main.showErrorDialog("Erro", "Erro ao registrar pedido", "Não foi possível registrar o pedido de compra, tente novamente mais tarde.", AlertType.ERROR)
				);
			}
		});
	}
	
	private void listarPedidos() {
		new RetrofitConfig().getPedidoCompraService().listar().enqueue(new Callback<PedidoCompra[]>() {
			@Override
			public void onResponse(Call<PedidoCompra[]> call, Response<PedidoCompra[]> response) {
				if (response.code() == 500) {
					Main.showErrorDialog("Erro", "Erro ao obter lista de pedidos", "Não foi possível obter a lista de pedidos, tente novamente mais tarde.", AlertType.ERROR);
				} else {
					montarTabela(response.body());
				}
			}
			
			@Override
			public void onFailure(Call<PedidoCompra[]> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> 
					Main.showErrorDialog("Erro", "Erro ao obter lista de pedidos", "Não foi possível obter a lista de pedidos, tente novamente mais tarde.", AlertType.ERROR)
				);
			}
		});
	}
	
	private void montarPainel(PedidoCompra pedido) {
		ImageView imgLancarNota = new ImageView(new Image(Main.class.getResource("/br/com/food4fit/assets/icons/confirmar-c.png").toString()));
		imgLancarNota.prefHeight(15);
		imgLancarNota.prefWidth(15);
		imgLancarNota.setCursor(Cursor.HAND);
		imgLancarNota.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			abrirPainelNotaFiscal(pedido);
		});
		
		ImageView imgEditar = new ImageView(new Image(Main.class.getResource("/br/com/food4fit/assets/icons/editar-c.png").toString()));
		imgEditar.prefHeight(15);
		imgEditar.prefWidth(15);
		imgEditar.setCursor(Cursor.HAND);
		imgEditar.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			editarPedido(pedido);
		});
		
		ImageView imgExcluir = new ImageView(new Image(Main.class.getResource("/br/com/food4fit/assets/icons/cancelar-c.png").toString()));
		imgExcluir.prefHeight(15);
		imgExcluir.prefWidth(15);
		imgExcluir.setCursor(Cursor.HAND);
		imgExcluir.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			excluirPedido(pedido);
		});
		
		HBox hBox = new HBox(imgLancarNota, imgEditar, imgExcluir);
		hBox.setSpacing(10);
		hBox.setAlignment(Pos.CENTER);
		pedido.setPaneOpcoes(hBox);
	}
	
	private void montarTabela(PedidoCompra[] pedidos) {
		for (PedidoCompra pedido : pedidos) {
			montarPainel(pedido);
		}
		
		colunaCodigo.setCellValueFactory(new PropertyValueFactory<>("id"));
		colunaFornecedor.setCellValueFactory(new PropertyValueFactory<>("fornecedor"));
		colunaProdutos.setCellValueFactory(new PropertyValueFactory<>("quantidadeProdutos"));
		colunaDataEmissao.setCellValueFactory(new PropertyValueFactory<>("dataEmissao"));
		colunaValorTotal.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
		colunaOpcoes.setCellValueFactory(new PropertyValueFactory<>("paneOpcoes"));
		tblPedido.setItems(FXCollections.observableArrayList(pedidos));
		
		colunaDataEmissao.setCellFactory(new ColumnFormatter<>(DATE_FORMAT));
	}
	
	private void excluirPedido(PedidoCompra pedido) {
		int resposta = Main.showConfirmDialog("Excluir", "Excluir", "Deseja realmente excluir este pedido?", AlertType.CONFIRMATION);
		if (resposta == 1) {
			new RetrofitConfig().getPedidoCompraService().excluir(pedido.getId()).enqueue(new Callback<Void>() {
				@Override
				public void onResponse(Call<Void> call, Response<Void> response) {
					Platform.runLater(() -> {
						if (response.code() == 500) {
							Main.showErrorDialog("Erro", "Erro ao excluir pedido", "Não foi possível excluir o pedido, tente novamente mais tarde.", AlertType.ERROR);
						} else {
							tblPedido.getItems().remove(pedido);
							Main.showInfDialog("Sucesso", "", "Pedido excluído com sucesso!!!");
						}
					});
				}
				
				@Override
				public void onFailure(Call<Void> call, Throwable t) {
					t.printStackTrace();
					Platform.runLater(() -> {
						Main.showErrorDialog("Erro", "Erro ao excluir pedido", "Não foi possível excluir o pedido, tente novamente mais tarde.", AlertType.ERROR);
					});
				}
			});
		}
	}
	
	private void editarPedido(PedidoCompra pedido) {
		formHelper.setObjectData(pedido);
		txtDataEmissao.setValue(pedido.getDataEmissao().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		txtFrete.setText(NUMBER_FORMAT.format(pedido.getFrete()));
		comboFornecedor.setValue(pedido.getFornecedor());
		spParcelas.getValueFactory().setValue(pedido.getParcelas());
		tblProduto.getItems().addAll(pedido.getProdutos());
		comboProduto.getItems().removeAll(pedido.getProdutos());
		atualizarTotal();
		abrirCadastro();
	}
	
	private void abrirPainelNotaFiscal(PedidoCompra pedido) {
		lblCodigoPedido.setText(String.valueOf(pedido.getId()));
		formHelperNota.setObjectData(pedido);
		paneBackground.setVisible(true);
		paneNotaFiscal.setVisible(true);
		paneNotaFiscal.toFront();
	}
	
	private @FXML void fecharNotaFiscal() {
		paneBackground.setVisible(false);
		paneNotaFiscal.setVisible(false);
		formHelperNota.setObjectData(null);
		txtNumeroNf.clear();
		txtDataRecebimento.setValue(null);
	}
	
	private @FXML void salvarNotaFiscal() {
		if (formHelperNota.validate()) {
			PedidoCompra pedido = (PedidoCompra) formHelperNota.getObjectData();
			pedido.setDataRecebimento(Date.from(txtDataRecebimento.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			pedido.setNumeroNotaFiscal(txtNumeroNf.getText());
			
			new RetrofitConfig().getPedidoCompraService().lancarNotaFiscal(pedido.getId(), pedido).enqueue(new Callback<Void>() {
				@Override
				public void onResponse(Call<Void> call, Response<Void> response) {
					Platform.runLater(() -> {
						if (response.code() == 500) {
							Main.showErrorDialog("Erro", "Erro ao lançar nota fiscal", "Não foi possível lançar a nota fiscal, tente novamente mais tarde.", AlertType.ERROR);
						} else {
							tblPedido.getItems().remove(pedido);
							Main.showInfDialog("Sucesso", "", "Nota fiscal lançada com sucesso!!!");
							fecharNotaFiscal();
						}
					});
				}
				
				@Override
				public void onFailure(Call<Void> call, Throwable t) {
					t.printStackTrace();
					Platform.runLater(() -> {
						Main.showErrorDialog("Erro", "Erro ao lançar nota fiscal", "Não foi possível lançar a nota fiscal, tente novamente mais tarde.", AlertType.ERROR);
					});
				}
			});
		}
	}
}
