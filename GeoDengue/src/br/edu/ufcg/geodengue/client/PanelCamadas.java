package br.edu.ufcg.geodengue.client;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.geodengue.client.eventos.AtualizarMapaEvento;
import br.edu.ufcg.geodengue.client.eventos.EventBus;
import br.edu.ufcg.geodengue.client.utils.Camada;

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

	private List<Camada> camadas;
	
	public PanelCamadas() {
		this.camadas = new ArrayList<Camada>();
		
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
				if (areasAgentes.getValue()) {
					camadas.add(Camada.AREA_AGENTES);
				} else {
					camadas.remove(Camada.AREA_AGENTES);
				}
				EventBus.getInstance().publica(new AtualizarMapaEvento());
			}
		});
		
		
		focos = new CheckBox("Focos");
		focos.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (focos.getValue()) {
					camadas.add(Camada.FOCOS);
				} else {
					camadas.remove(Camada.FOCOS);
				}
				EventBus.getInstance().publica(new AtualizarMapaEvento());
			}
		});
		
		pessoasContaminadas = new CheckBox("Pessoas Contaminadas");
		pessoasContaminadas.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (pessoasContaminadas.getValue()) {
					camadas.add(Camada.PESSOAS_CONTAMINADAS);
				} else {
					camadas.remove(Camada.PESSOAS_CONTAMINADAS);
				}
				EventBus.getInstance().publica(new AtualizarMapaEvento());
			}
		});
	}
	
	public boolean isCamadaAtiva(Camada camada) {
		return this.camadas.contains(camada);
	}
	
	public void adicionaCamada(Camada camada) {
		this.camadas.add(camada);
	}
	
	public void removeCamada(Camada camada) {
		this.camadas.remove(camada);
	}
	
}
