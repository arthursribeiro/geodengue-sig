package br.edu.ufcg.geodengue.client;

import br.edu.ufcg.geodengue.client.eventos.Assinante;
import br.edu.ufcg.geodengue.client.eventos.AtualizarMapaEvento;
import br.edu.ufcg.geodengue.client.eventos.EventBus;
import br.edu.ufcg.geodengue.client.eventos.MarcadorArrastadoEvento;
import br.edu.ufcg.geodengue.client.eventos.TiposDeEventos;
import br.edu.ufcg.geodengue.client.utils.Camada;
import br.edu.ufcg.geodengue.client.utils.Estado;
import br.edu.ufcg.geodengue.shared.SessaoDTO;

import com.google.gwt.maps.client.MapUIOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MapMoveEndHandler;
import com.google.gwt.maps.client.event.MarkerDragEndHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.overlay.GroundOverlay;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;

public class PanelMapa extends Composite {

	private MapWidget mapWidget;

	private Overlay bairros;
	private Overlay focos;
	private Overlay pessoas;
	private Marker marcador;
	private Polygon poligonoRaio;

	public PanelMapa(SessaoDTO sessao) {
		mapWidget = new MapWidget(LatLng.newInstance(-7.231188, -35.886669), 13);
		mapWidget.setSize("700px", "600px");
		mapWidget.addMapClickHandler(new MapClickHandler() {

			@Override
			public void onClick(MapClickEvent event) {
				trataCliqueNoMapa(event.getLatLng());
			}
		});

		mapWidget.addMapMoveEndHandler(new MapMoveEndHandler() {

			@Override
			public void onMoveEnd(MapMoveEndEvent event) {
				mudouDadosDoMapa();
			}
		});

		MapUIOptions options = mapWidget.getDefaultUI();
		options.setScrollwheel(true);
		options.setDoubleClick(false);
		options.setLargeMapControl3d(true);
		mapWidget.setUI(options);
		mapWidget.setDoubleClickZoom(false);
		mapWidget.setDraggable(true);

		DecoratorPanel decoratorPanelMapa = new DecoratorPanel();
		decoratorPanelMapa.add(mapWidget);

		initWidget(decoratorPanelMapa);

		EventBus.getInstance().registraAssinante(TiposDeEventos.ATUALIZAR_MAPA, new TrataAtualizarMapa());
	}

	private void mudouDadosDoMapa(){
		LatLngBounds bounds = mapWidget.getBounds();

		tryRemoveOverlay(bairros);
		tryRemoveOverlay(focos);
		tryRemoveOverlay(pessoas);
		tryRemoveOverlay(poligonoRaio);
		
		String hei = mapWidget.getSize().getHeight()+"";
		String wid = mapWidget.getSize().getWidth()+"";

		PanelPrincipal panelPrincipal = PanelPrincipal.getInstance();
		
		if (panelPrincipal.camadaAtiva(Camada.AREA_AGENTES)) {
			String boundsPoints = "";
			boundsPoints += (bounds.getSouthWest().getLongitude())+",";
			boundsPoints += (bounds.getSouthWest().getLatitude())+",";
			boundsPoints += (bounds.getNorthEast().getLongitude())+",";
			boundsPoints += (bounds.getNorthEast().getLatitude());

			String url = Constantes.GEOSERVER_MAPA_BAIRROS;
			url += "&styles=&bbox="+boundsPoints+"&width="+wid+"&height="+hei;
			
			bairros = new GroundOverlay(url, bounds);
			mapWidget.addOverlay(bairros);
		}
		
		if (panelPrincipal.camadaAtiva(Camada.PESSOAS_RAIO)) {
			if (poligonoRaio != null) mapWidget.addOverlay(poligonoRaio);
		}
		
		if (panelPrincipal.camadaAtiva(Camada.FOCOS)) {
			String boundsPoints = "";
			boundsPoints += (bounds.getSouthWest().getLongitude())+",";
			boundsPoints += (bounds.getSouthWest().getLatitude())+",";
			boundsPoints += (bounds.getNorthEast().getLongitude())+",";
			boundsPoints += (bounds.getNorthEast().getLatitude());
			
			String url = Constantes.GEOSERVER_MAPA_FOCOS;
			url += "&styles=&bbox="+boundsPoints+"&width="+wid+"&height="+hei;
						
			focos = new GroundOverlay(url, bounds);
			mapWidget.addOverlay(focos);
		}
		
		if (panelPrincipal.camadaAtiva(Camada.PESSOAS_CONTAMINADAS)) {
			String boundsPoints = "";
			boundsPoints += (bounds.getSouthWest().getLongitude())+",";
			boundsPoints += (bounds.getSouthWest().getLatitude())+",";
			boundsPoints += (bounds.getNorthEast().getLongitude())+",";
			boundsPoints += (bounds.getNorthEast().getLatitude());
			
			String url = Constantes.GEOSERVER_MAPA_PESSOAS;
			url += "&styles=&bbox="+boundsPoints+"&width="+wid+"&height="+hei;
						
			pessoas = new GroundOverlay(url, bounds);
			mapWidget.addOverlay(pessoas);
		}
	}
	
	private void trataCliqueNoMapa(LatLng latLng) {
		PanelPrincipal panelPrincipal = PanelPrincipal.getInstance();
		
		if (panelPrincipal.botaoAtivo(Estado.CADASTRAR_FOCO)
				|| panelPrincipal.botaoAtivo(Estado.CADASTRAR_PESSOA)
				|| panelPrincipal.botaoAtivo(Estado.CALCULAR_PESSOA_RAIO)) {
			cadastrarFocoOuPessoa(latLng);
		}
	}

	private void cadastrarFocoOuPessoa(LatLng latLng) {
		if(this.marcador == null) {
			MarkerOptions opt = MarkerOptions.newInstance();
			opt.setDraggable(true);
			this.marcador = new Marker(latLng, opt);
			this.marcador.addMarkerDragEndHandler(new MarkerDragEndHandler() {
				
				@Override
				public void onDragEnd(MarkerDragEndEvent event) {
					EventBus.getInstance().publica(new MarcadorArrastadoEvento());
					tryRemoveOverlay(poligonoRaio);
				}
			});
			mapWidget.addOverlay(marcador);
		}
	}
	
	private void tryRemoveOverlay(Overlay overlay) {
		try {
			mapWidget.removeOverlay(overlay);
		} catch (Exception e) {
//			e.printStackTrace();
		}		
	}

	public Marker getMarcador() {
		return this.marcador;
	}

	public void retiraMarcador() {
		if (marcador != null) {
			tryRemoveOverlay(marcador);
			marcador = null;
		}
	}
	
	/*
	 * CLASSES INTERNAS PARA TRATAMENTO DE EVENTOS!
	 */

	private class TrataAtualizarMapa implements Assinante<AtualizarMapaEvento> {
		@Override
		public void trataEvento(AtualizarMapaEvento evento) {
			mudouDadosDoMapa();
		}
	}

	public void adicionaPoligono(Polygon poligono) {
		tryRemoveOverlay(poligonoRaio);
		this.poligonoRaio = poligono;
		mapWidget.addOverlay(poligonoRaio);
	}

}
