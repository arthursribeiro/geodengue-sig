package br.edu.ufcg.geodengue.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.event.PolygonMouseOutHandler;
import com.google.gwt.maps.client.event.PolygonMouseOverHandler;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Classe que representa a Tabela de Filtros (por bairro, tipo de dengue etc)
 */
public class PanelFiltros extends Composite {

	private MapWidget mapWidget;
	private DecoratorPanel panelFiltros;
	
	public PanelFiltros(MapWidget mapWidget) {
		this.mapWidget = mapWidget;
	
		ScrollPanel panelFiltroDengue = new ScrollPanel();
		panelFiltroDengue.setWidth("200px");
		
		Tree filtroDengue = criaNoArvoreFiltro("Dengues", Dengue.criaMapaDengue());
		filtroDengue.setHeight("130px");
		panelFiltroDengue.add(filtroDengue);
		
		Label titulo = new Label("Filtros");
		
		VerticalPanel vPanelFiltros = new VerticalPanel();
		vPanelFiltros.setSize("200px", "475px");
		vPanelFiltros.add(titulo);
		vPanelFiltros.add(panelFiltroDengue);

		vPanelFiltros.setCellVerticalAlignment(titulo, HasVerticalAlignment.ALIGN_TOP);
		vPanelFiltros.setCellVerticalAlignment(panelFiltroDengue, HasVerticalAlignment.ALIGN_TOP);
		
		vPanelFiltros.setCellHeight(titulo, "10px");
		vPanelFiltros.setCellHeight(panelFiltroDengue, "130px");
		
		panelFiltros = new DecoratorPanel();
		panelFiltros.add(vPanelFiltros);
		
		initWidget(panelFiltros);
	}
	
	private Tree criaNoArvoreFiltro(String nome, Map<String, Polygon> mapaPoligono) {
		final Tree arvoreFiltro = new Tree();
		
		final Map<CheckBox, Polygon> checkBoxes = new HashMap<CheckBox, Polygon>();
		TreeItem item = arvoreFiltro.addItem(nome);
		
		for (final String mapKey : mapaPoligono.keySet()) {
			final CheckBox w =  new CheckBox(mapKey);
			final Polygon poligono = mapaPoligono.get(mapKey);
			
			final InfoWindow info = mapWidget.getInfoWindow();
			
			poligono.addPolygonMouseOverHandler(new PolygonMouseOverHandler() {
				@Override
				public void onMouseOver(PolygonMouseOverEvent event) {
					info.open(poligono.getBounds().getCenter(), new InfoWindowContent(mapKey));
				}
			});
	        
			poligono.addPolygonMouseOutHandler(new PolygonMouseOutHandler() {
				@Override
				public void onMouseOut(PolygonMouseOutEvent event) {
					info.close();
				}
			});
			
			w.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (w.getValue()) {
						mapWidget.addOverlay(poligono);
					} else {
						mapWidget.removeOverlay(poligono);
					}
				}
			});
			item.addItem(w);
			checkBoxes.put(w,poligono);
		}
		return arvoreFiltro;
	}
	
}
