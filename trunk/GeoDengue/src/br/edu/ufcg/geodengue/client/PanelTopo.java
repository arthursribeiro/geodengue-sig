package br.edu.ufcg.geodengue.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class PanelTopo extends Composite {

	private Label username;
	private Anchor login;
	private Anchor logout;
	
	public PanelTopo(final GeoDengue geoDengue) {
		login = new Anchor("Login");
		logout = new Anchor("Logout");
		username = new Label("Raquel");
		username.setVisible(false);
		logout.setVisible(false);
		
		login.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				geoDengue.mostraLoginForm();
			}
		});
		
		logout.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				geoDengue.logout();
			}
		});
		
		HorizontalPanel panel = new HorizontalPanel();
		panel.setStyleName("links");
		panel.setSpacing(5);
		
		panel.add(username);
		panel.add(login);
		panel.add(logout);
		
		initWidget(panel);
	}
	
	public void loginComSucesso(String usernameLogado) {
		username.setText(usernameLogado);
		username.setVisible(true);
		login.setVisible(false);
		logout.setVisible(true);
	}
	
	public void logoutComSucesso() {
		username.setText("");
		username.setVisible(false);
		login.setVisible(true);
		logout.setVisible(false);
	}
	
}
