package br.com.food4fit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javafx.scene.layout.Pane;

public class Departamento {
	private int id;
	private String departamento;
	@JsonIgnore
	private Pane paneOpcoes;

	public Pane getPaneOpcoes() {
		return paneOpcoes;
	}
	public void setPaneOpcoes(Pane paneOpcoes) {
		this.paneOpcoes = paneOpcoes;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	@Override
	public String toString() {
		return departamento;
	}


}
