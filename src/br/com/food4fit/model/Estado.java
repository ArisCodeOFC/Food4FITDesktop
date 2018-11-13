package br.com.food4fit.model;

public class Estado {
	private int id;
	private String estado;
	private String uf;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}
	@Override
	public String toString() {
		return estado;
	}

}
