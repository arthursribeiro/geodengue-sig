package br.edu.ufcg.geodengue.client.eventos;

import java.util.EventListener;

public interface Assinante<E extends EventoBase> extends EventListener {
	public void trataEvento(E evento);
}
