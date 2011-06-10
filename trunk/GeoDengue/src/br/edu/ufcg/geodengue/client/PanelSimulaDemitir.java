package br.edu.ufcg.geodengue.client;

import java.util.List;

import br.edu.ufcg.geodengue.client.service.GeoDengueService;
import br.edu.ufcg.geodengue.client.service.GeoDengueServiceAsync;
import br.edu.ufcg.geodengue.shared.PontoDTO;
import br.edu.ufcg.geodengue.shared.SimularDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PanelSimulaDemitir extends Composite implements PanelToggle {

	private GeoDengueServiceAsync server = GWT.create(GeoDengueService.class);
	
	private DecoratorPanel panelCadastra;
	private HTML mudancas;
	
	public PanelSimulaDemitir() {
		
		mudancas = new HTML();
		
		limpaCampos();
		
		VerticalPanel vPanelFiltros = new VerticalPanel();
		vPanelFiltros.setSize("930px" , "80px");
		vPanelFiltros.setSpacing(5);
		
		VerticalPanel hPanelDesc = new VerticalPanel();
		HTML lblDesc = new HTML("<b>Mudancas:</b> ");
		hPanelDesc.add(lblDesc);
		hPanelDesc.add(mudancas);
		
		vPanelFiltros.add(hPanelDesc);
		
		panelCadastra = new DecoratorPanel();
		panelCadastra.add(vPanelFiltros);
		
		initWidget(panelCadastra);
	}
	
	private void chamaServidorGetDados(final PontoDTO ponto) {
		
		final AsyncCallback<List<SimularDTO>> recuperaCallBack = new AsyncCallback<List<SimularDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Ocorreu um erro na comunicacao com o Servidor! =X");
			}

			@Override
			public void onSuccess(List<SimularDTO> result) {
				
				String texto = "";
				
				texto += "<b>Agente removido:</b> "+ponto.getDescricao();
				texto += "<br><br>";
				
				for (SimularDTO simularDTO : result) {
					texto += "<b>Nome do novo agente:</b> "+simularDTO.getNome();
					texto += "<br><b>Foco Responsavel:</b> "+simularDTO.getDescricao();
					texto += "<br><br>";
				}
				
				mudancas.setHTML(texto);
			}
			
		};
		
		server.simulaDemitir(ponto.getLatitude(), ponto.getLongitude(), recuperaCallBack);
	}
	
	public void povoa(PontoDTO ponto) {
		chamaServidorGetDados(ponto);
	}
	
	public void limpaCampos() {
		mudancas.setHTML("");
	}
	
}
