package br.com.food4fit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javafx.scene.layout.Pane;

public class Produto {
	private int id;
	private String titulo;
	private String descricao;
	private UnidadeDeMedida unidadeMedida;
	private ClassificacaoProduto classificacao;
	@JsonIgnore
	private Pane paneOpcoes;
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
}
