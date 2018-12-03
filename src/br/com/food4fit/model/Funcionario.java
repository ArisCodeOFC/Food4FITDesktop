package br.com.food4fit.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javafx.scene.layout.Pane;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Funcionario {

	private int id;
	private String nome;
	private String sobrenome;
	private String email;
	private int matricula;
	private Cargo cargo;
	private Date dataAdmissao;
	private Departamento departamento;
	private String avatar;
	private String genero;
	private String celular;
	private String telefone;
	private Date dataNascimento;
	private String rg;
	private String cpf;
	private int salario;
	private Date dataDemissao;
	private Endereco endereco;
	private List<Permissao> permissoes;
	@JsonIgnore
	private Pane paneOpcoes;

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	@JsonIgnore
	public String getDataNasciFormatada() {
		SimpleDateFormat data = new SimpleDateFormat("dd/MM/yyyy");

		return data.format(dataNascimento);
	}

	public Date getDataDemissao() {
		return dataDemissao;
	}

	public void setDataDemissao(Date dataDemissao) {
		this.dataDemissao = dataDemissao;
	}

	@JsonIgnore
	public String getDataDemissaoFormatada() {
		SimpleDateFormat data = new SimpleDateFormat("dd/MM/yyyy");

		return data.format(dataDemissao);
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public int getSalario() {
		return salario;
	}

	public void setSalario(int salario) {
		this.salario = salario;
	}

	public String getNomeCompleto() {
		return nome + " " + sobrenome;
	}

	@JsonIgnore
	public String getDataAdmissaoFormatada() {
		SimpleDateFormat data = new SimpleDateFormat("dd/MM/yyyy");

		return data.format(dataAdmissao);
	}

	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
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

	@JsonIgnore
	public Pane getPaneOpcoes() {
		return paneOpcoes;
	}

	public void setPaneOpcoes(Pane paneOpcoes) {
		this.paneOpcoes = paneOpcoes;
	}
	
	public List<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}
	
	@Override
	public String toString() {
		return id + " - " + getNomeCompleto();
	}
}
