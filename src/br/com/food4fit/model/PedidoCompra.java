package br.com.food4fit.model;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javafx.scene.layout.Pane;

public class PedidoCompra {
	private final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance(new Locale("pt", "br"));
	private int id;
	private Fornecedor fornecedor;
	private Date dataEmissao;
	private List<Produto> produtos;
	private double frete;
	private int parcelas;
	private @JsonIgnore Pane paneOpcoes;
	private Date dataRecebimento;
	private String numeroNotaFiscal;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public double getFrete() {
		return frete;
	}

	public void setFrete(double frete) {
		this.frete = frete;
	}

	public int getParcelas() {
		return parcelas;
	}

	public void setParcelas(int parcelas) {
		this.parcelas = parcelas;
	}
	
	public Pane getPaneOpcoes() {
		return paneOpcoes;
	}
	
	public void setPaneOpcoes(Pane paneOpcoes) {
		this.paneOpcoes = paneOpcoes;
	}
	
	public int getQuantidadeProdutos() {
		return this.produtos == null ? 0 : this.produtos.size();
	}
	
	public String getValorTotal() {
		double total = frete;
		for (Produto produto : produtos) {
			total += produto.getQuantidade() * produto.getValorUnitario();
		}
		
		return "R$ " + NUMBER_FORMAT.format(total);
	}
	
	public Date getDataRecebimento() {
		return dataRecebimento;
	}
	
	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}
	
	public String getNumeroNotaFiscal() {
		return numeroNotaFiscal;
	}
	
	public void setNumeroNotaFiscal(String numeroNotaFiscal) {
		this.numeroNotaFiscal = numeroNotaFiscal;
	}
}
