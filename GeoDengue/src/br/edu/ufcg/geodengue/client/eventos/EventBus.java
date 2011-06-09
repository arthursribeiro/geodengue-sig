package br.edu.ufcg.geodengue.client.eventos;

import java.util.ArrayList;
import java.util.HashMap;

public class EventBus {
	
	private HashMap<TiposDeEventos, ArrayList<Assinante<EventoBase>>> assinantes;

	private static EventBus singletonInstance;
	
	private EventBus() {
		this.assinantes = new HashMap<TiposDeEventos, ArrayList<Assinante<EventoBase>>>();
	}
	
	public static EventBus getInstance() {
		if (singletonInstance == null) {
			singletonInstance = new EventBus();
		}
		return singletonInstance;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void registraAssinante(TiposDeEventos tipoDeEvento, Assinante assinante) {
		if (tipoDeEvento == null) {
			throw new IllegalArgumentException("tipoDeEvento nao pode ser null");
		}
		if (assinante == null) {
			throw new IllegalArgumentException("assinante nao pode ser null");
		}

		if (!this.assinantes.containsKey(tipoDeEvento)) {
			this.assinantes.put(tipoDeEvento, new ArrayList<Assinante<EventoBase>>());
		}

		this.assinantes.get(tipoDeEvento).add(assinante);
	}

	@SuppressWarnings("rawtypes")
	public void removeAssinante(TiposDeEventos tipoDeEvento, Assinante assinante) {
		if (tipoDeEvento == null) {
			throw new IllegalArgumentException("tipoDeEvento nao pode ser null");
		}
		if (assinante == null) {
			throw new IllegalArgumentException("assinante nao pode ser null");
		}

		if (!this.assinantes.containsKey(tipoDeEvento)) {
			throw new IllegalArgumentException("assinante nao cadastrado");
		}
		
		this.assinantes.get(tipoDeEvento).remove(assinante);
	}
	
	public void publica(EventoBase evento) {
		if (evento == null) {
			throw new IllegalArgumentException("evento nao pode ser null");
		}

		if (this.assinantes.get(evento.getTipo()) == null) {
			return;
		}

		for (Assinante<EventoBase> assinante : this.assinantes.get(evento.getTipo())) {
			assinante.trataEvento(evento);
		}
	}

}
