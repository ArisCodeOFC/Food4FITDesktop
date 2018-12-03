package br.com.food4fit.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FluxoCaixa {
	private Date data;
	private double entrada;
	private double saida;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public double getEntrada() {
		return entrada;
	}

	public void setEntrada(double entrada) {
		this.entrada = entrada;
	}

	public double getSaida() {
		return saida;
	}

	public void setSaida(double saida) {
		this.saida = saida;
	}

	@Override
	public int hashCode() {
		return this.data.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof FluxoCaixa)) {
			return false;
		}
		
		return ((FluxoCaixa) obj).getData().equals(data);
	}
	
	public @JsonIgnore double getSaldo() {
		return entrada - saida;
	}
}
