package br.edu.ufcg.geodengue.client.service;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GeoDengueServiceAsync {

	public void getMapaBairros(AsyncCallback<Map<String,String>> callback);
	
}
