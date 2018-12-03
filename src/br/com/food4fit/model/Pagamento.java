package br.com.food4fit.model;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javafx.scene.layout.Pane;

public class Pagamento {
	private final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance(new Locale("pt", "br"));
	
	private int id;
	private double valor;
	private String nome;
	private Date data;
	private String notaFiscal;
	private @JsonIgnore Pane paneOpcoes;
	private boolean baixa;

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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
	
	public String getNotaFiscal() {
		return notaFiscal;
	}
	
	public void setNotaFiscal(String notaFiscal) {
		this.notaFiscal = notaFiscal;
	}
	
	public Pane getPaneOpcoes() {
		return paneOpcoes;
	}
	
	public void setPaneOpcoes(Pane paneOpcoes) {
		this.paneOpcoes = paneOpcoes;
	}
	
	public String getValorFormatado() {
		return "R$ " + NUMBER_FORMAT.format(valor);
	}
	
	public boolean isBaixa() {
		return baixa;
	}
	
	public void setBaixa(boolean baixa) {
		this.baixa = baixa;
	}
}
