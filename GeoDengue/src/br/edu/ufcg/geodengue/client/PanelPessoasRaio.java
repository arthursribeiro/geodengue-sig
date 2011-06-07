package br.edu.ufcg.geodengue.client;

import br.edu.ufcg.geodengue.client.service.GeoDengueService;
import br.edu.ufcg.geodengue.client.service.GeoDengueServiceAsync;
import br.edu.ufcg.geodengue.client.utils.GoogleMapsUtil;
import br.edu.ufcg.geodengue.shared.RaioDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PanelPessoasRaio extends Composite {

	private GeoDengueServiceAsync server = GWT.create(GeoDengueService.class);
	private DecoratorPanel panelPessoasRaio;
	
	public PanelPessoasRaio(String textoAjuda) {
		
		VerticalPanel vPanelFiltros = new VerticalPanel();
		vPanelFiltros.setSize("930px" , "80px");
		vPanelFiltros.setSpacing(5);
		
		vPanelFiltros.add(new Label(textoAjuda));
		
		HorizontalPanel hPanelDesc = new HorizontalPanel();
		Label lblDesc = new Label("Raio desejado: ");
		final TextBox raio = new TextBox();
		hPanelDesc.add(lblDesc);
		hPanelDesc.add(raio);
		
		Button botaoCalcular = new Button("Calcular");
		botaoCalcular.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String value = raio.getValue();
				if (value != null && !value.isEmpty()) {
					try {
						Integer.parseInt(value);
					} catch (Exception e) {
						Window.alert("Deve se colocar um valor inteiro para o raio!");
					}			
					calculaPessoasRaio(Integer.parseInt(value));
				} else {
					Window.alert("Digite um valor para o raio!");
				}
			}
		});

		vPanelFiltros.add(hPanelDesc);
		vPanelFiltros.add(botaoCalcular);
		
		panelPessoasRaio = new DecoratorPanel();
		panelPessoasRaio.add(vPanelFiltros);
		
		initWidget(panelPessoasRaio);
	}
	
	private void calculaPessoasRaio(final int raio) {
		final Marker marcador = PanelPrincipal.getInstance().getMarcador();
		if (marcador == null) {
			Window.alert("Necessário marcar o ponto no mapa!");
			return;
		}
		
		final AsyncCallback<Long> calculaCallBack = new AsyncCallback<Long>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Ocorreu um erro na comunicacao com o Servidor! =X");
			}

			@Override
			public void onSuccess(Long result) {
				Polygon poligono = new Polygon(GoogleMapsUtil.drawCircleFromRadius(marcador.getLatLng(), raio, 10), "yellow", 1, 1, "yellow", 0.1);
				PanelPrincipal.getInstance().adicionaPoligonoNoMapa(poligono);
				Window.alert("Pessoas no raio: "+ result);
			}
			
		};
		
		
		server.pessoasRaio(new RaioDTO(marcador.getLatLng().getLatitude(), marcador.getLatLng().getLongitude(), raio), calculaCallBack);
	
	}
	
}
