package br.edu.ufcg.geodengue.client;

import br.edu.ufcg.geodengue.client.eventos.BooleanEvento;
import br.edu.ufcg.geodengue.client.eventos.EventBus;
import br.edu.ufcg.geodengue.client.eventos.TiposDeEventos;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PanelCamadas extends Composite {

	private DecoratorPanel panelCamadas;
	private CheckBox areasAgentes;
	private CheckBox focos;
	private CheckBox pessoasContaminadas;
	
	public PanelCamadas() {
	
		criaCheckBoxes();
		
		VerticalPanel vPanelFiltros = new VerticalPanel();
		vPanelFiltros.setSize("200px", "100px");
		vPanelFiltros.add((new Label("Camadas")));
		vPanelFiltros.add(areasAgentes);
		vPanelFiltros.add(focos);
		vPanelFiltros.add(pessoasContaminadas);
		
		panelCamadas = new DecoratorPanel();
		panelCamadas.add(vPanelFiltros);
		
		initWidget(panelCamadas);
	}

	private void criaCheckBoxes() {
		areasAgentes = new CheckBox("Áreas de Agentes");
		areasAgentes.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				EventBus.getInstance().publica(new BooleanEvento(TiposDeEventos.CHECKBOX_AREA_AGENTE, areasAgentes.getValue()));
			}
		});
		
		
		focos = new CheckBox("Focos");
		focos.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				EventBus.getInstance().publica(new BooleanEvento(TiposDeEventos.CHECKBOX_FOCOS, focos.getValue()));
			}
		});
		
		pessoasContaminadas = new CheckBox("Pessoas Contaminadas");
		pessoasContaminadas.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				EventBus.getInstance().publica(new BooleanEvento(TiposDeEventos.CHECKBOX_PESSOAS_CONTAMINADAS, pessoasContaminadas.getValue()));
			}
		});
	}
}
