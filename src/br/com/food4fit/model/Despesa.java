package br.com.food4fit.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javafx.scene.layout.Pane;

public class Despesa {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	private int id;
	private String descricao;
	private Date dataEmissao;
	private Date dataVencimento;
	private double valor;
	private Fornecedor fornecedor;
	private Funcionario funcionario;
	private @JsonIgnore Pane paneOpcoes;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
	
	public Pane getPaneOpcoes() {
		return paneOpcoes;
	}
	
	public void setPaneOpcoes(Pane paneOpcoes) {
		this.paneOpcoes = paneOpcoes;
	}
	
	public String getNome() {
		if (this.funcionario != null) {
			return this.funcionario.getNomeCompleto();
		} else if (this.fornecedor != null) {
			return this.fornecedor.getRazaoSocial();
		} else {
			return "";
		}
	}
	
	public String getTipo() {
		if (this.funcionario != null) {
			return "FUNCIONÁRIO";
		} else if (this.fornecedor != null) {
			return "FORNECEDOR";
		} else {
			return "";
		}
	}
	
	public String getDataEmissaoFormatada() {
		return DATE_FORMAT.format(dataEmissao);
	}
	
	public String getDataVencimentoFormatada() {
		return DATE_FORMAT.format(dataVencimento);
	}
	
	public String getValorFormatado() {
		return String.format("R$ %.2f", valor);
	}
}
