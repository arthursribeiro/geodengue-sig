package br.edu.ufcg.geodengue.server;

import java.util.Map;

import br.edu.ufcg.geodengue.client.service.GeoDengueService;
import br.edu.ufcg.geodengue.server.persistence.PersistenceFacade;
import br.edu.ufcg.geodengue.shared.PontoDTO;
import br.edu.ufcg.geodengue.shared.RaioDTO;
import br.edu.ufcg.geodengue.shared.SessaoDTO;
import br.edu.ufcg.geodengue.shared.TooltipDTO;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class GeoDengueServiceImpl extends RemoteServiceServlet implements GeoDengueService {

	private static final long serialVersionUID = -6568662887145735114L;

	@Override
	public SessaoDTO login(String login, String senha) {
		return PersistenceFacade.getInstance().login(login, senha);
	}

	@Override
	public boolean cadastraNovoPonto(PontoDTO novoPonto) {
		return PersistenceFacade.getInstance().inserePonto(novoPonto);
	}
	
	@Override
	public int cadastraNovoAgente(String nome, PontoDTO ponto) {
		try {
			boolean b = PersistenceFacade.getInstance().insereAgente(nome, ponto);
			return b ? 1 : 0;
		} catch (Exception e) {
			return -1;
		}
	}

	@Override
	public long pessoasRaio(RaioDTO raio) {
		return PersistenceFacade.getInstance().pessoasRaio(raio);
	}
	
	@Override
	public TooltipDTO recuperaDadosTooltip(double latitude, double longitude) {
		return PersistenceFacade.getInstance().recuperaDadosTooltip(latitude, longitude);
	}
	
	@Override
	public PontoDTO recuperaFoco(double latitude, double longitude) {
		return PersistenceFacade.getInstance().recuperaFocos(latitude, longitude);
	}
	
	@Override
	public Map<String, String> recuperaBairrosSemResponsaveis() {
		return PersistenceFacade.getInstance().recuperaBairrosSemResponsaveis();
	}
	
	@Override	
	public double calculaDistanciaEntreFocos(PontoDTO p1, PontoDTO p2) {
		return PersistenceFacade.getInstance().calculaDistanciaEntreFocos(p1, p2);
	}

}
