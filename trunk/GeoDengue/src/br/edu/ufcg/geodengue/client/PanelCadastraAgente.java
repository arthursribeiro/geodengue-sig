package br.edu.ufcg.geodengue.client;

import br.edu.ufcg.geodengue.client.service.GeoDengueService;
import br.edu.ufcg.geodengue.client.service.GeoDengueServiceAsync;
import br.edu.ufcg.geodengue.shared.PontoDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PanelCadastraAgente extends Composite implements PanelToggle {

	private GeoDengueServiceAsync server = GWT.create(GeoDengueService.class);
	
	private DecoratorPanel panelCadastra;
	private TextBox nome;
	
	public PanelCadastraAgente(String textoAjuda) {
		
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
		
		Button botaoCadastrar = new Button("Cadastrar");
		botaoCadastrar.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				chamaServidorCadastrarAgente();
			}
		});
		
		hPanelDesc.add(botaoCadastrar);

		vPanelFiltros.add(hPanelDesc);
		
		panelCadastra = new DecoratorPanel();
		panelCadastra.add(vPanelFiltros);
		
		initWidget(panelCadastra);
	}
	
	private void chamaServidorCadastrarAgente() {
		
		final AsyncCallback<Integer> cadastraCallBack = new AsyncCallback<Integer>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Ocorreu um erro na comunicacao com o Servidor! =X");
			}

			@Override
			public void onSuccess(Integer result) {
				PanelPrincipal.getInstance().limpaDinamico();
				PanelPrincipal.getInstance().untoggle();
				if (result == -1) {
					Window.alert("Ja existe um agente responsavel por esse Bairro!");
					return;
				}
				String mensagem = result == 1 ? "Cadastrado com Sucesso! \\o/" : "Ocorreu um erro durante o Cadastro =/";
				Window.alert(mensagem);
			}
			
		};
		
		if (nome.getValue().isEmpty()) {
			Window.alert("Digite um nome para o Agente!");
			return;
		}
		
		Marker marcador = PanelPrincipal.getInstance().getMarcador();
		if (marcador == null) {
			Window.alert("Necessário marcar o ponto no mapa!");
			return;
		}
		
		try {
			server.cadastraNovoAgente(nome.getValue(), new PontoDTO(nome.getValue(), marcador.getLatLng().getLatitude(), marcador.getLatLng().getLongitude(), 'A'), cadastraCallBack);
		} catch (IllegalArgumentException e) {
			Window.alert("Ja existe um agente para esse bairro!");
		}
	}
	
	public void limpaCampos() {
		nome.setValue("");
	}

	
}
