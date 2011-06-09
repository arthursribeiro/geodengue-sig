package br.edu.ufcg.geodengue.server.persistence;

import java.sql.SQLException;
import java.util.Map;

import br.edu.ufcg.geodengue.shared.AgenteDTO;
import br.edu.ufcg.geodengue.shared.PontoDTO;
import br.edu.ufcg.geodengue.shared.RaioDTO;
import br.edu.ufcg.geodengue.shared.SessaoDTO;
import br.edu.ufcg.geodengue.shared.TooltipDTO;

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
	
	public boolean insereAgente(String nome, PontoDTO ponto) {
		
		TooltipDTO tool = dao.recuperaDadosTooltip(ponto.getLatitude(), ponto.getLongitude());
		
		if (!tool.getAgentes().isEmpty()) {
			throw new IllegalArgumentException();
		}
		
		try {
			dao.insereAgente(nome, ponto);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public TooltipDTO recuperaDadosTooltip(double latitude, double longitude) {
		return dao.recuperaDadosTooltip(latitude, longitude);
	}
	
	public PontoDTO recuperaFocos(double latitude, double longitude) {
		return dao.recuperaPonto(latitude, longitude);
	}
	
	public Map<String, String> recuperaBairrosSemResponsaveis() {
		return dao.recuperaBairrosSemResponsaveis();
	}
	
	public double calculaDistanciaEntreFocos(PontoDTO p1, PontoDTO p2) {
		return dao.calculaDistanciaEntreFocos(p1, p2);
	}
	
}
