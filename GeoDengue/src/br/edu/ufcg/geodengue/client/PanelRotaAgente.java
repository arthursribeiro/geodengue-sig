package br.edu.ufcg.geodengue.client;

import java.util.Collections;
import java.util.List;

import br.edu.ufcg.geodengue.client.service.GeoDengueService;
import br.edu.ufcg.geodengue.client.service.GeoDengueServiceAsync;
import br.edu.ufcg.geodengue.client.utils.GoogleMapsUtil;
import br.edu.ufcg.geodengue.shared.AreaAgenteDTO;
import br.edu.ufcg.geodengue.shared.PontoDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.geocode.DirectionQueryOptions;
import com.google.gwt.maps.client.geocode.DirectionResults;
import com.google.gwt.maps.client.geocode.Directions;
import com.google.gwt.maps.client.geocode.DirectionsCallback;
import com.google.gwt.maps.client.geocode.Waypoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PanelRotaAgente extends Composite implements PanelToggle {

	private GeoDengueServiceAsync server = GWT.create(GeoDengueService.class);
	
	private DecoratorPanel panelCadastra;
	private Label qntFocos;
	private Label comprimento;
	private DirectionQueryOptions dqo;
	private DirectionsCallback dc;
	private Waypoint[] pontosDaRota;
	
	public PanelRotaAgente() {
		
		qntFocos = new Label();
		comprimento = new Label();
		
		limpaCampos();
		
		VerticalPanel vPanelFiltros = new VerticalPanel();
		vPanelFiltros.setSize("930px" , "80px");
		vPanelFiltros.setSpacing(5);
		
		HorizontalPanel hPanelDesc = new HorizontalPanel();
		HTML lblDesc = new HTML("<b>Quantidade de Focos:</b> ");
		hPanelDesc.add(lblDesc);
		hPanelDesc.add(qntFocos);
		
		HorizontalPanel hPanelDesc2 = new HorizontalPanel();
		HTML lblDesc2 = new HTML("<b>Comprimento da rota:</b> ");
		hPanelDesc2.add(lblDesc2);
		hPanelDesc2.add(comprimento);
		
		vPanelFiltros.add(hPanelDesc);
		vPanelFiltros.add(hPanelDesc2);
		
		panelCadastra = new DecoratorPanel();
		panelCadastra.add(vPanelFiltros);
		
		initWidget(panelCadastra);
	}
	
	private void chamaServidorGetDados(final PontoDTO ponto) {
		
		final AsyncCallback<AreaAgenteDTO> recuperaCallBack = new AsyncCallback<AreaAgenteDTO>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Ocorreu um erro na comunicacao com o Servidor! =X");
			}

			@Override
			public void onSuccess(AreaAgenteDTO result) {
				PanelPrincipal.getInstance().removePolyline();
				comprimento.setText("");
				qntFocos.setText(result.getPontos().size()+"");
				
				if (result.getPontos().size() >= 2) {
					colocaRota(ponto);
				}
				
			}
			
		};
		
		server.recuperaDadosAreaAgente(ponto.getLatitude(), ponto.getLongitude(), recuperaCallBack);
	}
	
	public void povoa(PontoDTO ponto) {
		chamaServidorGetDados(ponto);
		
		if(dqo == null) {
			dqo = new DirectionQueryOptions(PanelPrincipal.getInstance().getMapa());
	        dqo.setRetrievePolyline(true);
			
	        dc = new DirectionsCallback() {
	
				@Override
				public void onFailure(int statusCode) {
					Window.alert("Ocorreu um erro no calculo da rota =~");
				}
	
				@Override
				public void onSuccess(DirectionResults result) {
					PanelPrincipal.getInstance().adicionaPolyline(result.getPolyline(), result.getMarkers());
					comprimento.setText(result.getDistance().inMeters()+"");
				}
	        };
		}
	}
	
	public void limpaCampos() {
		qntFocos.setText("");
		comprimento.setText("");
//		PanelPrincipal.getInstance().removePolyline();
	}

	protected void calculaRota(Waypoint[] pontosDaRota) {
        Directions.loadFromWaypoints(pontosDaRota, dqo, dc);
	}
	
	private void colocaRota(PontoDTO ponto) {
		final AsyncCallback<List<String>> distanciaCallBack = new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Ocorreu um erro na comunicacao com o Servidor! =X");
			}

			@Override
			public void onSuccess(List<String> result) {
				pontosDaRota = null;
				pontosDaRota = new Waypoint[result.size()];
				
				Collections.sort(result);
				
				int i = 0;
				for (String string : result) {
					pontosDaRota[i] = new Waypoint(GoogleMapsUtil.stringToLatLng(string));
					i++;
				}
				
				calculaRota(pontosDaRota);
			}

		};

		server.getPontosEmBairro(ponto.getLatitude(), ponto.getLongitude(), distanciaCallBack);
	}
	
}
