package br.com.food4fit.model;

public class TipoPagamento {
	private int id;
	private String forma;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getForma() {
		return forma;
	}

	public void setForma(String forma) {
		this.forma = forma;
	}
	
	@Override
	public String toString() {
		return forma;
	}
}
