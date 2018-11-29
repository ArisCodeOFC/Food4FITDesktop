package br.com.food4fit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javafx.scene.layout.Pane;

public class ClassificacaoProduto {
	private int id;
	private String titulo;
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
	public Pane getPaneOpcoes() {
		return paneOpcoes;
	}
	public void setPaneOpcoes(Pane paneOpcoes) {
		this.paneOpcoes = paneOpcoes;
	}


}
