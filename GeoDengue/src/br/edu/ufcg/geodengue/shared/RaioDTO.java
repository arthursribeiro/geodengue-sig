package br.edu.ufcg.geodengue.shared;

import java.io.Serializable;

public class RaioDTO implements Serializable {

	private static final long serialVersionUID = 7789787012069960980L;

	private double raio;
	private double latitude;
	private double longitude;
	
	public RaioDTO() { }
	
	public RaioDTO(double latitude, double longitude, double raio) {
		this.raio = raio;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getRaio() {
		return raio;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
}