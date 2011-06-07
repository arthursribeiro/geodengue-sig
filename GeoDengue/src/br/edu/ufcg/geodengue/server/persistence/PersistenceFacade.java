package br.edu.ufcg.geodengue.server.persistence;

import java.sql.SQLException;
import java.util.Map;

import br.edu.ufcg.geodengue.shared.AgenteDTO;
import br.edu.ufcg.geodengue.shared.PontoDTO;
import br.edu.ufcg.geodengue.shared.RaioDTO;
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
	
	public long pessoasRaio(RaioDTO raio) {
		return dao.pessoasRaio(raio);
	}
	
	public boolean inserePonto(PontoDTO novoPonto) {
		try {
			dao.inserePonto(novoPonto);
			return true;
		} catch (SQLException e) {
//			e.printStackTrace();
			return false;
		}
	}
	
}
