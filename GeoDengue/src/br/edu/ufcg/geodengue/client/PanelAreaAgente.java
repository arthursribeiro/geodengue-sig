package br.edu.ufcg.geodengue.client;

import br.edu.ufcg.geodengue.client.service.GeoDengueService;
import br.edu.ufcg.geodengue.client.service.GeoDengueServiceAsync;
import br.edu.ufcg.geodengue.shared.AreaAgenteDTO;
import br.edu.ufcg.geodengue.shared.PontoDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PanelAreaAgente extends Composite implements PanelToggle {

	private GeoDengueServiceAsync server = GWT.create(GeoDengueService.class);
	
	private DecoratorPanel panelCadastra;
	private Label quantidade;
	private Label area;
	private HTML focos;
	
	public PanelAreaAgente() {
		
		quantidade = new Label();
		area = new Label();
		focos = new HTML();
		
		limpaCampos();
		
		VerticalPanel vPanelFiltros = new VerticalPanel();
		vPanelFiltros.setSize("930px" , "80px");
		vPanelFiltros.setSpacing(5);
		
		HorizontalPanel hPanelDesc = new HorizontalPanel();
		Label lblDesc = new Label("Quantidade de Focos: ");
		hPanelDesc.add(lblDesc);
		hPanelDesc.add(quantidade);
		
		HorizontalPanel hPanelDesc2 = new HorizontalPanel();
		Label lblDesc2 = new Label("Área Responsavel: ");
		hPanelDesc2.add(lblDesc2);
		hPanelDesc2.add(area);
		
		vPanelFiltros.add(hPanelDesc);
		vPanelFiltros.add(hPanelDesc2);
		vPanelFiltros.add(new Label("Focos: "));
		vPanelFiltros.add(focos);
		
//		chamaServidorGetDados();
		
		panelCadastra = new DecoratorPanel();
		panelCadastra.add(vPanelFiltros);
		
		initWidget(panelCadastra);
	}
	
	private void chamaServidorGetDados(PontoDTO ponto) {
		
		final AsyncCallback<AreaAgenteDTO> recuperaCallBack = new AsyncCallback<AreaAgenteDTO>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Ocorreu um erro na comunicacao com o Servidor! =X");
			}

			@Override
			public void onSuccess(AreaAgenteDTO result) {
				quantidade.setText(result.getPontos().size()+"");
				area.setText(result.getArea()+"");
				
				int i = 1;
				String htmlString = "";
				for (String foco : result.getPontos()) {
					htmlString += "<b>Foco "+i+":</b> " + foco;
					htmlString += "<br>";
					i++;
				}
				focos.setHTML(htmlString);
			}
			
		};
		
		server.recuperaDadosAreaAgente(ponto.getLatitude(), ponto.getLongitude(), recuperaCallBack);
	}
	
	public void povoa(PontoDTO ponto) {
		chamaServidorGetDados(ponto);
	}
	
	public void limpaCampos() {
		quantidade.setText("");
		area.setText("");
		focos.setHTML("");
	}

	
}
