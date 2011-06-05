package br.edu.ufcg.geodengue.client.eventos;

public abstract class EventoBase {
	private TiposDeEventos tipo;

	public EventoBase(TiposDeEventos tipo) {
		this.tipo = tipo;
	}

	public TiposDeEventos getTipo() {
		return this.tipo;
	}
}
