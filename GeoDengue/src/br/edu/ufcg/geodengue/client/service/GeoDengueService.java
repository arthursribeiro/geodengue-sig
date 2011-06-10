package br.edu.ufcg.geodengue.client.service;

import java.util.List;
import java.util.Map;

import br.edu.ufcg.geodengue.shared.AreaAgenteDTO;
import br.edu.ufcg.geodengue.shared.PontoDTO;
import br.edu.ufcg.geodengue.shared.RaioDTO;
import br.edu.ufcg.geodengue.shared.SessaoDTO;
import br.edu.ufcg.geodengue.shared.TooltipDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("geoDengueService")
public interface GeoDengueService extends RemoteService {
	
	public SessaoDTO login(String login, String senha);
	
	public boolean cadastraNovoPonto(PontoDTO novoPonto);
	
	public long pessoasRaio(RaioDTO raio);
	
	public TooltipDTO recuperaDadosTooltip(double latitude, double longitude);
	
	public Map<String, String> recuperaBairrosSemResponsaveis();
	
	public int cadastraNovoAgente(String nome, PontoDTO ponto);
	
	public PontoDTO recuperaFoco(double latitude, double longitude);
	
	public PontoDTO recuperaAgente(double latitude, double longitude);
	
	public double calculaDistanciaEntreFocos(PontoDTO p1, PontoDTO p2);
	
	public AreaAgenteDTO recuperaDadosAreaAgente(double latitude, double longitude);
	
	public List<String> focosDistancia(double latitude, double longitude, double distancia);
}
