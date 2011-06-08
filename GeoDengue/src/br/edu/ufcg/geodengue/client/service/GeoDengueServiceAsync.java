package br.edu.ufcg.geodengue.client.service;

import br.edu.ufcg.geodengue.shared.PontoDTO;
import br.edu.ufcg.geodengue.shared.RaioDTO;
import br.edu.ufcg.geodengue.shared.SessaoDTO;
import br.edu.ufcg.geodengue.shared.TooltipDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GeoDengueServiceAsync {

	public void login(String login, String senha, AsyncCallback<SessaoDTO> callback);
	
	public void cadastraNovoPonto(PontoDTO novoPonto, AsyncCallback<Boolean> callback);
	
	public void pessoasRaio(RaioDTO raio, AsyncCallback<Long> callback);
	
	public void recuperaDadosTooltip(double latitude, double longitude, AsyncCallback<TooltipDTO> callback);

}
