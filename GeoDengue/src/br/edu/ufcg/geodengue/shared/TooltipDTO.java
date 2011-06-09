package br.edu.ufcg.geodengue.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TooltipDTO implements Serializable {

	private static final long serialVersionUID = -6421337309638607112L;
	
	private String bairro;

	List<String> focos;
	List<String> bairros;
	List<String> pessoas;
	List<String> agentes;
	
	private double latitude;
	private double longitude;
	
	public TooltipDTO() { }

	public TooltipDTO(double latitude, double longitude) {
		this.focos = new ArrayList<String>();
		this.pessoas = new ArrayList<String>();
		this.bairros = new ArrayList<String>();
		this.agentes = new ArrayList<String>();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getBairro() {
		return bairro;
	}

	public List<String> getFocos() {
		return focos;
	}

	public List<String> getPessoas() {
		return pessoas;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public List<String> getBairros() {
		return bairros;
	}

	public List<String> getAgentes() {
		return agentes;
	}

}
