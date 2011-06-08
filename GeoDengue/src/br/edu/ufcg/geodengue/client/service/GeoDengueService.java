package br.edu.ufcg.geodengue.client.service;

import java.util.Map;

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
	
	public boolean cadastraNovoAgente(String nome, String bairro);
	
	public PontoDTO recuperaFoco(double latitude, double longitude);
	
	public double calculaDistanciaEntreFocos(PontoDTO p1, PontoDTO p2);
}
