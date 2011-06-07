package br.edu.ufcg.geodengue.client;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.geodengue.client.utils.Camada;
import br.edu.ufcg.geodengue.client.utils.Estado;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class PanelAcoes extends Composite {

	private DecoratorPanel panelAcoes;
	private ToggleButton cadastrarFoco;
	private ToggleButton cadastrarPessoa;
	private ToggleButton pessoasRaio;
	private Button distanciaFocos;
	
	private List<Estado> estados;
	
	public PanelAcoes() {
		this.estados = new ArrayList<Estado>();
		
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
				trataClique(new PanelCadastraPonto("Texto de ajuda do Cadastrar Foco", true), Estado.CADASTRAR_FOCO, cadastrarFoco.isDown());
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
				trataClique(new PanelCadastraPonto("Texto de ajuda do Cadastrar Pessoa", false), Estado.CADASTRAR_PESSOA, cadastrarPessoa.isDown());
			}
		});
		cadastrarPessoa.addMouseDownHandler(new MouseDownHandler() {
			
			@Override
			public void onMouseDown(MouseDownEvent event) {
				if (!cadastrarPessoa.isDown()) untoggleButtons();
			}
		});
		
		pessoasRaio = new ToggleButton("Pessoas em um Raio");
		pessoasRaio.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				trataClique(new PanelPessoasRaio("Texto de ajuda do Calcular Pessoas em um Raio"), Estado.CALCULAR_PESSOA_RAIO, pessoasRaio.isDown());
				if(pessoasRaio.isDown()) PanelPrincipal.getInstance().adicionaCamada(Camada.PESSOAS_RAIO);
				else PanelPrincipal.getInstance().removeCamada(Camada.PESSOAS_RAIO);
			}
		});
		pessoasRaio.addMouseDownHandler(new MouseDownHandler() {
			
			@Override
			public void onMouseDown(MouseDownEvent event) {
				if (!cadastrarPessoa.isDown()) untoggleButtons();
			}
		});
		
		distanciaFocos = new Button("Distância entre Focos");
		distanciaFocos.setEnabled(false);
	}
	
	public void untoggleButtons() {
		cadastrarPessoa.setValue(false, true);
		cadastrarFoco.setValue(false, true);
	}
	
	private void trataClique(Widget w, Estado estado, boolean valor) {
		limpaDinamico();
		if(valor) {
			RootPanel.get("dinamico").add(w);
			estados.add(estado);
		}
		PanelPrincipal.getInstance().retiraMarcador();
	}
	
	public void limpaDinamico() {
		RootPanel.get("dinamico").clear();
		estados.clear();
	}
	
	public boolean isBotaoAtivo(Estado estado) {
		return this.estados.contains(estado);
	}
	
}


