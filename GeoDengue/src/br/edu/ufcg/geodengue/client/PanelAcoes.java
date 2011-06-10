package br.edu.ufcg.geodengue.client;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.geodengue.client.eventos.AtualizarMapaEvento;
import br.edu.ufcg.geodengue.client.eventos.EventBus;
import br.edu.ufcg.geodengue.client.utils.Camada;
import br.edu.ufcg.geodengue.client.utils.Estado;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PanelAcoes extends Composite {

	private DecoratorPanel panelAcoes;
	private ToggleButton cadastrarFoco;
	private ToggleButton cadastrarAgente;
	private ToggleButton cadastrarPessoa;
	private ToggleButton pessoasRaio;
	private ToggleButton distanciaFocos;
	private ToggleButton rotaDeAgente;
	private ToggleButton areaDoAgente;
	private ToggleButton simularDemissao;
	private ToggleButton focosADistancia;
	
	private PanelCadastraPonto panelCadastraFoco;
	private PanelCadastraPonto panelCadastraPessoa;
	private PanelCadastraAgente panelCadastraAgente;
	private PanelPessoasRaio panelPessoasRaio;
	private PanelFocosDistancia panelFocosADistancia;
	private PanelDistanciaFocos panelDistanciaFocos;
	private PanelAreaAgente panelAreaAgente;
	
	private List<Estado> estados;
	
	public PanelAcoes() {
		this.estados = new ArrayList<Estado>();
		
		this.panelCadastraFoco = new PanelCadastraPonto("Texto de ajuda do Cadastrar Foco", true);
		this.panelCadastraPessoa = new PanelCadastraPonto("Texto de ajuda do Cadastrar Pessoa", false);
		this.panelCadastraAgente = new PanelCadastraAgente("Texto de ajuda do Cadastrar Agente");
		this.panelPessoasRaio = new PanelPessoasRaio("Texto de ajuda do Calcular Pessoas em um Raio");
		this.panelFocosADistancia = new PanelFocosDistancia("Texto de ajuda do Focos a uma distancia X");
		this.panelDistanciaFocos = new PanelDistanciaFocos("Texto de ajuda do Calcular Pessoas em um Raio");
		this.panelAreaAgente = new PanelAreaAgente();
		
		this.panelPessoasRaio.iniciaTratador();
		this.panelFocosADistancia.iniciaTratador();
		
		criaBotoes();
		
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setWidth("200px");
		vPanel.setSpacing(5);
		vPanel.add(cadastrarAgente);
		vPanel.add(cadastrarFoco);
		vPanel.add(cadastrarPessoa);
		vPanel.add(pessoasRaio);
		vPanel.add(distanciaFocos);
		vPanel.add(focosADistancia);
		vPanel.add(areaDoAgente);
		vPanel.add(rotaDeAgente);
		vPanel.add(simularDemissao);
		
		panelAcoes = new DecoratorPanel();
		panelAcoes.add(vPanel);
		
		initWidget(panelAcoes);
		
	}
	
	private void criaBotoes() {

		cadastrarAgente = new ToggleButton("Cadastrar Agente");
		cadastrarAgente.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				panelCadastraAgente.limpaCampos();
				trataClique(panelCadastraAgente, Estado.CADASTRAR_AGENTE, cadastrarAgente.isDown());
			}
		});
		cadastrarAgente.addMouseDownHandler(new MouseDownHandler() {
			
			@Override
			public void onMouseDown(MouseDownEvent event) {
				if (!cadastrarAgente.isDown()) untoggleButtons();
			}
		});
		
		cadastrarFoco = new ToggleButton("Cadastrar Foco");
		cadastrarFoco.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				panelCadastraFoco.limpaCampos();
				trataClique(panelCadastraFoco, Estado.CADASTRAR_FOCO, cadastrarFoco.isDown());
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
				panelCadastraPessoa.limpaCampos();
				trataClique(panelCadastraPessoa, Estado.CADASTRAR_PESSOA, cadastrarPessoa.isDown());
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
				trataClique(panelPessoasRaio, Estado.CALCULAR_PESSOA_RAIO, pessoasRaio.isDown());
				if(pessoasRaio.isDown()) {
					panelPessoasRaio.assinaTratador();
					PanelPrincipal.getInstance().adicionaCamada(Camada.PESSOAS_RAIO);
				} else {
					panelPessoasRaio.removeTratador();
					PanelPrincipal.getInstance().removeCamada(Camada.PESSOAS_RAIO);
					PanelPrincipal.getInstance().removePoligonoPessoasRaio();
				}
			}
		});
		pessoasRaio.addMouseDownHandler(new MouseDownHandler() {
			
			@Override
			public void onMouseDown(MouseDownEvent event) {
				if (!pessoasRaio.isDown()) untoggleButtons();
				EventBus.getInstance().publica(new AtualizarMapaEvento());
			}
		});
		
		focosADistancia = new ToggleButton("Focos a uma Distancia X");
		focosADistancia.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				trataClique(panelFocosADistancia, Estado.FOCOS_DISTANCIA, focosADistancia.isDown());
				if(focosADistancia.isDown()) {
					panelFocosADistancia.assinaTratador();
				} else {
					panelFocosADistancia.removeTratador();
				}
			}
		});
		focosADistancia.addMouseDownHandler(new MouseDownHandler() {
			
			@Override
			public void onMouseDown(MouseDownEvent event) {
				if (!focosADistancia.isDown()) untoggleButtons();
				EventBus.getInstance().publica(new AtualizarMapaEvento());
			}
		});

		distanciaFocos = new ToggleButton("Distância entre Focos");
		distanciaFocos.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				trataClique(panelDistanciaFocos, Estado.DISTANCIA_DOIS_FOCOS, distanciaFocos.isDown());
			}
		});
		distanciaFocos.addMouseDownHandler(new MouseDownHandler() {
			
			@Override
			public void onMouseDown(MouseDownEvent event) {
				if (!distanciaFocos.isDown()) untoggleButtons();
				EventBus.getInstance().publica(new AtualizarMapaEvento());
			}
		});
		
		areaDoAgente = new ToggleButton("Área do Agente");
		areaDoAgente.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				panelAreaAgente.limpaCampos();
				trataClique(panelAreaAgente, Estado.AREA_AGENTE, areaDoAgente.isDown());
			}
		});
		areaDoAgente.addMouseDownHandler(new MouseDownHandler() {
			
			@Override
			public void onMouseDown(MouseDownEvent event) {
				if (!areaDoAgente.isDown()) untoggleButtons();
			}
		});
		
		rotaDeAgente = new ToggleButton("Rota de Agente");
		rotaDeAgente. setEnabled(false);
		
		simularDemissao = new ToggleButton("Simular Demissao");
		simularDemissao.setEnabled(false);
	}
	
	public void untoggleButtons() {
		cadastrarPessoa.setValue(false, true);
		cadastrarAgente.setValue(false, true);
		cadastrarFoco.setValue(false, true);
		pessoasRaio.setValue(false, true);
		distanciaFocos.setValue(false, true);
		areaDoAgente.setValue(false, true);
		rotaDeAgente.setValue(false, true);
		simularDemissao.setValue(false, true);
		focosADistancia.setValue(false, true);
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
	
	public PanelDistanciaFocos getPanelDistancia() {
		return panelDistanciaFocos;
	}

	public PanelAreaAgente getPanelAreaAgente() {
		return panelAreaAgente;
	}
	
}


