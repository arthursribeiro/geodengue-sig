package br.edu.ufcg.geodengue.shared;

import java.io.Serializable;

public class SessaoDTO implements Serializable {
	private static final long serialVersionUID = 5601920317052491442L;

	private AgenteDTO agente;

	public SessaoDTO() { }
	
	public SessaoDTO(AgenteDTO agente) {
		this.agente = agente;
	}

	public AgenteDTO getAgente() {
		return agente;
	}
	
}
