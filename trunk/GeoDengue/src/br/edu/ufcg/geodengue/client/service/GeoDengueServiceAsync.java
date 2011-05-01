package br.edu.ufcg.geodengue.client.service;

import java.util.Map;

import br.edu.ufcg.geodengue.shared.SessaoDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GeoDengueServiceAsync {

	public void getMapaBairros(AsyncCallback<Map<String,String>> callback);
	
	public void login(String login, String senha, AsyncCallback<SessaoDTO> callback);
	
}
