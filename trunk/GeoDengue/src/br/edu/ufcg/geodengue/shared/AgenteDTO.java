package br.edu.ufcg.geodengue.shared;

import java.io.Serializable;

public class AgenteDTO implements Serializable {

	private static final long serialVersionUID = 1716565210490442915L;
	
	private String nome;
	
	private int areaCobertura;
	private int focosResponsavel;
	private int comprimentoRota;
	private int focosNaRota;

	public AgenteDTO() { }

	public AgenteDTO(String nome, int areaCobertura, int focosResponsavel,
			int comprimentoRota, int focosNaRota) {
		this.nome = nome;
		this.areaCobertura = areaCobertura;
		this.focosResponsavel = focosResponsavel;
		this.comprimentoRota = comprimentoRota;
		this.focosNaRota = focosNaRota;
	}
	
	public String getNome() {
		return nome;
	}

	public int getAreaCobertura() {
		return areaCobertura;
	}

	public int getFocosResponsavel() {
		return focosResponsavel;
	}

	public int getComprimentoRota() {
		return comprimentoRota;
	}

	public int getFocosNaRota() {
		return focosNaRota;
	}
}
