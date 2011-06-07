package br.edu.ufcg.geodengue.client;

import br.edu.ufcg.geodengue.shared.AgenteDTO;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PanelDados extends Composite {

	private AgenteDTO agente;
	
	public PanelDados(AgenteDTO agente) {
		this.agente = agente;
		
		DecoratorPanel decoratorPanelDados = new DecoratorPanel();
		
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setSize("930px" , "80px");

		vPanel.add(new Label("Área de Cobertura: "+agente.getAreaCobertura()));
		vPanel.add(new Label("Comprimento da Rota: "+agente.getComprimentoRota()));
		vPanel.add(new Label("Focos Responsável: "+agente.getFocosResponsavel()));
		vPanel.add(new Label("Focos na Rota: "+agente.getFocosNaRota()));
		
		decoratorPanelDados.add(vPanel);
		initWidget(decoratorPanelDados);		
	}

	public AgenteDTO getAgente() {
		return agente;
	}
	
}
