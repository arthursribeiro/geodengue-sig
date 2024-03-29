package br.edu.ufcg.geodengue.client.service;

import java.util.List;
import java.util.Map;

import br.edu.ufcg.geodengue.shared.AreaAgenteDTO;
import br.edu.ufcg.geodengue.shared.PontoDTO;
import br.edu.ufcg.geodengue.shared.RaioDTO;
import br.edu.ufcg.geodengue.shared.SessaoDTO;
import br.edu.ufcg.geodengue.shared.SimularDTO;
import br.edu.ufcg.geodengue.shared.TooltipDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GeoDengueServiceAsync {

	public void login(String login, String senha, AsyncCallback<SessaoDTO> callback);
	
	public void cadastraNovoPonto(PontoDTO novoPonto, AsyncCallback<Boolean> callback);
	
	public void pessoasRaio(RaioDTO raio, AsyncCallback<Long> callback);
	
	public void recuperaDadosTooltip(double latitude, double longitude, AsyncCallback<TooltipDTO> callback);
	
	public void recuperaBairrosSemResponsaveis(AsyncCallback<Map<String, String>> callback);
	
	public void cadastraNovoAgente(String nome, PontoDTO ponto, AsyncCallback<Integer> callback);
	
	public void recuperaFoco(double latitude, double longitude, AsyncCallback<PontoDTO> callback);
	
	public void recuperaAgente(double latitude, double longitude, AsyncCallback<PontoDTO> callback);
	
	public void calculaDistanciaEntreFocos(PontoDTO p1, PontoDTO p2, AsyncCallback<Double> callback);
	
	public void recuperaDadosAreaAgente(double latitude, double longitude, AsyncCallback<AreaAgenteDTO> callback);

	public void focosDistancia(double latitude, double longitude, double distancia, AsyncCallback<List<String>> calculaCallBack);
	
	public void getPontosEmBairro(double latitude, double longitude, AsyncCallback<List<String>> calculaCallBack);
	
	public void simulaDemitir(double latitude, double longitude, AsyncCallback<List<SimularDTO>> calculaCallBack);

}
