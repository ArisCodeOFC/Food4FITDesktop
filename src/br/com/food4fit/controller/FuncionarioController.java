package br.com.food4fit.controller;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import javax.swing.event.DocumentEvent.EventType;

import br.com.food4fit.Main;
import br.com.food4fit.config.RetrofitConfig;
import br.com.food4fit.model.Cargo;
import br.com.food4fit.model.Cidade;
import br.com.food4fit.model.Departamento;
import br.com.food4fit.model.Estado;
import br.com.food4fit.model.Funcionario;
import br.com.food4fit.model.UnidadeDeMedida;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FuncionarioController {
	@FXML
	private AnchorPane conteudo;

	@FXML
	private TableView tblFuncionario;

	@FXML
	private TableColumn colunaEmail;

	@FXML
	private TableColumn colunaOpc;

	@FXML
	private TableColumn colunaCargo;

	@FXML
	private TableColumn colunaAdmissao;

	@FXML
	private TableColumn colunaMatricula;

	@FXML
	private TableColumn colunaNome;

	@FXML
	private Pane paneConteudo;

	@FXML
	private TextField txtEmail;

	@FXML
	private TextField txtTelefone;

	@FXML
	private ComboBox comboDepartamento;

	@FXML
	private TextField txtCpf;

	@FXML
	private TextField txtSalario;

	@FXML
	private TextField txtDtAdmissao;

	@FXML
	private TextField txtDtNasc;

	@FXML
	private TextField txtCelularU;

	@FXML
	private ComboBox comboCargo;

	@FXML
	private ToggleGroup genero;

	@FXML
	private RadioButton rdoMulher;

	@FXML
	private RadioButton rdoHomem;

	@FXML
	private TextField txtMatricula;

	@FXML
	private TextField txtNome;

	@FXML
	private TextField txtSobrenome;

	@FXML
	private TextField txtRg;

	@FXML
	private ImageView fotoFuncionario;

	@FXML
	private TextField txtNumero;

	@FXML
	private TextField txtComplemento;

	@FXML
	private TextField txtLogradouro;

	@FXML
	private ComboBox comboEstado;

	@FXML
	private ComboBox comboCidade;

	@FXML
	private TextField txtCep;

	@FXML
	private TextField txtBairro;

	@FXML
	private Button escolherImg;

	public void initialize() {
		paneConteudo.setStyle("visibility: false");
		final FileChooser fileChooser = new FileChooser();
		rdoHomem.setUserData("H");
		rdoMulher.setUserData("M");

		escolherImg.setOnAction((final ActionEvent e) -> {
			configureFileChooser(fileChooser);
			File file = fileChooser.showOpenDialog(conteudo.getScene().getWindow());
			if (file != null) {
				openFile(file);
			}
		});

		listarFuncionario();
		listarCargo();
		listarDepartamento();
		listarEstado();

	}

	public void listarEstado(){
		Call<Estado[]> retorno = new RetrofitConfig().getEstadoService().listarEstado();
		retorno.enqueue(new Callback<Estado[]>() {

			@Override
			public void onResponse(Call<Estado[]> arg0, Response<Estado[]> arg1) {
				comboEstado.setItems(FXCollections.observableList(Arrays.asList(arg1.body())));


			}

			@Override
			public void onFailure(Call<Estado[]> arg0, Throwable arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void listarCidade(int id){
		Call<Cidade[]> retorno = new RetrofitConfig().getEstadoService().listarCidade(id);
	}

	public void listarCargo() {
		Call<Cargo[]> retorno = new RetrofitConfig().getCargoService().listar();
		retorno.enqueue(new Callback<Cargo[]>() {

			@Override
			public void onResponse(Call<Cargo[]> arg0, Response<Cargo[]> arg1) {
				comboCargo.setItems(FXCollections.observableList(Arrays.asList(arg1.body())));


			}

			@Override
			public void onFailure(Call<Cargo[]> arg0, Throwable arg1) {
				arg1.printStackTrace();

			}
		});

	}

	public void listarDepartamento(){
		Call<Departamento[]> retorno = new RetrofitConfig().getDepartamentoService().listar();
		retorno.enqueue(new Callback<Departamento[]>() {

			@Override
			public void onResponse(Call<Departamento[]> arg0, Response<Departamento[]> arg1) {
				comboDepartamento.setItems(FXCollections.observableArrayList(Arrays.asList(arg1.body())));

			}

			@Override
			public void onFailure(Call<Departamento[]> arg0, Throwable arg1) {


			}
		});
	}

	public void listarFuncionario() {
		Call<Funcionario[]> retorno = new RetrofitConfig().getFuncionarioService().lista();
		retorno.enqueue(new Callback<Funcionario[]>() {

			@Override
			public void onResponse(Call<Funcionario[]> arg0, Response<Funcionario[]> arg1) {
				for (Funcionario f : arg1.body()) {
					Image editImg = new Image(Main.class.getResource("assets/icons/editar-c.png").toString());

					Image cancelImg = new Image(Main.class.getResource("assets/icons/cancelar-c.png").toString());

					ImageView editView = new ImageView();
					editView.prefHeight(15);
					editView.prefWidth(15);
					editView.setImage(editImg);
					editView.setStyle("-fx-cursor: hand;");
					editView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
						txtNome.setUserData(f);
						txtSobrenome.setText(f.getSobrenome());
						txtEmail.setText(f.getEmail());
						txtDtAdmissao.setText(String.valueOf(f.getDataAdmissaoFormatada()));
						comboCargo.setValue(f.getCargo());
						txtMatricula.setText(String.valueOf(f.getMatricula()));
						txtNome.setText(f.getNome());
						txtDtNasc.setText(String.valueOf(f.getDataNasciFormatada()));

						// genero.selectToggle(f.getGenero());
						txtCelularU.setText(f.getCelular());
						txtTelefone.setText(f.getTelefone());
						txtCpf.setText(f.getCpf());
						txtRg.setText(f.getRg());
						txtSalario.setText(String.valueOf(f.getSalario()));
						comboDepartamento.setValue(f.getDepartamento());
						comboEstado.setValue(f.getEndereco().getEstado());
						comboCidade.setValue(f.getEndereco().getCidade());
						txtBairro.setText(f.getEndereco().getBairro());
						txtLogradouro.setText(f.getEndereco().getLogradouro());
						txtNumero.setText(f.getEndereco().getNumero());
						txtComplemento.setText(f.getEndereco().getComplemento());
						txtCep.setText(f.getEndereco().getCep());
						//fotoFuncionario

						abrirConteudo();
						event.consume();
					});

					ImageView deleteView = new ImageView();
					deleteView.prefHeight(15);
					deleteView.prefWidth(15);
					deleteView.setImage(cancelImg);
					deleteView.setStyle("-fx-cursor: hand");
					deleteView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
						Call<Void> retorno = new RetrofitConfig().getFuncionarioService().excluir(f.getId());
						retorno.enqueue(new Callback<Void>() {

							@Override
							public void onFailure(Call<Void> arg0, Throwable arg1) {
								arg1.printStackTrace();

							}

							@Override
							public void onResponse(Call<Void> arg0, Response<Void> arg1) {
								if (arg1.code() == 500) {
									Main.showConfirmDialog("OK", "Erro", "Falha ao tentar excluir",
											Alert.AlertType.WARNING);
								} else {

									Platform.runLater(() -> {
										int result = Main.showConfirmDialog("OK", "Excluir", "Deseja excluir o item?",
												Alert.AlertType.WARNING);
										if (result == 1) {
											initialize();
										}

									});

								}

							}
						});

					});

					HBox hBox = new HBox(editView, deleteView);
					hBox.setPrefHeight(15);
					hBox.setPrefWidth(15);
					hBox.setStyle("-fx-padding: 0 0 0 32; -fx-spacing:10;");

					f.setPaneOpcoes(hBox);

				}

				colunaNome.setCellValueFactory(new PropertyValueFactory<Funcionario, String>("nomeCompleto"));
				colunaMatricula.setCellValueFactory(new PropertyValueFactory<Funcionario, String>("matricula"));
				colunaEmail.setCellValueFactory(new PropertyValueFactory<Funcionario, String>("email"));
				colunaCargo.setCellValueFactory(new PropertyValueFactory<Funcionario, String>("cargo"));
				colunaAdmissao.setCellValueFactory(new PropertyValueFactory<Funcionario, Date>("dataFormatada"));
				colunaOpc.setCellValueFactory(new PropertyValueFactory<UnidadeDeMedida, Pane>("paneOpcoes"));
				tblFuncionario.setItems(FXCollections.observableArrayList(arg1.body()));
			}

			@Override
			public void onFailure(Call<Funcionario[]> arg0, Throwable arg1) {
				arg1.printStackTrace();

			}
		});
	}

	// ******************************************************************

	// escolherImg

	private static void configureFileChooser(final FileChooser fileChooser) {
		fileChooser.setTitle("View Pictures");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images", "*.*"),
				new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.png"));
	}

	private void openFile(File file) {
		EventQueue.invokeLater(() -> {
			try {
				Image imagen = new Image(new FileInputStream(file));
				fotoFuncionario.setImage(imagen);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	// ******************************************************************

	@FXML
	void salvar() {

	}

	// Abrir o panel oculto
	@FXML
	void abrirConteudo() {
		paneConteudo.setStyle("visibility: true;");

	}

	// Fecha o panel que foi aberto
	@FXML
	void fechaConteudo() {
		paneConteudo.setStyle("visibility: false");
		txtCelularU.clear();
		txtCpf.clear();
		txtDtAdmissao.clear();
		txtDtNasc.clear();
		txtEmail.clear();
		txtMatricula.clear();
		txtNome.clear();
		txtRg.clear();
		txtSalario.clear();
		txtSobrenome.clear();
		txtTelefone.clear();
		comboCargo.setValue(null);
		comboDepartamento.setValue(null);
		genero.selectToggle(null);
		txtDtNasc.clear();
		comboEstado.setValue(null);
		comboCidade.setValue(null);
		txtBairro.clear();
		txtLogradouro.clear();
		txtNumero.clear();
		txtComplemento.clear();
		txtCep.clear();

	}

}
