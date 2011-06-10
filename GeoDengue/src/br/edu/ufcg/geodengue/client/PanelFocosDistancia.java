package br.edu.ufcg.geodengue.client;

import java.util.List;

import br.edu.ufcg.geodengue.client.eventos.Assinante;
import br.edu.ufcg.geodengue.client.eventos.EventBus;
import br.edu.ufcg.geodengue.client.eventos.MarcadorArrastadoEvento;
import br.edu.ufcg.geodengue.client.eventos.TiposDeEventos;
import br.edu.ufcg.geodengue.client.service.GeoDengueService;
import br.edu.ufcg.geodengue.client.service.GeoDengueServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PanelFocosDistancia extends Composite {

	private GeoDengueServiceAsync server = GWT.create(GeoDengueService.class);
	private DecoratorPanel panelFocosDistancia;
	private TextBox distancia;
	private HTML focos;
	
	private TrataNovoPonto trataNovoPonto;
	
	public PanelFocosDistancia(String textoAjuda) {
		
		focos = new HTML();
		
		VerticalPanel vPanelFiltros = new VerticalPanel();
		vPanelFiltros.setSize("930px" , "80px");
		vPanelFiltros.setSpacing(5);
		
		vPanelFiltros.add(new Label(textoAjuda));
		
		Button botaoCalcular = new Button("Calcular");
		botaoCalcular.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String value = distancia.getValue();
				if (value != null && !value.isEmpty()) {
					calculaNovosFocos();
				} else {
					Window.alert("Deve se colocar um valor numerico para a distancia!");
				}
			}
		});
		
		HorizontalPanel hPanelDesc = new HorizontalPanel();
		Label lblDesc = new Label("Distancia desejada: ");
		distancia = new TextBox();
		hPanelDesc.add(lblDesc);
		hPanelDesc.add(distancia);
		hPanelDesc.add(botaoCalcular);
		
		vPanelFiltros.add(hPanelDesc);
		vPanelFiltros.add(new Label("Focos: "));
		vPanelFiltros.add(focos);
		
		panelFocosDistancia = new DecoratorPanel();
		panelFocosDistancia.add(vPanelFiltros);
		
		initWidget(panelFocosDistancia);
		
	}
	
	private void calculaNovosFocos() {
		final Marker marcador = PanelPrincipal.getInstance().getMarcador();
		if (marcador == null) {
			Window.alert("Necessário marcar o ponto no mapa!");
			return;
		}

		try {
			Double.parseDouble(distancia.getValue());
		} catch (Exception e) {
			Window.alert("Deve se colocar um valor numerico para a distancia!");
			return;
		}			
		
		final double distanciaValor = Double.parseDouble(distancia.getValue());
		
		final AsyncCallback<List<String>> calculaCallBack = new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Ocorreu um erro na comunicacao com o Servidor! =X");
			}

			@Override
			public void onSuccess(List<String> result) {
				int i = 1;
				String htmlString = "";
				for (String foco : result) {
					htmlString += "<b>Foco "+i+":</b> " + foco;
					htmlString += "<br>";
					i++;
				}
				focos.setHTML(htmlString);
			}
			
		};
		
		server.focosDistancia(marcador.getLatLng().getLatitude(), marcador.getLatLng().getLongitude(), distanciaValor, calculaCallBack);
	}
	
	public void iniciaTratador() {
		trataNovoPonto = new TrataNovoPonto();
	}
	
	public void assinaTratador() {
		EventBus.getInstance().registraAssinante(TiposDeEventos.MODIFICOU_LOCAL_MARCADOR, trataNovoPonto);
	}
	
	public void removeTratador() {
		EventBus.getInstance().removeAssinante(TiposDeEventos.MODIFICOU_LOCAL_MARCADOR, trataNovoPonto);
	}
	
	private class TrataNovoPonto implements Assinante<MarcadorArrastadoEvento> {
		@Override
		public void trataEvento(MarcadorArrastadoEvento evento) {
			calculaNovosFocos();
		}
	}
	
	public void limpaCampos() {
		focos.setHTML("");
	}
}
