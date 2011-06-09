package br.edu.ufcg.geodengue.client;

import br.edu.ufcg.geodengue.client.eventos.Assinante;
import br.edu.ufcg.geodengue.client.eventos.EventBus;
import br.edu.ufcg.geodengue.client.eventos.MarcadorArrastadoEvento;
import br.edu.ufcg.geodengue.client.eventos.TiposDeEventos;
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
	private TextBox raio;
	private TextBox pessoasRaio;
	
	private TrataNovoCalculoPoligono trataCalculo;
	
	public PanelPessoasRaio(String textoAjuda) {
		
		VerticalPanel vPanelFiltros = new VerticalPanel();
		vPanelFiltros.setSize("930px" , "80px");
		vPanelFiltros.setSpacing(5);
		
		vPanelFiltros.add(new Label(textoAjuda));
		
		HorizontalPanel hPanelDesc = new HorizontalPanel();
		Label lblDesc = new Label("Raio desejado: ");
		raio = new TextBox();
		hPanelDesc.add(lblDesc);
		hPanelDesc.add(raio);
		
		Button botaoCalcular = new Button("Calcular");
		botaoCalcular.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String value = raio.getValue();
				if (value != null && !value.isEmpty()) {
					calculaPessoasRaio();
				} else {
					Window.alert("Digite um valor para o raio!");
				}
			}
		});
		
		HorizontalPanel hPanelDesc2 = new HorizontalPanel();
		Label lblDesc2 = new Label("Pessoas no Raio: ");
		pessoasRaio = new TextBox();
		pessoasRaio.setValue("0");
		pessoasRaio.setEnabled(false);
		hPanelDesc2.add(lblDesc2);
		hPanelDesc2.add(pessoasRaio);

		vPanelFiltros.add(hPanelDesc);
		vPanelFiltros.add(hPanelDesc2);
		vPanelFiltros.add(botaoCalcular);
		
		panelPessoasRaio = new DecoratorPanel();
		panelPessoasRaio.add(vPanelFiltros);
		
		initWidget(panelPessoasRaio);
		

		
	}
	
	private void calculaPessoasRaio() {
		final Marker marcador = PanelPrincipal.getInstance().getMarcador();
		if (marcador == null) {
			Window.alert("Necessário marcar o ponto no mapa!");
			return;
		}

		try {
			Double.parseDouble(raio.getValue());
		} catch (Exception e) {
			Window.alert("Deve se colocar um valor numerico para o raio!");
			return;
		}			
		
		final double raioValor = Double.parseDouble(raio.getValue());
		
		final AsyncCallback<Long> calculaCallBack = new AsyncCallback<Long>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Ocorreu um erro na comunicacao com o Servidor! =X");
			}

			@Override
			public void onSuccess(Long result) {
				Polygon poligono = new Polygon(GoogleMapsUtil.drawCircleFromRadius(marcador.getLatLng(), raioValor, 15), "blue", 1, 1, "blue", 0.2);
				PanelPrincipal.getInstance().adicionaPoligonoNoMapa(poligono);
				pessoasRaio.setValue(result.toString());
			}
			
		};
		
		server.pessoasRaio(new RaioDTO(marcador.getLatLng().getLatitude(), marcador.getLatLng().getLongitude(), raioValor), calculaCallBack);
	}
	
	public void iniciaTratador() {
		trataCalculo = new TrataNovoCalculoPoligono();
	}
	
	public void assinaTratador() {
		EventBus.getInstance().registraAssinante(TiposDeEventos.MODIFICOU_LOCAL_MARCADOR, trataCalculo);
	}
	
	public void removeTratador() {
		EventBus.getInstance().removeAssinante(TiposDeEventos.MODIFICOU_LOCAL_MARCADOR, trataCalculo);
	}
	
	private class TrataNovoCalculoPoligono implements Assinante<MarcadorArrastadoEvento> {
		@Override
		public void trataEvento(MarcadorArrastadoEvento evento) {
			calculaPessoasRaio();
		}
	}
	
}
