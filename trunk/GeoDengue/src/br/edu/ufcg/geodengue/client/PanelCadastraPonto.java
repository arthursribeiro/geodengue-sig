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

public class PanelCadastraPonto extends Composite implements PanelToggle {

	private GeoDengueServiceAsync server = GWT.create(GeoDengueService.class);
	
	private DecoratorPanel panelCadastra;
	private TextBox descricao;
	
	public PanelCadastraPonto(String textoAjuda, final boolean ehFoco) {
		
		descricao = new TextBox();
		
		limpaCampos();
		
		VerticalPanel vPanelFiltros = new VerticalPanel();
		vPanelFiltros.setSize("930px" , "80px");
		vPanelFiltros.setSpacing(5);
		
		vPanelFiltros.add(new Label(textoAjuda));
		
		HorizontalPanel hPanelDesc = new HorizontalPanel();
		Label lblDesc = new Label("Descrição: ");
		hPanelDesc.add(lblDesc);
		hPanelDesc.add(descricao);
		
		Button botaoCadastrar = new Button("Cadastrar");
		botaoCadastrar.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				char tipo = ehFoco ? 'F' : 'P';
				chamaServidorCadastrarPonto(descricao.getValue(), tipo);
			}
		});

		hPanelDesc.add(botaoCadastrar);
		
		vPanelFiltros.add(hPanelDesc);
		
		panelCadastra = new DecoratorPanel();
		panelCadastra.add(vPanelFiltros);
		
		initWidget(panelCadastra);
	}
	
	private void chamaServidorCadastrarPonto(String descricao, char tipo) {
		
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
		
		Marker marcador = PanelPrincipal.getInstance().getMarcador();
		if (marcador == null) {
			Window.alert("Necessário marcar o ponto no mapa!");
			return;
		}
		server.cadastraNovoPonto(new PontoDTO(descricao, marcador.getLatLng().getLatitude(), marcador.getLatLng().getLongitude(), tipo), cadastraCallBack);
	}

	public void limpaCampos() {
		descricao.setValue("");
	}
	
}
