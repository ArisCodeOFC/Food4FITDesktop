package br.com.food4fit.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javafx.scene.layout.Pane;

public class Funcionario {

	private int id;
	private String nome;
	private String sobrenome;
	private String email;
	private int matricula;
	private String cargo;
	private Date dataAdmissao;
	@JsonIgnore
	private Pane paneOpcoes;


	public String getNomeCompleto(){
		return nome+" "+sobrenome ;
	}

	public String getDataFormatada(){
		SimpleDateFormat data = new SimpleDateFormat("dd/MM/yyyy");

		return data.format(dataAdmissao);
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public Date getDataAdmissao() {
		return dataAdmissao;
	}

	public void setDataAdmissao(Date dataAdmissao) {
		this.dataAdmissao = dataAdmissao;
	}




	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getMatricula() {
		return matricula;
	}

	public void setMatricula(int matricula) {
		this.matricula = matricula;
	}

	public Pane getPaneOpcoes() {
		return paneOpcoes;
	}

	public void setPaneOpcoes(Pane paneOpcoes) {
		this.paneOpcoes = paneOpcoes;
	}

}
