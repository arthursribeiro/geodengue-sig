package br.edu.ufcg.geodengue.client;

import br.edu.ufcg.geodengue.client.service.GeoDengueService;
import br.edu.ufcg.geodengue.client.service.GeoDengueServiceAsync;
import br.edu.ufcg.geodengue.shared.SessaoDTO;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Tela Inicial
 */
public class GeoDengue implements EntryPoint {

	private PanelTopo panelTopo;
	private PanelLogin panelLogin;
	
	private GeoDengueServiceAsync server = GWT.create(GeoDengueService.class);
	
	public void onModuleLoad() {
		panelTopo = new PanelTopo(this);
		panelLogin = new PanelLogin(this);
		
		RootPanel.get("login").add(panelTopo);
	}
	
	public void login(String login, String senha) {
		final AsyncCallback<SessaoDTO> loginCallBack = new AsyncCallback<SessaoDTO>() {

			@Override
			public void onFailure(Throwable caught) { }

			@Override
			public void onSuccess(SessaoDTO result) {
				if (result == null) {
					panelLogin.erroLogin();
				} else {
					panelTopo.loginComSucesso(result.getAgente().getNome());
					panelLogin.limpaCampos();
					RootPanel.get("container").clear();
					RootPanel.get("container").add(new PanelPrincipalAgente(result));
				}
			}
			
		};
		
		server.login(login, senha, loginCallBack);
	}
	
	public void logout() {
		panelTopo.logoutComSucesso();
		RootPanel.get("container").clear();
		RootPanel.get("dados").clear();
		RootPanel.get("acoes").clear();
		RootPanel.get("dinamico").clear();
	}

	public void mostraLoginForm() {
		RootPanel.get("container").clear();
		RootPanel.get("container").add(panelLogin);
	}

}
