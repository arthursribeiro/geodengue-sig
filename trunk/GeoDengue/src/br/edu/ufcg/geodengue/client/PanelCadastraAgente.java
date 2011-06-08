package br.edu.ufcg.geodengue.client;

import java.util.Map;

import br.edu.ufcg.geodengue.client.service.GeoDengueService;
import br.edu.ufcg.geodengue.client.service.GeoDengueServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PanelCadastraAgente extends Composite implements PanelToggle {

	private GeoDengueServiceAsync server = GWT.create(GeoDengueService.class);
	
	private DecoratorPanel panelCadastra;
	private ListBox lista;
	private TextBox nome;
	
	public PanelCadastraAgente(String textoAjuda) {
		
		lista = new ListBox();
		nome = new TextBox();
		
		limpaCampos();
		
		VerticalPanel vPanelFiltros = new VerticalPanel();
		vPanelFiltros.setSize("930px" , "80px");
		vPanelFiltros.setSpacing(5);
		
		vPanelFiltros.add(new Label(textoAjuda));
		
		HorizontalPanel hPanelDesc = new HorizontalPanel();
		Label lblDesc = new Label("Nome do Agente: ");
		hPanelDesc.add(lblDesc);
		hPanelDesc.add(nome);
		
		HorizontalPanel hPanelDesc2 = new HorizontalPanel();
		Label lblDesc2 = new Label("Bairro Responsavel: ");
		hPanelDesc2.add(lblDesc2);
		hPanelDesc2.add(lista);
		
		
		Button botaoCadastrar = new Button("Cadastrar");
		botaoCadastrar.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				chamaServidorCadastrarAgente();
			}
		});

		vPanelFiltros.add(hPanelDesc);
		vPanelFiltros.add(hPanelDesc2);
		vPanelFiltros.add(botaoCadastrar);
		
		panelCadastra = new DecoratorPanel();
		panelCadastra.add(vPanelFiltros);
		
		initWidget(panelCadastra);
	}
	
	private void criaListBox() {
		
		final AsyncCallback<Map<String, String>> recuperaCallBack = new AsyncCallback<Map<String, String>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Ocorreu um erro na comunicacao com o Servidor! =X");
			}

			@Override
			public void onSuccess(Map<String, String> result) {
				for (String chave : result.keySet()) {
					lista.addItem(chave, result.get(chave));
				}
			}
			
		};
		
		server.recuperaBairrosSemResponsaveis(recuperaCallBack);
	}

	private void chamaServidorCadastrarAgente() {
		
		final AsyncCallback<Boolean> cadastraCallBack = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Ocorreu um erro na comunicacao com o Servidor! =X");
			}

			@Override
			public void onSuccess(Boolean result) {
				PanelPrincipal.getInstance().limpaDinamico();
				PanelPrincipal.getInstance().untoggle();
				String mensagem = result ? "Cadastrado com Sucesso! \\o/" : "Ocorreu um erro durante o Cadastro =/";
				Window.alert(mensagem);
			}
			
		};
		
		if (nome.getValue().isEmpty()) {
			Window.alert("Digite um nome para o Agente!");
			return;
		}
		
		server.cadastraNovoAgente(nome.getValue(), lista.getValue(lista.getSelectedIndex()), cadastraCallBack);
	}

	public void limpaCampos() {
		nome.setValue("");
		lista.clear();
		criaListBox();
	}

	
}
