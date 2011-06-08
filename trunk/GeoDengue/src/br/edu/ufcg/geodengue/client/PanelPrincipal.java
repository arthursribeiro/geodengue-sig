package br.edu.ufcg.geodengue.client;

import br.edu.ufcg.geodengue.client.utils.Camada;
import br.edu.ufcg.geodengue.client.utils.Estado;

import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class PanelPrincipal extends Composite {

	private static PanelPrincipal singletonInstance;
	
	private PanelAcoes panelAcoes;
	private PanelCamadas panelCamadas;
//	private PanelDados panelDados;
	private PanelMapa panelMapa;
	
	private PanelPrincipal() {
	}
	
	private void criaComponentes() {
//		panelDados = new PanelDados(sessao.getAgente());
		panelAcoes = new PanelAcoes();
		
		HorizontalPanel panelMapaECamadas = new HorizontalPanel();
		panelMapaECamadas.setSpacing(10);
		
		panelCamadas = new PanelCamadas();
		panelMapaECamadas.add(panelCamadas);

		panelMapa = new PanelMapa();
		panelMapaECamadas.add(panelMapa);

//		RootPanel.get("dados").add(panelDados);
		RootPanel.get("acoes").add(panelAcoes);
		RootPanel.get("container").add(panelMapaECamadas);
	}

	public static void inicia() {
		if (singletonInstance == null) {
			singletonInstance = new PanelPrincipal();
			singletonInstance.criaComponentes();
		} else {
			throw new IllegalStateException("Ja existe um PanelPrincipal!");
		}
	}
	
	public static PanelPrincipal getInstance() {
		if (singletonInstance == null) {
			throw new IllegalStateException("Opa!");
		}
		return singletonInstance;
	}
	
	public boolean camadaAtiva(Camada camada) {
		return panelCamadas.isCamadaAtiva(camada);
	}
	
	public boolean botaoAtivo(Estado estado) {
		return panelAcoes.isBotaoAtivo(estado);
	}
	
	public Marker getMarcador() {
		return panelMapa.getMarcador();
	}
	
	public void retiraMarcador() {
		panelMapa.retiraMarcador();
	}
	
	public void limpaDinamico() {
		panelAcoes.limpaDinamico();
	}
	
	public void untoggle() {
		panelAcoes.untoggleButtons();
	}
	
	public void adicionaPoligonoNoMapa(Polygon poligono) {
		panelMapa.adicionaPoligono(poligono);
	}
	
	public void adicionaCamada(Camada camada) {
		panelCamadas.adicionaCamada(camada);
	}
	
	public void removeCamada(Camada camada) {
		panelCamadas.removeCamada(camada);
	}
	
	public void removePoligonoPessoasRaio() {
		panelMapa.removePoligonoPessoasRaio();
	}
	
	public PanelDistanciaFocos getPanelDistancia() {
		return panelAcoes.getPanelDistancia();
	}
	
}
