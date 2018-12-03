package br.com.food4fit.controller;

import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

import javax.imageio.ImageIO;

import br.com.food4fit.Main;
import br.com.food4fit.component.MaskedTextField;
import br.com.food4fit.config.RetrofitConfig;
import br.com.food4fit.config.RetrofitConfigImg;
import br.com.food4fit.helper.FormHelper;
import br.com.food4fit.model.Cargo;
import br.com.food4fit.model.Cidade;
import br.com.food4fit.model.Departamento;
import br.com.food4fit.model.Endereco;
import br.com.food4fit.model.Estado;
import br.com.food4fit.model.Funcionario;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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

public class FuncionarioController {
	private FormHelper formHelper = FormHelper.getInstance();
	private @FXML AnchorPane conteudo;
	private @FXML TableView<Funcionario> tblFuncionario;
	private @FXML TableColumn<Funcionario, String> colunaEmail, colunaAdmissao, colunaNome;
	private @FXML TableColumn<Funcionario, Pane> colunaOpc;
	private @FXML TableColumn<Funcionario, Cargo> colunaCargo;
	private @FXML TableColumn<Funcionario, Integer> colunaMatricula;
	private @FXML Pane paneConteudo;
	private @FXML TextField txtEmail, txtSalario, txtNome, txtSobrenome, txtNumero, txtComplemento, txtBairro, txtLogradouro;
	private @FXML MaskedTextField txtTelefone, txtCelularU, txtMatricula, txtCpf, txtRg, txtCep;
	private @FXML ComboBox<Departamento> comboDepartamento;
	private @FXML ComboBox<Cargo> comboCargo;
	private @FXML ToggleGroup genero;
	private @FXML RadioButton rdoMulher, rdoHomem;
	private @FXML ImageView fotoFuncionario;
	private @FXML ComboBox<Estado> comboEstado;
	private @FXML ComboBox<Cidade> comboCidade;
	private @FXML DatePicker dpDataNascimento, dpDataAdmissao;
	private @FXML Button escolherImg;
	private Pane paneBackground;

