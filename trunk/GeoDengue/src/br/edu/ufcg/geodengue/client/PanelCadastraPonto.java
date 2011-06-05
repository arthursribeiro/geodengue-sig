package br.edu.ufcg.geodengue.client;

import br.edu.ufcg.geodengue.client.eventos.CadastrarNovoPontoEvento;
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

public class PanelCadastraPonto extends Composite {

	private DecoratorPanel panelCadastra;
	
	public PanelCadastraPonto(String textoAjuda, final boolean ehFoco) {
		
		
		VerticalPanel vPanelFiltros = new VerticalPanel();
		vPanelFiltros.setSize("930px" , "80px");
		vPanelFiltros.setSpacing(5);
		
		vPanelFiltros.add(new Label(textoAjuda));
		
		HorizontalPanel hPanelDesc = new HorizontalPanel();
		Label lblDesc = new Label("Descrição: ");
		final TextBox descricao = new TextBox();
		hPanelDesc.add(lblDesc);
		hPanelDesc.add(descricao);
		
		Button botaoCadastrar = new Button("Cadastrar");
		botaoCadastrar.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				EventBus.getInstance().publica(new CadastrarNovoPontoEvento(descricao.getValue(), ehFoco));
			}
		});

		vPanelFiltros.add(hPanelDesc);
		vPanelFiltros.add(botaoCadastrar);
		
		panelCadastra = new DecoratorPanel();
		panelCadastra.add(vPanelFiltros);
		
		initWidget(panelCadastra);
	}
	
}
