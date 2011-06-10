package br.edu.ufcg.geodengue.shared;

import java.io.Serializable;
import java.util.List;

public class AreaAgenteDTO implements Serializable {

	private static final long serialVersionUID = 7802090286201431995L;

	private List<String> pontos;
	private double area;
	
	public AreaAgenteDTO() { }
	
	public AreaAgenteDTO(List<String> pontos, double area) {
		this.pontos = pontos;
		this.area = area;
	}
	
	public List<String> getPontos() {
		return pontos;
	}
	
	public double getArea() {
		return area;
	}
	
}
