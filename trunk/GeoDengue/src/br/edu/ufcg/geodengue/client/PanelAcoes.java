package br.edu.ufcg.geodengue.client;

import br.edu.ufcg.geodengue.client.eventos.BooleanEvento;
import br.edu.ufcg.geodengue.client.eventos.EventBus;
import br.edu.ufcg.geodengue.client.eventos.TiposDeEventos;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ToggleButton;

public class PanelAcoes extends Composite {

	private DecoratorPanel panelAcoes;
	private ToggleButton cadastrarFoco;
	private ToggleButton cadastrarPessoa;
	private Button pessoasRaio;
	private Button distanciaFocos;
	
	public PanelAcoes() {
		criaBotoes();
		
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setSpacing(5);
		hPanel.add(cadastrarFoco);
		hPanel.add(cadastrarPessoa);
		hPanel.add(pessoasRaio);
		hPanel.add(distanciaFocos);
		
		panelAcoes = new DecoratorPanel();
		panelAcoes.add(hPanel);
		
		initWidget(panelAcoes);
		
	}
	
	private void criaBotoes() {

		cadastrarFoco = new ToggleButton("Cadastrar Foco");
		cadastrarFoco.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				EventBus.getInstance().publica(new BooleanEvento(TiposDeEventos.CADASTRAR_FOCO_CLICADO, cadastrarFoco.isDown()));
			}
		});
		cadastrarFoco.addMouseDownHandler(new MouseDownHandler() {
			
			@Override
			public void onMouseDown(MouseDownEvent event) {
				if (!cadastrarFoco.isDown()) untoggleButtons();
			}
		});
		
		cadastrarPessoa = new ToggleButton("Cadastrar Pessoa");
		cadastrarPessoa.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				EventBus.getInstance().publica(new BooleanEvento(TiposDeEventos.CADASTRAR_PESSOA_CLICADO, cadastrarPessoa.isDown()));
			}
		});
		cadastrarPessoa.addMouseDownHandler(new MouseDownHandler() {
			
			@Override
			public void onMouseDown(MouseDownEvent event) {
				if (!cadastrarPessoa.isDown()) untoggleButtons();
			}
		});
		
		pessoasRaio = new Button("Pessoas em um Raio");
		pessoasRaio.setEnabled(false);
		
		distanciaFocos = new Button("Distância entre Focos");
		distanciaFocos.setEnabled(false);
	}
	
	public void untoggleButtons() {
		cadastrarPessoa.setValue(false, true);
		cadastrarFoco.setValue(false, true);
	}
	
}


