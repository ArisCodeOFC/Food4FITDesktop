package br.com.food4fit.model;

public class Permissao {
	private int id;
	private String descricao;
	private String chave;
	private boolean web;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getChave() {
		return this.chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public boolean isWeb() {
		return this.web;
	}

	public void setWeb(boolean web) {
		this.web = web;
	}
}
