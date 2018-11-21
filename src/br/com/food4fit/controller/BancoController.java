package br.com.food4fit.controller;

import br.com.food4fit.Main;
import br.com.food4fit.config.RetrofitConfig;
import br.com.food4fit.model.Banco;
import br.com.food4fit.model.UnidadeDeMedida;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BancoController {
	@FXML
    private TextField txtAgencia;

    @FXML
    private TextField txtConta;

    @FXML
    private TableColumn colunaOpc;

    @FXML
    private TableColumn colunaBanco;

    @FXML
    private TableColumn colunaAgencia;

    @FXML
    private TableView tblBanco;

    @FXML
    private Pane paneConteudo;

    @FXML
    private TextField txtBanco;

    @FXML
    private TableColumn colunaConta;


    public void initialize(){
    	paneConteudo.setStyle("visibility: false");

//    	listaBanco();
    }


  @FXML
  void salvar() {

  }


//    @FXML
//    void salvar() {
//    	String banco = txtBanco.getText();
//    	String agencia = txtAgencia.getText();
//		String conta = txtConta.getText();
//
//		Banco bancoObj = new Banco();
//
//		bancoObj.setAgencia(agencia);
//		bancoObj.setBanco(banco);
//		bancoObj.setConta(conta);
//
//		if(txtBanco.getUserData() == null){
//			Call<Banco> retorno = new RetrofitConfig().getBancoServece().salvar(bancoObj);
//			retorno.enqueue(new Callback<Banco>() {
//
//				@Override
//				public void onFailure(Call<Banco> arg0, Throwable arg1) {
//					// TODO Auto-generated method stub
//
//				}
//
//				@Override
//				public void onResponse(Call<Banco> arg0, Response<Banco> arg1) {
//					if (arg1.code() == 500) {
//						Main.showConfirmDialog("OK", "Erro", "Falha ao tentar conectar com o servidor",
//								Alert.AlertType.WARNING);
//					} else {
//
//						Banco b = arg1.body();
//
//						if (b == null) {
//							System.out.println("Deu ruim");
//						} else {
//							Platform.runLater(() -> {
//								Main.showInfDialog("Sucesso", "", "Banco cadastrado com secesso!!!");
//								fechaConteudo();
//
//								initialize();
//
//							});
//						}
//
//					}
//
//				}
//			});
//		}else{
//			int decisao = Main.showConfirmDialog("Sim", "Editar", "Deseja editar as informações?", Alert.AlertType.WARNING);
//
//			if(decisao == 1){
//				Banco banc = (Banco) txtBanco.getUserData();
//				int id = banc.getId();
//				Call<Void> retorno = new RetrofitConfig().getBancoServece().editar(banc, id);
//				retorno.enqueue(new Callback<Void>() {
//
//					@Override
//					public void onResponse(Call<Void> arg0, Response<Void> arg1) {
//						if (arg1.code() == 500) {
//							Main.showConfirmDialog("OK", "Erro", "Falha ao tentar conectar com o servidor",
//									Alert.AlertType.WARNING);
//						}else{
//							Platform.runLater(() ->{
//								fechaConteudo();
//								initialize();
//								txtBanco.setUserData(null);
//							});
//						}
//
//					}
//
//					@Override
//					public void onFailure(Call<Void> arg0, Throwable arg1) {
//						// TODO Auto-generated method stub
//
//					}
//
//				});
//			}
//		}
//    }
//
//
//    public void listaBanco(){
//    	Call<Banco[]> retorno = new RetrofitConfig().getBancoServece().lista();
//    	retorno.enqueue(new Callback<Banco[]>() {
//
//			@Override
//			public void onResponse(Call<Banco[]> arg0, Response<Banco[]> arg1) {
//				for (Banco b : arg1.body()) {
//
//					Image editImg = new Image(Main.class.getResource("assets/icons/editar-c.png").toString());
//
//					Image cancelImg = new Image(Main.class.getResource("assets/icons/cancelar-c.png").toString());
//
//					ImageView editView = new ImageView();
//					editView.prefHeight(15);
//					editView.prefWidth(15);
//					editView.setImage(editImg);
//					editView.setStyle("-fx-cursor: hand;");
//					editView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
//						txtBanco.setUserData(b);
//						abrirConteudo();
//						txtBanco.setText(b.getBanco());
//						txtAgencia.setText(b.getAgencia());
//						txtConta.setText(b.getConta());
//
//						event.consume();
//					});
//
//					ImageView deleteView = new ImageView();
//					deleteView.prefHeight(15);
//					deleteView.prefWidth(15);
//					deleteView.setImage(cancelImg);
//					deleteView.setStyle("-fx-cursor: hand");
//					deleteView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
//						Call<Void> resultAcao = new RetrofitConfig().getBancoServece().excluir(b.getId());
//						resultAcao.enqueue(new Callback<Void>() {
//
//							@Override
//							public void onFailure(Call<Void> arg0, Throwable arg1) {
//								// TODO Auto-generated method stub
//
//							}
//
//							@Override
//							public void onResponse(Call<Void> arg0, Response<Void> arg1) {
//								if (arg1.code() == 500) {
//									Main.showConfirmDialog("OK", "Erro", "Falha ao tentar excluir",
//											Alert.AlertType.WARNING);
//								} else {
//
//									Platform.runLater(() -> {
//										int result = Main.showConfirmDialog("OK", "Excluir", "Deseja excluir o item?",
//												Alert.AlertType.WARNING);
//										if (result == 1) {
//											initialize();
//										}
//
//									});
//
//								}
//
//							}
//						});
//
//						event.consume();
//					});
//
//					HBox hBox = new HBox(editView, deleteView);
//					hBox.setPrefHeight(15);
//					hBox.setPrefWidth(15);
//					hBox.setStyle("-fx-padding: 0 0 0 20; -fx-spacing:10;");
//
//					b.setPaneOpcoes(hBox);
//				}
//
//			}
//
//			@Override
//			public void onFailure(Call<Banco[]> arg0, Throwable arg1) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//    }

 // Abrir o panel oculto
 	@FXML
 	void abrirConteudo() {
 		paneConteudo.setStyle("visibility: true;");
 	}

 	// Fecha o panel que foi aberto
 	@FXML
 	void fechaConteudo() {
 		paneConteudo.setStyle("visibility: false");
 		txtBanco.clear();
 		txtAgencia.clear();
 		txtConta.clear();

 	}
}
