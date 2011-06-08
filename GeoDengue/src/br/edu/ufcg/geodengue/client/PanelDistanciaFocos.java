package br.edu.ufcg.geodengue.client;

import br.edu.ufcg.geodengue.client.service.GeoDengueService;
import br.edu.ufcg.geodengue.client.service.GeoDengueServiceAsync;
import br.edu.ufcg.geodengue.shared.PontoDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PanelDistanciaFocos extends Composite implements PanelToggle {

	private TextBox foco1;
	private TextBox foco2;
	private TextBox distancia;
	
	private PontoDTO desc1;
	private PontoDTO desc2;
	
	private GeoDengueServiceAsync server = GWT.create(GeoDengueService.class);
	
	public PanelDistanciaFocos(String textoAjuda) {
		
		desc1 = new PontoDTO();
		desc2 = new PontoDTO();
		
		VerticalPanel vPanelFiltros = new VerticalPanel();
		vPanelFiltros.setSize("930px" , "80px");
		vPanelFiltros.setSpacing(5);
		
		vPanelFiltros.add(new Label(textoAjuda));
		
		HorizontalPanel hPanelDesc = new HorizontalPanel();
		Label lblDesc = new Label("Foco 1: ");
		foco1 = new TextBox();
		foco1.setEnabled(false);
		foco1.setValue(desc1.getDescricao());
		hPanelDesc.add(lblDesc);
		hPanelDesc.add(foco1);
		
		HorizontalPanel hPanelDesc2 = new HorizontalPanel();
		Label lblDesc2 = new Label("Foco 2: ");
		foco2 = new TextBox();
		foco2.setEnabled(false);
		foco2.setValue(desc2.getDescricao());
		hPanelDesc2.add(lblDesc2);
		hPanelDesc2.add(foco2);
		
		HorizontalPanel hPanelDesc3 = new HorizontalPanel();
		Label lblDesc3 = new Label("Distância: ");
		distancia = new TextBox();
		distancia.setEnabled(false);
		hPanelDesc3.add(lblDesc3);
		hPanelDesc3.add(distancia);


		vPanelFiltros.add(hPanelDesc);
		vPanelFiltros.add(hPanelDesc2);
		vPanelFiltros.add(hPanelDesc3);
		
		DecoratorPanel panelDistancia = new DecoratorPanel();
		panelDistancia.add(vPanelFiltros);
		
		initWidget(panelDistancia);
	}
	
	public void novoFoco(PontoDTO novo) {
		if (desc1 == null) {
			limpaCampos();
			desc1 = novo;
			foco1.setValue(desc1.getDescricao());
		} else if (desc2 == null) {
			desc2 = novo;
			foco2.setValue(desc2.getDescricao());
			calculaDistancia();
		} else {
			limpaCampos();
			desc1 = novo;
			foco1.setValue(desc1.getDescricao());
			desc2 = null;
		}
	}
	
	private void calculaDistancia() {
		
		final AsyncCallback<Double> distanciaCallBack = new AsyncCallback<Double>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Ocorreu um erro na comunicacao com o Servidor! =X");
			}

			@Override
			public void onSuccess(Double result) {
				distancia.setValue(result.toString());
			}

		};

		server.calculaDistanciaEntreFocos(desc1, desc2, distanciaCallBack);
	}

	@Override
	public void limpaCampos() {
		desc1 = null;
		desc2 = null;
		foco1.setValue("");
		foco2.setValue("");
		distancia.setValue("");
	}

}

