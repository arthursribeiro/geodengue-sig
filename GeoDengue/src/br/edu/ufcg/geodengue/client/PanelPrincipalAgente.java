package br.edu.ufcg.geodengue.client;

import br.edu.ufcg.geodengue.shared.SessaoDTO;

import com.google.gwt.maps.client.MapUIOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PanelPrincipalAgente extends Composite {

	private SessaoDTO sessao;

	private MapWidget mapWidget;
	
	public PanelPrincipalAgente(SessaoDTO sessao) {
		this.sessao = sessao;
		
		mapWidget = new MapWidget(LatLng.newInstance(-7.231188, -35.886669), 13);
		mapWidget.setSize("700px", "600px");
		
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
		
		panelMapaEFiltros.add(decoratorPanelMapa);
		
		PanelFiltros panelFiltros = new PanelFiltros(mapWidget);
		panelMapaEFiltros.add(panelFiltros);
		
		DecoratorPanel decoratorPanelDados = new DecoratorPanel();
		decoratorPanelDados.add(criaPanelDados());
		
		DecoratorPanel decoratorPanelAcoes = new DecoratorPanel();
		decoratorPanelAcoes.add(criaPanelAcoes());
		
		panelCompleto.add(decoratorPanelDados);
		panelCompleto.add(decoratorPanelAcoes);
		panelCompleto.add(panelMapaEFiltros);
		
		initWidget(panelCompleto);
	}
	
	private HorizontalPanel criaPanelAcoes() {
		HorizontalPanel hPanel = new HorizontalPanel();
//		hPanel.setWidth("930px");Os
		hPanel.setSpacing(5);
		hPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
	    
		hPanel.add(new Button("Botao1"));
		hPanel.add(new Button("Botao2"));
		hPanel.add(new Button("Botao3"));
		hPanel.add(new Button("Botao4"));
		
		return hPanel;
	}
	
	private VerticalPanel criaPanelDados() {
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setSize("930px" , "80px");
		
		vPanel.add(new Label("Area de Cobertura: "+sessao.getAgente().getAreaCobertura()));
		vPanel.add(new Label("Comprimento da Rota: "+sessao.getAgente().getComprimentoRota()));
		vPanel.add(new Label("Focos Responsavel: "+sessao.getAgente().getFocosResponsavel()));
		vPanel.add(new Label("Focos na Rota: "+sessao.getAgente().getFocosNaRota()));
		return vPanel;
	}
	
}
