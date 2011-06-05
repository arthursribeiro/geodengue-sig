package br.edu.ufcg.geodengue.client.service;

import br.edu.ufcg.geodengue.shared.PontoDTO;
import br.edu.ufcg.geodengue.shared.SessaoDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("geoDengueService")
public interface GeoDengueService extends RemoteService {
	
	public SessaoDTO login(String login, String senha);
	
	public boolean cadastraNovoPonto(PontoDTO novoPonto);
	
}
