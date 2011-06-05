package br.edu.ufcg.geodengue.client.eventos;

public class CadastrarNovoPontoEvento extends EventoBase {

	private String descricao;
	private boolean foco;
	
	public CadastrarNovoPontoEvento(String qnt, boolean ehFoco) {
		super(TiposDeEventos.CADASTRAR_NOVO_PONTO);
		this.descricao = qnt;
		this.foco = ehFoco;
	}

	public String getDescricao() {
		return descricao;
	}

	public boolean isFoco() {
		return foco;
	}
	
}
