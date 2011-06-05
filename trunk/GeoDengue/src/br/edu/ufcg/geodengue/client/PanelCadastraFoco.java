package br.edu.ufcg.geodengue.client;

import br.edu.ufcg.geodengue.client.eventos.CadastrarNovoFocoEvento;
import br.edu.ufcg.geodengue.client.eventos.EventBus;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PanelCadastraFoco extends Composite {

	private DecoratorPanel panelCamadas;
	
	public PanelCadastraFoco() {
		
		
		VerticalPanel vPanelFiltros = new VerticalPanel();
		vPanelFiltros.setSize("930px" , "80px");
		vPanelFiltros.setSpacing(5);
		
		Label textoAjuda = new Label("Texto explicando como cadastrar um novo Foco.");
		vPanelFiltros.add(textoAjuda);
		
		HorizontalPanel hPanelInfec = new HorizontalPanel();
		Label lblInfec = new Label("Infectados: ");
		final TextBox infectados = new TextBox();
		hPanelInfec.add(lblInfec);
		hPanelInfec.add(infectados);
		
		Button botaoCadastrar = new Button("Cadastrar");
		botaoCadastrar.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				EventBus.getInstance().publica(new CadastrarNovoFocoEvento(infectados.getValue()));
			}
		});

		vPanelFiltros.add(hPanelInfec);
		vPanelFiltros.add(botaoCadastrar);
		
		panelCamadas = new DecoratorPanel();
		panelCamadas.add(vPanelFiltros);
		
		initWidget(panelCamadas);
	}
	
}
