package br.com.food4fit.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javafx.scene.layout.Pane;

public class ConciliacaoBancaria {
	private int id;
	private double valor;
	private Date data;
	private int tipo;
	private boolean conciliado;
	private @JsonIgnore Pane paneOpcoes;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public boolean isConciliado() {
		return conciliado;
	}

	public void setConciliado(boolean conciliado) {
		this.conciliado = conciliado;
	}
	
	public Pane getPaneOpcoes() {
		return paneOpcoes;
	}
	
	public void setPaneOpcoes(Pane paneOpcoes) {
		this.paneOpcoes = paneOpcoes;
	}
	
	public @JsonIgnore String getTipoExtrato() {
		return tipo == 0 ? "SAÍDA" : "ENTRADA";
	}
	
	public @JsonIgnore double getSaldo() {
		return valor * (tipo == 0 ? -1 : 1);
	}
}
