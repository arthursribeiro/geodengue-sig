package br.edu.ufcg.geodengue.client.eventos;

public class BooleanEvento extends EventoBase {

	private boolean valor;
	
	public BooleanEvento(TiposDeEventos tipo, boolean valor) {
		super(tipo);
		this.valor = valor;
	}

	public boolean getValor() {
		return valor;
	}

}
