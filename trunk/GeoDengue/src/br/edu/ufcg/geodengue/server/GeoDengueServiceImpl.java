package br.edu.ufcg.geodengue.server;

import java.util.Map;

import br.edu.ufcg.geodengue.client.service.GeoDengueService;
import br.edu.ufcg.geodengue.server.persistence.PersistenceFacade;
import br.edu.ufcg.geodengue.shared.SessaoDTO;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class GeoDengueServiceImpl extends RemoteServiceServlet implements GeoDengueService {

	private static final long serialVersionUID = -6568662887145735114L;

	@Override
	public Map<String,String> getMapaBairros() {
		return PersistenceFacade.getInstance().getMapaBairros();
	}

	@Override
	public SessaoDTO login(String login, String senha) {
		return PersistenceFacade.getInstance().login(login, senha);
	}

}
