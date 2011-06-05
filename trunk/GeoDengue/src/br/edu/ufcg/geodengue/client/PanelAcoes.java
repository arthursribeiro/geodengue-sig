package br.edu.ufcg.geodengue.client;

import br.edu.ufcg.geodengue.client.eventos.BooleanEvento;
import br.edu.ufcg.geodengue.client.eventos.EventBus;
import br.edu.ufcg.geodengue.client.eventos.TiposDeEventos;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ToggleButton;

public class PanelAcoes extends Composite {

	private DecoratorPanel panelAcoes;
	private ToggleButton cadastrarFoco;
	private Button pessoasRaio;
	private Button distanciaFocos;
	
	public PanelAcoes() {
		criaBotoes();
		
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setSpacing(5);
		hPanel.add(cadastrarFoco);
		hPanel.add(pessoasRaio);
		hPanel.add(distanciaFocos);
		
		panelAcoes = new DecoratorPanel();
		panelAcoes.add(hPanel);
		
		initWidget(panelAcoes);
		
	}
	
	private void criaBotoes() {

		cadastrarFoco = new ToggleButton("Cadastrar Foco");
		
		cadastrarFoco.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				EventBus.getInstance().publica(new BooleanEvento(TiposDeEventos.CADASTRAR_FOCO_CLICADO, cadastrarFoco.isDown()));
			}
		});
		//hPanel.add(new Button("Cadastrar"));
		
		pessoasRaio = new Button("Pessoas em um Raio");
		distanciaFocos = new Button("Distância entre Focos");
	}
	
}