	private @FXML void initialize() {
		paneConteudo.setStyle("visibility: false");
		formHelper.addValidation(txtMatricula, FormHelper.REQUIRED | FormHelper.VALID_MASK);
		formHelper.addValidation(txtNome, FormHelper.REQUIRED);
		formHelper.addValidation(txtSobrenome, FormHelper.REQUIRED);
		formHelper.addValidation(dpDataNascimento, FormHelper.REQUIRED);
		formHelper.addValidation(txtEmail, FormHelper.REQUIRED | FormHelper.VALID_EMAIL);
		formHelper.addValidation(genero, FormHelper.REQUIRED);
		formHelper.addValidation(txtCelularU, FormHelper.REQUIRED | FormHelper.VALID_MASK);
		formHelper.addValidation(txtTelefone, FormHelper.REQUIRED | FormHelper.VALID_MASK);
		formHelper.addValidation(txtCpf, FormHelper.REQUIRED | FormHelper.VALID_MASK);
		formHelper.addValidation(txtRg, FormHelper.REQUIRED | FormHelper.VALID_MASK);
		formHelper.addValidation(dpDataAdmissao, FormHelper.REQUIRED);
		formHelper.addValidation(comboCargo, FormHelper.REQUIRED);
		formHelper.addValidation(comboDepartamento, FormHelper.REQUIRED);
		formHelper.addValidation(txtSalario, FormHelper.REQUIRED | FormHelper.VALID_DOUBLE);
		formHelper.addValidation(comboEstado, FormHelper.REQUIRED);
		formHelper.addValidation(comboCidade, FormHelper.REQUIRED);
		formHelper.addValidation(txtCep, FormHelper.REQUIRED | FormHelper.VALID_MASK);
		formHelper.addValidation(txtBairro, FormHelper.REQUIRED);
		formHelper.addValidation(txtLogradouro, FormHelper.REQUIRED);
		formHelper.addValidation(txtNumero, FormHelper.REQUIRED);
		
		paneBackground = new Pane();
		paneBackground.setPrefWidth(conteudo.getPrefWidth());
		paneBackground.setPrefHeight(conteudo.getPrefHeight());
		paneBackground.setLayoutX(0);
		paneBackground.setLayoutY(0);
		paneBackground.setVisible(false);
		paneBackground.managedProperty().bind(paneBackground.visibleProperty());
		paneBackground.setBackground(new Background(new BackgroundFill(new Color(0, 0, 0, 0.5), CornerRadii.EMPTY, Insets.EMPTY)));
		conteudo.getChildren().add(paneBackground);

		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Escolher imagem");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Imagens", "*.jpg", "*.png"));

		comboEstado.valueProperty().addListener((obs, oldval, newval) -> {
			comboCidade.setValue(null);
			if (newval != null) {
				Estado estado = (Estado) newval;
				listarCidade(estado.getId());
			}
		});

		escolherImg.setOnAction((final ActionEvent e) -> {
			File file = fileChooser.showOpenDialog(conteudo.getScene().getWindow());
			if (file != null) {
				fileChooser.setInitialDirectory(file.getParentFile());
				openFile(file);
			}
		});

		listarFuncionario();
		listarCargo();
		listarDepartamento();
		listarEstado();
	}
	
	public void listarEstado() {
		comboEstado.setCursor(Cursor.WAIT);
		Call<Estado[]> retorno = new RetrofitConfig().getEstadoService().listarEstado();
		retorno.enqueue(new Callback<Estado[]>() {
			@Override
			public void onResponse(Call<Estado[]> call, Response<Estado[]> response) {
				Platform.runLater(() -> {
					comboEstado.setCursor(Cursor.HAND);
					if (response.code() == 500) {
						Main.showErrorDialog("Erro", "Erro ao obter lista de estados", "Não foi possível obter a lista de estados, tente novamente mais tarde.", AlertType.ERROR);
					} else {
						comboEstado.setItems(FXCollections.observableArrayList(Arrays.asList(response.body())));
					}
				});
			}
			
			@Override
			public void onFailure(Call<Estado[]> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> {
					comboEstado.setCursor(Cursor.HAND);
					Main.showErrorDialog("Erro", "Erro ao obter lista de estados", "Não foi possível obter a lista de estados, tente novamente mais tarde.", AlertType.ERROR);
				});
			}
		});
	}

	public void listarCidade(int id) {
		comboCidade.setCursor(Cursor.WAIT);
		Call<Cidade[]> retorno = new RetrofitConfig().getEstadoService().listarCidade(id);
		retorno.enqueue(new Callback<Cidade[]>() {
			@Override
			public void onResponse(Call<Cidade[]> call, Response<Cidade[]> response) {
				comboCidade.setCursor(Cursor.HAND);
				if (response.code() == 500) {
					Main.showErrorDialog("Erro", "Erro ao obter lista de cidades", "Não foi possível obter a lista de cidades, tente novamente mais tarde.", AlertType.ERROR);
				} else {
					comboCidade.setItems(FXCollections.observableArrayList(Arrays.asList(response.body())));
				}
			}
			
			@Override
			public void onFailure(Call<Cidade[]> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> {
					comboCidade.setCursor(Cursor.HAND);
					Main.showErrorDialog("Erro", "Erro ao obter lista de cidades", "Não foi possível obter a lista de cidades, tente novamente mais tarde.", AlertType.ERROR);
				});
			}
		});
	}

	public void listarCargo() {
		comboCargo.setCursor(Cursor.WAIT);
		Call<Cargo[]> retorno = new RetrofitConfig().getCargoService().listar();
		retorno.enqueue(new Callback<Cargo[]>() {
			@Override
			public void onResponse(Call<Cargo[]> call, Response<Cargo[]> response) {
				comboCargo.setCursor(Cursor.HAND);
				if (response.code() == 500) {
					Main.showErrorDialog("Erro", "Erro ao obter lista de cargos", "Não foi possível obter a lista de cargos, tente novamente mais tarde.", AlertType.ERROR);
				} else {
					comboCargo.setItems(FXCollections.observableArrayList(Arrays.asList(response.body())));
				}
			}
			
			@Override
			public void onFailure(Call<Cargo[]> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> {
					comboCargo.setCursor(Cursor.HAND);
					Main.showErrorDialog("Erro", "Erro ao obter lista de cargos", "Não foi possível obter a lista de cargos, tente novamente mais tarde.", AlertType.ERROR);
				});
			}
		});
	}

	public void listarDepartamento() {
		comboDepartamento.setCursor(Cursor.WAIT);
		Call<Departamento[]> retorno = new RetrofitConfig().getDepartamentoService().listar();
		retorno.enqueue(new Callback<Departamento[]>() {
			@Override
			public void onResponse(Call<Departamento[]> call, Response<Departamento[]> response) {
				comboDepartamento.setCursor(Cursor.HAND);
				if (response.code() == 500) {
					Main.showErrorDialog("Erro", "Erro ao obter lista de departamentos", "Não foi possível obter a lista de departamentos, tente novamente mais tarde.", AlertType.ERROR);
				} else {
					comboDepartamento.setItems(FXCollections.observableArrayList(Arrays.asList(response.body())));
				}
			}
			
			@Override
			public void onFailure(Call<Departamento[]> call, Throwable t) {
				t.printStackTrace();
				Platform.runLater(() -> {
					comboDepartamento.setCursor(Cursor.HAND);
					Main.showErrorDialog("Erro", "Erro ao obter lista de departamentos", "Não foi possível obter a lista de departamentos, tente novamente mais tarde.", AlertType.ERROR);
				});
			}
		});
	}

	public void listarFuncionario() {
		Call<Funcionario[]> retorno = new RetrofitConfig().getFuncionarioService().lista();
		retorno.enqueue(new Callback<Funcionario[]>() {
			@Override
			public void onResponse(Call<Funcionario[]> call, Response<Funcionario[]> response) {
				if (response.code() == 500) {
					Platform.runLater(() -> 
						Main.showErrorDialog("Erro", "Erro ao obter lista de funcionários", "Não foi possível obter a lista de funcionários, tente novamente mais tarde.", AlertType.ERROR)
					);
					
				} else {
					montarTabela(response.body());
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
	
	private void montarPainel(Funcionario funcionario) {
		Image usuarioImg = new Image(Main.class.getResource("assets/icons/usuario-c.png").toString());
		Image editImg = new Image(Main.class.getResource("assets/icons/editar-c.png").toString());
		Image cancelImg = new Image(Main.class.getResource("assets/icons/cancelar-c.png").toString());

		ImageView usuarioView = new ImageView();
		usuarioView.setImage(usuarioImg);
		usuarioView.prefHeight(15);
		usuarioView.prefWidth(15);
		usuarioView.setStyle("-fx-cursor: hand;");

		usuarioView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			Main.getHome().mudarTela("Usuario", "Cadastrar Usuário", funcionario);
		});
		
		ImageView editView = new ImageView();
		editView.prefHeight(15);
		editView.prefWidth(15);
		editView.setImage(editImg);
		editView.setStyle("-fx-cursor: hand;");

		editView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			editarFuncionario(funcionario);
			event.consume();
		});

		ImageView deleteView = new ImageView();
		deleteView.prefHeight(15);
		deleteView.prefWidth(15);
		deleteView.setImage(cancelImg);
		deleteView.setStyle("-fx-cursor: hand");
		deleteView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			excluirFuncionario(funcionario);
		});

		HBox hBox = new HBox(usuarioView, editView, deleteView);
		hBox.setSpacing(10);
		hBox.setAlignment(Pos.CENTER);
		funcionario.setPaneOpcoes(hBox);
	}
	
	private void montarTabela(Funcionario[] funcionarios) {
		for (Funcionario funcionario : funcionarios) {
			montarPainel(funcionario);
		}

		colunaNome.setCellValueFactory(new PropertyValueFactory<Funcionario, String>("nomeCompleto"));
		colunaMatricula.setCellValueFactory(new PropertyValueFactory<Funcionario, Integer>("matricula"));
		colunaEmail.setCellValueFactory(new PropertyValueFactory<Funcionario, String>("email"));
		colunaCargo.setCellValueFactory(new PropertyValueFactory<Funcionario, Cargo>("cargo"));
		colunaAdmissao.setCellValueFactory(new PropertyValueFactory<Funcionario, String>("dataAdmissaoFormatada"));
		colunaOpc.setCellValueFactory(new PropertyValueFactory<Funcionario, Pane>("paneOpcoes"));
		tblFuncionario.setItems(FXCollections.observableArrayList(funcionarios));
	}

	// ******************************************************************

	// escolherImg

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

	private @FXML void salvar() {
		if (formHelper.validate()) {
			String celular = txtCelularU.getText();
			String telefone = txtTelefone.getText();
			String cpf = txtCpf.getText();
	
			Date dataAdm = Date.from(dpDataAdmissao.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
			
			Date dataNasc = Date.from(dpDataNascimento.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
	
			String email = txtEmail.getText();
	
			String matri = txtMatricula.getText();
			Integer matricula = Integer.parseInt(matri);
	
			String nome = txtNome.getText();
			String sobrenome = txtSobrenome.getText();
			String rg = txtRg.getText();
	
			String salario = txtSalario.getText();
			Integer sal = Integer.parseInt(salario);
	
			String sexo = "";
			if (rdoHomem.isSelected()) {
				sexo = "H";
			} else if (rdoMulher.isSelected()) {
				sexo = "M";
			}

			// Convertendo a imagem para base 64
			BufferedImage avatar = SwingFXUtils.fromFXImage(fotoFuncionario.getImage(), null);
			ByteArrayOutputStream s = new ByteArrayOutputStream();
			String foto = "";
			try {
				ImageIO.write(avatar, "png", s);
				byte[] res = s.toByteArray();
				s.close();
				foto = Base64.getEncoder().encodeToString(res);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Cargo cargo = (Cargo) comboCargo.getValue();
			Departamento departamento = (Departamento) comboDepartamento.getValue();
	
			Estado estado = (Estado) comboEstado.getValue();
			Cidade cidade = (Cidade) comboCidade.getValue();
	
			String bairro = txtBairro.getText();
			String logradouro = txtLogradouro.getText();
			String numero = txtNumero.getText();
			String complemento = txtComplemento.getText();
			String cep = txtCep.getText();
			
			Funcionario funcionario;
			if (formHelper.getObjectData() != null) {
				funcionario = (Funcionario) formHelper.getObjectData();
			} else {
				funcionario = new Funcionario();
			}
			
			Endereco end = funcionario.getEndereco() == null ? new Endereco() : funcionario.getEndereco();
			funcionario.setCelular(celular);
			funcionario.setTelefone(telefone);
			funcionario.setCpf(cpf);
			funcionario.setDataAdmissao(dataAdm);
			funcionario.setDataNascimento(dataNasc);
			funcionario.setEmail(email);
			funcionario.setMatricula(matricula);
			funcionario.setNome(nome);
			funcionario.setSobrenome(sobrenome);
			funcionario.setRg(rg);
			funcionario.setSalario(sal);
			funcionario.setGenero(sexo);
			funcionario.setCargo(cargo);
			funcionario.setDepartamento(departamento);
	
			funcionario.setEndereco(end);
			funcionario.getEndereco().setCidade(cidade);
			funcionario.getEndereco().setEstado(estado);
			funcionario.getEndereco().setBairro(bairro);
			funcionario.getEndereco().setLogradouro(logradouro);
			funcionario.getEndereco().setNumero(numero);
			funcionario.getEndereco().setComplemento(complemento);
			funcionario.getEndereco().setCep(cep);
	
			Call<String> retorno = new RetrofitConfigImg().getAvatarService().enviar(foto);
			retorno.enqueue(new Callback<String>() {
				@Override
				public void onResponse(Call<String> call, Response<String> response) {
					funcionario.setAvatar(response.body());
					if (formHelper.getObjectData() == null) {
						Call<Funcionario> ret = new RetrofitConfig().getFuncionarioService().salvar(funcionario);
						ret.enqueue(new Callback<Funcionario>() {
							@Override
							public void onResponse(Call<Funcionario> call, Response<Funcionario> response) {
								Platform.runLater(() -> {
									if (response.code() == 500) {
										Main.showErrorDialog("Erro", "Erro ao inserir funcionário", "Não foi possível inserir o funcionário, tente novamente mais tarde.", AlertType.ERROR);
									} else {
										montarPainel(response.body());
										tblFuncionario.getItems().add(response.body());
										Main.showInfDialog("Sucesso", "", "Funcionário cadastrado com sucesso!!!");
										fecharConteudo();
									}
								});
							}
							
							@Override
							public void onFailure(Call<Funcionario> call, Throwable t) {
								t.printStackTrace();
								Platform.runLater(() -> 
									Main.showErrorDialog("Erro", "Erro ao inserir funcionário", "Não foi possível inserir o funcionário, tente novamente mais tarde.", AlertType.ERROR)
								);
							}
						});
						
					} else {
						Call<Void> ret = new RetrofitConfig().getFuncionarioService().editar(funcionario, funcionario.getId());
						ret.enqueue(new Callback<Void>() {
							@Override
							public void onResponse(Call<Void> call, Response<Void> response) {
								Platform.runLater(() -> {
									if (response.code() == 500) {
										Main.showErrorDialog("Erro", "Erro ao atualizar funcionário", "Não foi possível atualizar o funcionário, tente novamente mais tarde.", AlertType.ERROR);
									} else {
										formHelper.setObjectData(null);
										tblFuncionario.refresh();
										Main.showInfDialog("Sucesso", "", "Funcionário atualizado com sucesso!!!");
										fecharConteudo();
									}
								});
							}
							
							@Override
							public void onFailure(Call<Void> call, Throwable t) {
								t.printStackTrace();
								Platform.runLater(() -> 
									Main.showErrorDialog("Erro", "Erro ao atualizar funcionário", "Não foi possível atualizar o funcionário, tente novamente mais tarde.", AlertType.ERROR)
								);
							}
						});
					}
				}
				
				@Override
				public void onFailure(Call<String> call, Throwable t) {
					t.printStackTrace();
					Platform.runLater(() -> {
						Main.showErrorDialog("Erro", "Erro ao enviar a foto", "Não foi possível enviar a foto ao servidor, tente novamente mais tarde.", AlertType.ERROR);
					});
				}
			});
		}
	}

	// Abrir o panel oculto
	private @FXML void abrirConteudo() {
		paneConteudo.setStyle("visibility: true;");
		paneBackground.setVisible(true);
		paneConteudo.toFront();
	}

	// Fecha o panel que foi aberto
	private @FXML void fecharConteudo() {
		formHelper.setObjectData(null);
		paneConteudo.setStyle("visibility: false");
		paneBackground.setVisible(false);
		txtCelularU.clear();
		txtCpf.clear();
		dpDataAdmissao.setValue(null);
		dpDataNascimento.setValue(null);
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
		comboEstado.setValue(null);
		comboCidade.setValue(null);
		txtBairro.clear();
		txtLogradouro.clear();
		txtNumero.clear();
		txtComplemento.clear();
		txtCep.clear();

	}

	private void editarFuncionario(Funcionario funcionario) {
		formHelper.setObjectData(funcionario);
		txtSobrenome.setText(funcionario.getSobrenome());
		txtEmail.setText(funcionario.getEmail());
		txtMatricula.setPlainText(String.valueOf(funcionario.getMatricula()));
		txtNome.setText(funcionario.getNome());
		dpDataNascimento.setValue(funcionario.getDataNascimento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		dpDataAdmissao.setValue(funcionario.getDataAdmissao().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

		if ("M".equals(funcionario.getGenero())) {
			rdoMulher.setSelected(true);
		} else {
			rdoHomem.setSelected(true);
		}

		txtCelularU.setPlainText(funcionario.getCelular());
		txtTelefone.setPlainText(funcionario.getTelefone());
		txtCpf.setPlainText(funcionario.getCpf());
		txtRg.setPlainText(funcionario.getRg());
		txtSalario.setText(String.valueOf(funcionario.getSalario()));
		comboDepartamento.setValue(funcionario.getDepartamento());
		comboCargo.setValue(funcionario.getCargo());
		comboEstado.setValue(funcionario.getEndereco().getEstado());
		comboCidade.setValue(funcionario.getEndereco().getCidade());
		txtBairro.setText(funcionario.getEndereco().getBairro());
		txtLogradouro.setText(funcionario.getEndereco().getLogradouro());
		txtNumero.setText(funcionario.getEndereco().getNumero());
		txtComplemento.setText(funcionario.getEndereco().getComplemento());
		txtCep.setPlainText(funcionario.getEndereco().getCep());
		//fotoFuncionario.setImage(new Image("http://localhost/inf4t/Allan/Food-4FitWEB-Procedure-master/" + funcionario.getAvatar()));
		fotoFuncionario.setImage(new Image("http://localhost/arisCodeProcedural/" + funcionario.getAvatar()));
		abrirConteudo();
	}
	
	private void excluirFuncionario(Funcionario funcionario) {
		int resposta = Main.showConfirmDialog("Excluir", "Excluir", "Deseja realmente excluir este funcionário?", AlertType.CONFIRMATION);
		if (resposta == 1) {
			Call<Void> retorno = new RetrofitConfig().getFuncionarioService().excluir(funcionario.getId());
			retorno.enqueue(new Callback<Void>() {
				@Override
				public void onResponse(Call<Void> call, Response<Void> response) {
					Platform.runLater(() -> {
						if (response.code() == 500) {
							Main.showErrorDialog("Erro", "Erro ao excluir funcionário", "Não foi possível excluir o funcionário, tente novamente mais tarde.", AlertType.ERROR);
						} else {
							tblFuncionario.getItems().remove(funcionario);
							Main.showInfDialog("Sucesso", "", "Funcionário excluído com sucesso!!!");
						}
					});
				}
				
				@Override
				public void onFailure(Call<Void> call, Throwable t) {
					t.printStackTrace();
					Platform.runLater(() -> {
						Main.showErrorDialog("Erro", "Erro ao excluir funcionário", "Não foi possível excluir o funcionário, tente novamente mais tarde.", AlertType.ERROR);
					});
				}
			});
		}
	}
}
