package br.edu.ufcg.geodengue.client;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import br.edu.ufcg.geodengue.client.service.GeoDengueService;
import br.edu.ufcg.geodengue.client.service.GeoDengueServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.event.PolygonMouseOutHandler;
import com.google.gwt.maps.client.event.PolygonMouseOverHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Classe que representa a Tabela de Filtros (por bairro, tipo de dengue etc)
 */
public class PanelFiltros extends Composite {

	private final String COR = "blue";
	private MapWidget mapWidget;
	private GeoDengueServiceAsync server = GWT.create(GeoDengueService.class);
	private DecoratorPanel panelFiltros;
	
	public PanelFiltros(MapWidget mapWidget) {
		this.mapWidget = mapWidget;
	
		ScrollPanel panelFiltroDengue = new ScrollPanel();
		panelFiltroDengue.setSize("200px", "100px");
		
		Tree filtroDengue = criaNoArvoreFiltro("Dengues", Dengue.criaMapaDengue());
		panelFiltroDengue.add(filtroDengue);
		
		final ScrollPanel panelFiltroBairros = new ScrollPanel();
		panelFiltroBairros.setSize("200px", "500px");
		
		final AsyncCallback<Map<String,String>> testeCallBack = new AsyncCallback<Map<String,String>>() {
			@Override
			public void onSuccess(Map<String,String> pontos) {
				Map<String, Polygon> mapa = new TreeMap<String, Polygon>();
				for(String s : pontos.keySet()) {
					mapa.put(s, new Polygon(stringToLatLng(pontos.get(s)), COR, 1, 1, COR, 0.1));
				}
				Tree filtroBairros = criaNoArvoreFiltro("Bairros", mapa);
				panelFiltroBairros.add(filtroBairros);
			}

			@Override
			public void onFailure(Throwable caught) { }
		};
		
		server.getMapaBairros(testeCallBack);
		
		VerticalPanel vPanelFiltros = new VerticalPanel();
		vPanelFiltros.setSize("200px", "700px");
		vPanelFiltros.add((new Label("Filtros")));
		vPanelFiltros.add(panelFiltroDengue);
		vPanelFiltros.add(panelFiltroBairros);
		
		panelFiltros = new DecoratorPanel();
		panelFiltros.add(vPanelFiltros);
		
		initWidget(panelFiltros);
	}
	
	private LatLng[] stringToLatLng(String poligono) {
		poligono = poligono.replace("POLYGON", "");
		poligono = poligono.replace("))", "");
		poligono = poligono.replace("((", "");

		String[] pontos = poligono.split(",");
		LatLng[] retorno = new LatLng[pontos.length];
		
		int i = 0;
		for (String p : pontos) {
			String[] latLong = p.split(" ");
			double lat = Double.parseDouble(latLong[0]);
			double lon = Double.parseDouble(latLong[1]);
			retorno[i] = LatLng.newInstance(lat, lon);
			i++;
		}
		
		return retorno;
	}
	
	private Tree criaNoArvoreFiltro(String nome, Map<String, Polygon> mapaPoligono) {
		final Tree arvoreFiltro = new Tree();
		
		final Map<CheckBox, Polygon> checkBoxes = new HashMap<CheckBox, Polygon>();
		TreeItem item = arvoreFiltro.addItem(nome);
		
		final CheckBox todos = new CheckBox("Todos");
		todos.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				for(CheckBox check : checkBoxes.keySet()) {
					check.setValue(todos.getValue());
					if (todos.getValue()) {
						mapWidget.addOverlay(checkBoxes.get(check));
					} else {
						mapWidget.removeOverlay(checkBoxes.get(check));
					}
				}
			}
		});
		
		item.addItem(todos);
		
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
