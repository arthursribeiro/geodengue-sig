package br.edu.ufcg.geodengue.client;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.geodengue.client.eventos.Assinante;
import br.edu.ufcg.geodengue.client.eventos.AtualizarMapaEvento;
import br.edu.ufcg.geodengue.client.eventos.EventBus;
import br.edu.ufcg.geodengue.client.eventos.MarcadorArrastadoEvento;
import br.edu.ufcg.geodengue.client.eventos.TiposDeEventos;
import br.edu.ufcg.geodengue.client.service.GeoDengueService;
import br.edu.ufcg.geodengue.client.service.GeoDengueServiceAsync;
import br.edu.ufcg.geodengue.client.utils.Camada;
import br.edu.ufcg.geodengue.client.utils.Estado;
import br.edu.ufcg.geodengue.shared.PontoDTO;
import br.edu.ufcg.geodengue.shared.TooltipDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
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
import com.google.gwt.maps.client.overlay.Polyline;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;

public class PanelMapa extends Composite {

	private MapWidget mapWidget;
	private GeoDengueServiceAsync server = GWT.create(GeoDengueService.class);

	private Overlay bairros;
	private Overlay focos;
	private Overlay pessoas;
	private Overlay agentes;
	private Marker marcador;
	private Polygon poligonoRaio;
	private InfoWindow infoWindow = null;
	private Polyline rota;
	private List<Marker> marca;
	
	public PanelMapa() {
		marca = new ArrayList<Marker>();
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
		options.setScaleControl(true);
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
		tryRemoveOverlay(agentes);
		tryRemoveOverlay(poligonoRaio);
		tryRemoveOverlay(rota);
		
		for (Marker ma : marca) {
			tryRemoveOverlay(ma);
		}

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
		
		if (panelPrincipal.camadaAtiva(Camada.ROTA)) {
			if (rota != null) {
				mapWidget.addOverlay(rota);
				for (Marker ma : marca) {
					mapWidget.addOverlay(ma);
				}
			}
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
		
		if (panelPrincipal.camadaAtiva(Camada.AGENTES)) {
			String boundsPoints = "";
			boundsPoints += (bounds.getSouthWest().getLongitude())+",";
			boundsPoints += (bounds.getSouthWest().getLatitude())+",";
			boundsPoints += (bounds.getNorthEast().getLongitude())+",";
			boundsPoints += (bounds.getNorthEast().getLatitude());

			String url = Constantes.GEOSERVER_MAPA_AGENTES;
			url += "&styles=&bbox="+boundsPoints+"&width="+wid+"&height="+hei;

			agentes = new GroundOverlay(url, bounds);
			mapWidget.addOverlay(agentes);
		}
	}

	private void trataCliqueNoMapa(LatLng latLng) {
		PanelPrincipal panelPrincipal = PanelPrincipal.getInstance();

		if (marcador == null && (panelPrincipal.botaoAtivo(Estado.CADASTRAR_FOCO)
				|| panelPrincipal.botaoAtivo(Estado.CADASTRAR_PESSOA)
				|| panelPrincipal.botaoAtivo(Estado.CADASTRAR_AGENTE)
				|| panelPrincipal.botaoAtivo(Estado.FOCOS_DISTANCIA)
				|| panelPrincipal.botaoAtivo(Estado.CALCULAR_PESSOA_RAIO))) {
			cadastrarFocoOuPessoa(latLng);
		} else if(panelPrincipal.botaoAtivo(Estado.DISTANCIA_DOIS_FOCOS)) {
			trataDistanciaDoisFocos(latLng);
		} else if (panelPrincipal.botaoAtivo(Estado.AREA_AGENTE) || panelPrincipal.botaoAtivo(Estado.ROTA) || panelPrincipal.botaoAtivo(Estado.SIMULA_DEMITIR)) {
			trataGetDadosAgente(latLng);
		} else {
			adicionaToolTip(latLng);
		}
	}

	private void trataGetDadosAgente(LatLng latLng) {
		final AsyncCallback<PontoDTO> distanciaCallBack = new AsyncCallback<PontoDTO>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Ocorreu um erro na comunicacao com o Servidor! =X");
			}

			@Override
			public void onSuccess(PontoDTO result) {
				if(result != null) {
					PanelPrincipal panelPrincipal = PanelPrincipal.getInstance();
					if (panelPrincipal.botaoAtivo(Estado.ROTA)) {
						PanelRotaAgente panelRotaAgente = PanelPrincipal.getInstance().getPanelRotaAgente();
						panelRotaAgente.povoa(result);
					} else if (panelPrincipal.botaoAtivo(Estado.SIMULA_DEMITIR)) {
						PanelSimulaDemitir panelSimulaDemitir = PanelPrincipal.getInstance().getPanelSimulaDemitir();
						panelSimulaDemitir.povoa(result);
					} else {
						PanelAreaAgente panelAreaAgente = PanelPrincipal.getInstance().getPanelAreaAgente();
						panelAreaAgente.povoa(result);
					}
				}
			}

		};

