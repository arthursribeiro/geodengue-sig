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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class PanelAcoes extends Composite {

	private DecoratorPanel panelAcoes;
	private ToggleButton cadastrarFoco;
	private ToggleButton cadastrarAgente;
	private ToggleButton cadastrarPessoa;
	private ToggleButton pessoasRaio;
	private ToggleButton distanciaFocos;
	
	private PanelCadastraPonto panelCadastraFoco;
	private PanelCadastraPonto panelCadastraPessoa;
	private PanelCadastraAgente panelCadastraAgente;
	private PanelPessoasRaio panelPessoasRaio;
	private PanelDistanciaFocos panelDistanciaFocos;
	
	private List<Estado> estados;
	
	public PanelAcoes() {
		this.estados = new ArrayList<Estado>();
		
		this.panelCadastraFoco = new PanelCadastraPonto("Texto de ajuda do Cadastrar Foco", true);
		this.panelCadastraPessoa = new PanelCadastraPonto("Texto de ajuda do Cadastrar Pessoa", false);
		this.panelCadastraAgente = new PanelCadastraAgente("Texto de ajuda do Cadastrar Agente");
		this.panelPessoasRaio = new PanelPessoasRaio("Texto de ajuda do Calcular Pessoas em um Raio");
		this.panelDistanciaFocos = new PanelDistanciaFocos("Texto de ajuda do Calcular Pessoas em um Raio");
		
		this.panelPessoasRaio.iniciaTratador();
		
		criaBotoes();
		
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setSpacing(5);
		hPanel.add(cadastrarAgente);
		hPanel.add(cadastrarFoco);
		hPanel.add(cadastrarPessoa);
		hPanel.add(pessoasRaio);
		hPanel.add(distanciaFocos);
		
		panelAcoes = new DecoratorPanel();
		panelAcoes.add(hPanel);
		
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
	}
	
	public void untoggleButtons() {
		cadastrarPessoa.setValue(false, true);
		cadastrarAgente.setValue(false, true);
		cadastrarFoco.setValue(false, true);
		pessoasRaio.setValue(false, true);
		distanciaFocos.setValue(false, true);
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
	
}


