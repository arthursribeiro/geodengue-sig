package br.edu.ufcg.geodengue.shared;

import java.io.Serializable;

public class SimularDTO implements Serializable {

	private static final long serialVersionUID = 7429031334641830412L;

	private int id;
	private String nome;
	private String descricao;
	
	public SimularDTO() { }
	
	public SimularDTO(int id, String nome, String descricao) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
	}

	public int getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getDescricao() {
		return descricao;
	}
}
