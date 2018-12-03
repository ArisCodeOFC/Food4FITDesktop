package br.com.food4fit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.Pane;

public class Produto {
	private int id;
	private String titulo;
	private String descricao;
	private UnidadeDeMedida unidadeMedida;
	private ClassificacaoProduto classificacao;
	private double valorUnitario;
	private int quantidade;
	private @JsonIgnore Pane paneOpcoes;
	private @JsonIgnore SimpleStringProperty totalProperty = new SimpleStringProperty("R$ 0,00");
	private @JsonIgnore Pane paneCompra;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public UnidadeDeMedida getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(UnidadeDeMedida unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	public ClassificacaoProduto getClassificacao() {
		return classificacao;
	}

	public void seClassificacao(ClassificacaoProduto classificacao) {
		this.classificacao = classificacao;
	}

	public Pane getPaneOpcoes() {
		return paneOpcoes;
	}

	public void setPaneOpcoes(Pane paneOpcoes) {
		this.paneOpcoes = paneOpcoes;
	}
	
	@Override
	public String toString() {
		return id + " - " + titulo;
	}
	
	@JsonIgnore
	public String getSigla() {
		return this.unidadeMedida != null ? this.unidadeMedida.getSigla() : "";
	}
	
	public double getValorUnitario() {
		return valorUnitario;
	}
	
	public void setValorUnitario(double valorUnitario) {
		this.valorUnitario = valorUnitario;
		totalProperty.set(String.format("R$ %.2f", this.quantidade * this.valorUnitario));
	}
	
	public int getQuantidade() {
		return quantidade;
	}
	
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
		totalProperty.set(String.format("R$ %.2f", this.quantidade * this.valorUnitario));
	}
	
	public String getTotal() {
		return totalProperty.get();
	}
	
	public SimpleStringProperty totalProperty() {
		return totalProperty;
	}
	
	public Pane getPaneCompra() {
		return paneCompra;
	}
	
	public void setPaneCompra(Pane paneCompra) {
		this.paneCompra = paneCompra;
	}
	
	@Override
	public int hashCode() {
		return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Produto)) {
			return false;
		}
		
		return ((Produto) obj).getId() == id;
	}
}
