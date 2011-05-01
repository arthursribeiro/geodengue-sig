package br.edu.ufcg.geodengue.client.service;

import java.util.Map;

import br.edu.ufcg.geodengue.shared.SessaoDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("geoDengueService")
public interface GeoDengueService extends RemoteService {
	
	/**
	 * Chave: Nome do bairro (pego do BD)
	 * Valor: toString do Objeto do BD - ex: POLYGON((lat long, lat long, ...))
	 *   
	 * @return mapa dos bairros de CampinaGrande
	 */
	public Map<String, String> getMapaBairros();

	public SessaoDTO login(String login, String senha);
	
}
