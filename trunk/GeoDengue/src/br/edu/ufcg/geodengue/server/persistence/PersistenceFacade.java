package br.edu.ufcg.geodengue.server.persistence;

import java.util.Map;

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
	
}
