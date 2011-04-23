package br.edu.ufcg.geodengue.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.maps.client.MapUIOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Tela Inicial
 */
public class GeoDengue implements EntryPoint {

	private MapWidget mapWidget;
	
	public void onModuleLoad() {
		
		mapWidget = new MapWidget(LatLng.newInstance(-7.231188, -35.886669), 13);
		mapWidget.setSize("800px", "700px");
		
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
			
		RootPanel.get("container").add(panelMapaEFiltros);
	}

}
