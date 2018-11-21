package br.com.food4fit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javafx.scene.layout.Pane;

public class Banco {
	private int id;
	private String banco;
	private String conta;
	private String agencia;
	@JsonIgnore
	private Pane paneOpcoes;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBanco() {
		return banco;
	}
	public void setBanco(String banco) {
		this.banco = banco;
	}
	public String getConta() {
		return conta;
	}
	public void setConta(String conta) {
		this.conta = conta;
	}
	public String getAgencia() {
		return agencia;
	}
	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}
	public Pane getPaneOpcoes() {
		return paneOpcoes;
	}
	public void setPaneOpcoes(Pane paneOpcoes) {
		this.paneOpcoes = paneOpcoes;
	}


}
