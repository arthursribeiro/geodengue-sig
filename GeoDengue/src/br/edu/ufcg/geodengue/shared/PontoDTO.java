package br.edu.ufcg.geodengue.shared;

import java.io.Serializable;

public class PontoDTO implements Serializable {

	private static final long serialVersionUID = 7789787012069960980L;

	private int id;
	
	private String descricao;
	private double latitude;
	private double longitude;
	private char tipo;
	
	public PontoDTO() { }
	
	public PontoDTO(String descricao, double latitude, double longitude, char tipo) {
		this.descricao = descricao;
		this.latitude = latitude;
		this.longitude = longitude;
		this.tipo = tipo;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public char getTipo() {
		return tipo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
