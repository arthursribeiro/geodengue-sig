package br.edu.ufcg.geodengue.client;

import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PanelCamadas extends Composite {

	private MapWidget mapWidget;
	private DecoratorPanel panelCamadas;
	
	public PanelCamadas(MapWidget mapWidget) {
		this.mapWidget = mapWidget;
	
		VerticalPanel vPanelFiltros = new VerticalPanel();
		vPanelFiltros.setSize("200px", "100px");
		vPanelFiltros.add((new Label("Camadas")));
		vPanelFiltros.add(new CheckBox("Áreas de Agentes"));
		vPanelFiltros.add(new CheckBox("Focos"));
		vPanelFiltros.add(new CheckBox("Pessoas Contaminadas"));
		
		panelCamadas = new DecoratorPanel();
		panelCamadas.add(vPanelFiltros);
		
		initWidget(panelCamadas);
	}
	
}
