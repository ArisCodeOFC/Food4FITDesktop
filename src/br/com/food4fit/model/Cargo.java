package br.com.food4fit.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javafx.scene.layout.Pane;

public class Cargo {
	private int id;
	private String cargo;
	private List<Permissao> permissoes;
	private @JsonIgnore Pane paneOpcoes;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	
	public List<Permissao> getPermissoes() {
		return permissoes;
	}
	
	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}
	
	public Pane getPaneOpcoes() {
		return paneOpcoes;
	}

	public void setPaneOpcoes(Pane paneOpcoes) {
		this.paneOpcoes = paneOpcoes;
	}

	@Override
	public String toString() {
		return cargo;
	}
}
