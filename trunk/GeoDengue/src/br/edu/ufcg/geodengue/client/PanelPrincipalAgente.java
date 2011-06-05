package br.edu.ufcg.geodengue.client;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.geodengue.client.eventos.Assinante;
import br.edu.ufcg.geodengue.client.eventos.BooleanEvento;
import br.edu.ufcg.geodengue.client.eventos.CadastrarNovoPontoEvento;
import br.edu.ufcg.geodengue.client.eventos.EventBus;
import br.edu.ufcg.geodengue.client.eventos.TiposDeEventos;
import br.edu.ufcg.geodengue.client.service.GeoDengueService;
import br.edu.ufcg.geodengue.client.service.GeoDengueServiceAsync;
import br.edu.ufcg.geodengue.shared.PontoDTO;
import br.edu.ufcg.geodengue.shared.SessaoDTO;

import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PanelPrincipalAgente extends Composite {

	private enum Estado {
		CADASTRAR_FOCO,
		CADASTRAR_PESSOA;
	}

	private enum Camada {
		AREA_AGENTES,
		FOCOS,
		PESSOAS_CONTAMINADAS;
	}

	private SessaoDTO sessao;
	private MapWidget mapWidget;
	private List<Estado> estados;
	private List<Camada> camadas;

	private PanelCamadas panelCamadas;
	private PanelAcoes panelAcoes;
	
	private Marker marcador;
	private Overlay bairros;
	private Overlay focos;
	
	private GeoDengueServiceAsync server = GWT.create(GeoDengueService.class);
	

	public PanelPrincipalAgente(SessaoDTO sessao) {
		this.sessao = sessao;
		this.estados = new ArrayList<Estado>();
		this.camadas = new ArrayList<Camada>();

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
		
		panelAcoes = new PanelAcoes();
		RootPanel.get("acoes").add(panelAcoes);

		initWidget(panelCompleto);

		EventBus.getInstance().registraAssinante(TiposDeEventos.CHECKBOX_AREA_AGENTE, new TrataMudancaDeAreaAgente());
		EventBus.getInstance().registraAssinante(TiposDeEventos.CHECKBOX_FOCOS, new TrataMudancaDeFoco());
		EventBus.getInstance().registraAssinante(TiposDeEventos.CHECKBOX_PESSOAS_CONTAMINADAS, new TrataMudancaDePessoasContaminadas());
		EventBus.getInstance().registraAssinante(TiposDeEventos.CADASTRAR_FOCO_CLICADO, new TrataCliqueCadastrarFoco());
		EventBus.getInstance().registraAssinante(TiposDeEventos.CADASTRAR_PESSOA_CLICADO, new TrataCliqueCadastrarPessoa());
		EventBus.getInstance().registraAssinante(TiposDeEventos.CADASTRAR_NOVO_PONTO, new TrataCadastrarNovoFoco());
		EventBus.getInstance().registraAssinante(TiposDeEventos.CADASTRAR_NOVO_PONTO, new TrataCadastrarNovaPessoa());
	}

	private void mudouDadosDoMapa(){
		LatLngBounds bounds = mapWidget.getBounds();
		

		String hei = mapWidget.getSize().getHeight()+"";
		String wid = mapWidget.getSize().getWidth()+"";

		if (camadas.contains(Camada.AREA_AGENTES)) {
			String boundsPoints = "";
			boundsPoints += (bounds.getSouthWest().getLongitude())+",";
			boundsPoints += (bounds.getSouthWest().getLatitude())+",";
			boundsPoints += (bounds.getNorthEast().getLongitude())+",";
			boundsPoints += (bounds.getNorthEast().getLatitude());

			String url = Constantes.GEOSERVER_MAPA_BAIRROS;
			url += "&styles=&bbox="+boundsPoints+"&width="+wid+"&height="+hei;
			
			tryRemoveOverlay(bairros);
			bairros = new GroundOverlay(url, bounds);
			mapWidget.addOverlay(bairros);
		}
		
		if (camadas.contains(Camada.FOCOS)) {
			String boundsPoints = "";
			boundsPoints += (bounds.getSouthWest().getLatitude())+",";
			boundsPoints += (bounds.getSouthWest().getLongitude())+",";
			boundsPoints += (bounds.getNorthEast().getLatitude())+",";
			boundsPoints += (bounds.getNorthEast().getLongitude());
			
			String url = Constantes.GEOSERVER_MAPA_FOCOS;
			url += "&styles=&bbox="+boundsPoints+"&width="+wid+"&height="+hei;
						
			System.err.println(url);
			
			tryRemoveOverlay(focos);
			focos = new GroundOverlay(url, bounds);
			mapWidget.addOverlay(focos);
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
		if (estados.contains(Estado.CADASTRAR_FOCO) || estados.contains(Estado.CADASTRAR_PESSOA))
			cadastrarFocoOuPessoa(latLng);
	}

	private void cadastrarFocoOuPessoa(LatLng latLng) {
		if(this.marcador == null) {
			MarkerOptions opt = MarkerOptions.newInstance();
			opt.setDraggable(true);
			this.marcador = new Marker(latLng, opt);
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
	
	private void trataCliqueCadastro(Estado estado, String texto, boolean valor) {
		RootPanel.get("dinamico").clear();
		estados.clear();
		if(valor) {
			RootPanel.get("dinamico").add(new PanelCadastraPonto(texto, true));
			estados.add(estado);
			if (marcador != null) {
				marcador = null;
				tryRemoveOverlay(marcador);
			}
		} else {
			tryRemoveOverlay(marcador);
		}
		marcador = null;
	}
	
	private void chamaServidorCadastrarPonto(String descricao, char tipo) {
		
		final AsyncCallback<Boolean> cadastraCallBack = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Ocorreu um erro na comunicacao com o Servidor! =X");
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					Window.alert("Cadastrado com Sucesso! \\o/");
					panelAcoes.untoggleButtons();
				} else {
					Window.alert("Ocorreu um erro durante o Cadastro =/");
				}
			}
			
		};
		
		server.cadastraNovoPonto(new PontoDTO(descricao, marcador.getLatLng().getLatitude(), marcador.getLatLng().getLongitude(), tipo), cadastraCallBack);
	}
	
	/*
	 * CLASSES INTERNAS PARA TRATAMENTO DE EVENTOS!
	 */

	private class TrataMudancaDeAreaAgente implements Assinante<BooleanEvento> {
		@Override
		public void trataEvento(BooleanEvento evento) {
			if (evento.getValor()) camadas.add(Camada.AREA_AGENTES);
			else {
				camadas.remove(Camada.AREA_AGENTES);
				tryRemoveOverlay(bairros);
			}
			mudouDadosDoMapa();
		}
	}

	private class TrataMudancaDeFoco implements Assinante<BooleanEvento> {
		@Override
		public void trataEvento(BooleanEvento evento) {
			if (evento.getValor()) camadas.add(Camada.FOCOS);
			else {
				camadas.remove(Camada.FOCOS);
				tryRemoveOverlay(focos);
			}
			mudouDadosDoMapa();
		}
	}

	private class TrataMudancaDePessoasContaminadas implements Assinante<BooleanEvento> {
		@Override
		public void trataEvento(BooleanEvento evento) {
			// TODO Codigo para executar quando Usuario clica no CheckBox da Camada Pessoas Contaminadas
		}
	}

	private class TrataCliqueCadastrarFoco implements Assinante<BooleanEvento> {
		@Override
		public void trataEvento(BooleanEvento evento) {
			trataCliqueCadastro(Estado.CADASTRAR_FOCO, "Texto Ajuda Para Cadastro de Foco", evento.getValor());
		}
	}
	
	private class TrataCliqueCadastrarPessoa implements Assinante<BooleanEvento> {
		@Override
		public void trataEvento(BooleanEvento evento) {
			trataCliqueCadastro(Estado.CADASTRAR_PESSOA, "Texto Ajuda Para Cadastro de Pessoa", evento.getValor());
		}
	}
	
	private class TrataCadastrarNovoFoco implements Assinante<CadastrarNovoPontoEvento> {
		@Override
		public void trataEvento(CadastrarNovoPontoEvento evento) {
			if(evento.isFoco()) {
				if (marcador == null) {
					Window.alert("Necessário marcar o ponto no mapa!");
					return;
				}
				chamaServidorCadastrarPonto(evento.getDescricao(), 'F');
			}
		}
	}
	
	private class TrataCadastrarNovaPessoa implements Assinante<CadastrarNovoPontoEvento> {
		@Override
		public void trataEvento(CadastrarNovoPontoEvento evento) {
			if (!evento.isFoco()) {
				if (marcador == null) {
					Window.alert("Necessário marcar o ponto no mapa!");
					return;
				}
				chamaServidorCadastrarPonto(evento.getDescricao(), 'P');
			}
		}
	}

}