		server.recuperaAgente(latLng.getLatitude(), latLng.getLongitude(), distanciaCallBack);
	}

	private void trataDistanciaDoisFocos(final LatLng latLongitude) {
		final AsyncCallback<PontoDTO> distanciaCallBack = new AsyncCallback<PontoDTO>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Ocorreu um erro na comunicacao com o Servidor! =X");
			}

			@Override
			public void onSuccess(PontoDTO result) {
				if(result != null) {
					PanelDistanciaFocos panelDistancia = PanelPrincipal.getInstance().getPanelDistancia();
					panelDistancia.novoFoco(result);
				}
			}

		};

		server.recuperaFoco(latLongitude.getLatitude(), latLongitude.getLongitude(), distanciaCallBack);
	}

	private void adicionaToolTip(final LatLng latLongitude) {

		try {
			infoWindow.close();
		} catch (Exception e) {
			// TODO: handle exception
		}

		final AsyncCallback<TooltipDTO> tooltipCallBack = new AsyncCallback<TooltipDTO>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Ocorreu um erro na comunicacao com o Servidor! =X");
			}

			@Override
			public void onSuccess(TooltipDTO result) {
				PanelPrincipal panelPrincipal = PanelPrincipal.getInstance();

				String toolTipoText = "";

				// TODO melhorar isso.. ta estranho o tooltip, sem contar o código repetido.
				if (panelPrincipal.camadaAtiva(Camada.AREA_AGENTES) && !result.getBairros().isEmpty()) {
					toolTipoText += "<center><h3>Bairro"+(result.getBairros().size() == 1 ? "" : "S")+"</h3></center>";
					for (String desc : result.getBairros()) {
						toolTipoText += "<p><b>Nome: </b>"+desc+"</p>";
					}
				}

				if (panelPrincipal.camadaAtiva(Camada.FOCOS) && !result.getFocos().isEmpty()) {
					toolTipoText += "<center><h3>FOCO"+(result.getFocos().size() == 1 ? "" : "S")+"</h3></center>";
					for (String desc : result.getFocos()) {
						toolTipoText += "<p><b>Descrição: </b>"+desc+"</p>";
					}
				}

				if (panelPrincipal.camadaAtiva(Camada.PESSOAS_CONTAMINADAS) && !result.getPessoas().isEmpty()) {
					toolTipoText += "<center><h3>PESSOA"+(result.getPessoas().size() == 1 ? "" : "S")+"</h3></center>";
					for (String desc : result.getPessoas()) {
						toolTipoText += "<p><b>Descrição: </b>"+desc+"</p>";
					}
				}
				
				if (panelPrincipal.camadaAtiva(Camada.AGENTES) && !result.getAgentes().isEmpty()) {
					toolTipoText += "<center><h3>AGENTE"+(result.getAgentes().size() == 1 ? "" : "S")+"</h3></center>";
					for (String desc : result.getAgentes()) {
						toolTipoText += "<p><b>Nome do Agente: </b>"+desc+"</p>";
					}
				}

				if (!toolTipoText.isEmpty()) adicionaTooltip(result.getLongitude(), result.getLatitude(), toolTipoText);
			}

		};

		server.recuperaDadosTooltip(latLongitude.getLatitude(), latLongitude.getLongitude(), tooltipCallBack);
	}

	private void adicionaTooltip(double latitude, double longitude, String texto) {
		if (infoWindow != null) {
			infoWindow.close();
		}

		infoWindow = mapWidget.getInfoWindow();
		infoWindow.open(LatLng.newInstance(longitude, latitude), new InfoWindowContent(texto));
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

	public void removePoligonoPessoasRaio() {
		tryRemoveOverlay(poligonoRaio);
	}
	
	public void adicionaPolyline(Polyline novaRota, List<Marker> lista) {
		tryRemoveOverlay(rota);
		for (Marker ma : marca) {
			tryRemoveOverlay(ma);
		}
		this.rota = novaRota;
		this.marca = lista;
		mapWidget.addOverlay(rota);
		for (Marker ma : marca) {
			mapWidget.addOverlay(ma);
		}
	}

	public void removePolyline() {
		tryRemoveOverlay(rota);
		for (Marker ma : marca) {
			tryRemoveOverlay(ma);
		}
		marca = new ArrayList<Marker>();
		rota = null;
	}

	public MapWidget getMapWidget() {
		return this.mapWidget;
	}
	
}
