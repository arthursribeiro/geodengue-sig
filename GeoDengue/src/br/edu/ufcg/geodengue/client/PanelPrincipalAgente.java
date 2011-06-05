package br.edu.ufcg.geodengue.client;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.geodengue.client.eventos.Assinante;
import br.edu.ufcg.geodengue.client.eventos.BooleanEvento;
import br.edu.ufcg.geodengue.client.eventos.CadastrarNovoFocoEvento;
import br.edu.ufcg.geodengue.client.eventos.EventBus;
import br.edu.ufcg.geodengue.client.eventos.TiposDeEventos;
import br.edu.ufcg.geodengue.shared.SessaoDTO;

import com.google.gwt.maps.client.MapUIOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MapMoveEndHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.overlay.GroundOverlay;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PanelPrincipalAgente extends Composite {

	private enum Estado {
		AREA_AGENTES,
		CADASTRAR_FOCO;
	}

	private SessaoDTO sessao;
	private MapWidget mapWidget;
	private List<Estado> estados;
	private Marker marcador;
	private PanelCamadas panelCamadas;
	private Overlay bairros;

	public PanelPrincipalAgente(SessaoDTO sessao) {
		this.sessao = sessao;
		this.estados = new ArrayList<Estado>();

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
				mudouBairros();
			}
		});

		VerticalPanel panelCompleto = new VerticalPanel();
		panelCompleto.setSpacing(10);
		panelCompleto.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		HorizontalPanel panelMapaEFiltros = new HorizontalPanel();
		panelMapaEFiltros.setSpacing(10);

		MapUIOptions options = mapWidget.getDefaultUI();
		options.setScrollwheel(true);
		options.setDoubleClick(false);
		options.setLargeMapControl3d(true);
		mapWidget.setUI(options);
		mapWidget.setDoubleClickZoom(false);
		mapWidget.setDraggable(true);

		DecoratorPanel decoratorPanelMapa = new DecoratorPanel();
		decoratorPanelMapa.add(mapWidget);

		VerticalPanel panelCamadasEFiltros = new VerticalPanel();
		panelCamadasEFiltros.setSpacing(5);

		PanelFiltros panelFiltros = new PanelFiltros(mapWidget);
		panelCamadas = new PanelCamadas();
		panelCamadasEFiltros.add(panelCamadas);
		panelCamadasEFiltros.add(panelFiltros);

		panelMapaEFiltros.add(panelCamadasEFiltros);

		panelMapaEFiltros.add(decoratorPanelMapa);

		DecoratorPanel decoratorPanelDados = new DecoratorPanel();
		decoratorPanelDados.add(criaPanelDados());

		panelCompleto.add(panelMapaEFiltros);

		RootPanel.get("dados").add(decoratorPanelDados);
		RootPanel.get("acoes").add(new PanelAcoes());

		initWidget(panelCompleto);

		EventBus.getInstance().registraAssinante(TiposDeEventos.CHECKBOX_AREA_AGENTE, new TrataMudancaDeAreaAgente());
		EventBus.getInstance().registraAssinante(TiposDeEventos.CHECKBOX_FOCOS, new TrataMudancaDeFoco());
		EventBus.getInstance().registraAssinante(TiposDeEventos.CHECKBOX_PESSOAS_CONTAMINADAS, new TrataMudancaDePessoasContaminadas());
		EventBus.getInstance().registraAssinante(TiposDeEventos.CADASTRAR_FOCO_CLICADO, new TrataCliqueCadastrarFato());
		EventBus.getInstance().registraAssinante(TiposDeEventos.CADASTRAR_NOVO_FOCO, new TrataCadastrarNovoFato());
	}


	private void mudouBairros(){
		if (estados.contains(Estado.AREA_AGENTES)) {
			LatLngBounds bounds = mapWidget.getBounds();

			String boundsPoints = "";
			boundsPoints += (bounds.getSouthWest().getLongitude())+",";
			boundsPoints += (bounds.getSouthWest().getLatitude())+",";
			boundsPoints += (bounds.getNorthEast().getLongitude())+",";
			boundsPoints += (bounds.getNorthEast().getLatitude());

			String hei = mapWidget.getSize().getHeight()+"";
			String wid = mapWidget.getSize().getWidth()+"";

			String url = Constantes.GEOSERVER;
			url += Constantes.GEOSERVER_WMS + "&";
			url += Constantes.GEOSERVER_GET_MAP + "&";
			url += Constantes.GEOSERVER_BAIRROS_LAYER + "&";
			url += Constantes.GEOSERVER_SRS + "&";
			url += Constantes.GEOSERVER_PNG + "&";
			url += Constantes.GEOSERVER_TRANSPARENT + "&";
			url += "styles=&bbox="+boundsPoints+"&width="+wid+"&height="+hei;
			
			tryRemoveOverlay(bairros);
			bairros = new GroundOverlay(url, mapWidget.getBounds());
			mapWidget.addOverlay(bairros);
		}
	}

	private VerticalPanel criaPanelDados() {
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setSize("930px" , "80px");

		vPanel.add(new Label("Área de Cobertura: "+sessao.getAgente().getAreaCobertura()));
		vPanel.add(new Label("Comprimento da Rota: "+sessao.getAgente().getComprimentoRota()));
		vPanel.add(new Label("Focos Responsável: "+sessao.getAgente().getFocosResponsavel()));
		vPanel.add(new Label("Focos na Rota: "+sessao.getAgente().getFocosNaRota()));
		return vPanel;
	}

	private void trataCliqueNoMapa(LatLng latLng) {
		if (estados.contains(Estado.CADASTRAR_FOCO))
			cadastrarFoco(latLng);
	}

	private void cadastrarFoco(LatLng latLng) {
		if(this.marcador == null) {
			MarkerOptions opt = MarkerOptions.newInstance();
			opt.setDraggable(true);
			this.marcador = new Marker(latLng, opt);
			mapWidget.addOverlay(marcador);
		}
	}

	private class TrataMudancaDeAreaAgente implements Assinante<BooleanEvento> {
		@Override
		public void trataEvento(BooleanEvento evento) {
			if (evento.getValor()) estados.add(Estado.AREA_AGENTES);
			else {
				estados.remove(Estado.AREA_AGENTES);
				mapWidget.removeOverlay(bairros);
			}
			mudouBairros();
		}
	}

	private class TrataMudancaDeFoco implements Assinante<BooleanEvento> {
		@Override
		public void trataEvento(BooleanEvento evento) {
			System.err.println("TrataMudancaDeFoco");
		}
	}

	private class TrataMudancaDePessoasContaminadas implements Assinante<BooleanEvento> {
		@Override
		public void trataEvento(BooleanEvento evento) {
			System.err.println("TrataMudancaDePessoasContaminadas");
		}
	}

	private class TrataCliqueCadastrarFato implements Assinante<BooleanEvento> {
		@Override
		public void trataEvento(BooleanEvento evento) {
			RootPanel.get("dinamico").clear();
			if(evento.getValor()) {
				RootPanel.get("dinamico").add(new PanelCadastraFoco());
				estados.add(Estado.CADASTRAR_FOCO);
			} else {
				estados.remove(Estado.CADASTRAR_FOCO);
				tryRemoveOverlay(marcador);
			}
			marcador = null;
		}
	}
	
	private class TrataCadastrarNovoFato implements Assinante<CadastrarNovoFocoEvento> {
		@Override
		public void trataEvento(CadastrarNovoFocoEvento evento) {
			System.err.println("TrataCadastrarNovoFato" + evento.getQntInfectados());
			System.err.println(marcador.getLatLng().getLatitude());
			System.err.println(marcador.getLatLng().getLongitude());
		}
	}
	
	private void tryRemoveOverlay(Overlay overlay) {
		try {
			mapWidget.removeOverlay(overlay);
		} catch (Exception e) {
//			e.printStackTrace();
		}		
	}

}
