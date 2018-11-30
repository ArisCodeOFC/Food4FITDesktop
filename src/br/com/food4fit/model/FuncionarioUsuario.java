package br.com.food4fit.model;

import java.util.List;

public class FuncionarioUsuario {
	private String senha;
	private List<Permissao> permissoes;

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public List<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}
}
