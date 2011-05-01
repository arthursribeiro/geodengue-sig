package br.edu.ufcg.geodengue.server.persistence;

import java.util.Map;

import br.edu.ufcg.geodengue.shared.AgenteDTO;
import br.edu.ufcg.geodengue.shared.SessaoDTO;

public class PersistenceFacade {
	
	private GeoDengueDAO dao;
	
	private static PersistenceFacade instance;
	
	private PersistenceFacade() {
		dao = new GeoDengueDAO();
	}
	
	public static PersistenceFacade getInstance() {
		if (instance == null) {
			instance = new PersistenceFacade();
		}
		return instance;
	}
	
	public Map<String,String> getMapaBairros() {
		return dao.getMapaBairros();
	}

	public SessaoDTO login(String login, String senha) {
		AgenteDTO agente = dao.getAgente(login, senha);		
		return (SessaoDTO) (agente == null ? agente : new SessaoDTO(agente));
	}
	
}
