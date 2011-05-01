package br.edu.ufcg.geodengue.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PanelLogin extends Composite {

	private TextBox login;
	private PasswordTextBox senha;
	private Button botaoLogin;

	public PanelLogin(final GeoDengue geoDengue) {
		Label loginlbl = new Label("Login: ");
		Label senhalbl = new Label("Senha: ");
		loginlbl.setWidth("45px");
		senhalbl.setWidth("45px");

		login = new TextBox();
		senha = new PasswordTextBox();
		botaoLogin = new Button("Login");
		
		botaoLogin.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				geoDengue.login(login.getValue(), senha.getValue());
			}
		});
		
		login.setStyleName("inputText");
		senha.setStyleName("inputText");
		
		HorizontalPanel hPanelLogin = new HorizontalPanel();
		hPanelLogin.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hPanelLogin.add(loginlbl);
		hPanelLogin.add(login);

		HorizontalPanel hPanelSenha = new HorizontalPanel();
		hPanelSenha.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hPanelSenha.add(senhalbl);
		hPanelSenha.add(senha);
		
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		vPanel.add(hPanelLogin);
		vPanel.add(hPanelSenha);
		vPanel.add(botaoLogin);
		
		initWidget(vPanel);
		setStyleName("centralizar");
	}

	public void erroLogin() {
		final DialogBox dBox = new DialogBox();
		dBox.setText("Dados Invalidos!!");
		
		dBox.setAnimationEnabled(true);
		dBox.setGlassEnabled(true);
		
		Button fecha = new Button("Ok");
		fecha.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dBox.hide();				
			}
		});
		
		dBox.add(fecha);
		dBox.center();
		dBox.show();
	}
	
	public void limpaCampos() {
		login.setValue(null);
		senha.setValue(null);
	}
	
}

