package br.com.food4fit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javafx.scene.layout.Pane;

public class UnidadeDeMedida {
	private int id;
	private String unidadeMedida;
	private String sigla;
	@JsonIgnore
	private Pane paneOpcoes;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(String unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public Pane getPaneOpcoes() {
		return paneOpcoes;
	}

	public void setPaneOpcoes(Pane paneOpcoes) {
		this.paneOpcoes = paneOpcoes;
	}

	@Override
	public String toString() {
		return this.unidadeMedida + " (" + this.sigla  + ")";
	}
}
