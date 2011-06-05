package br.edu.ufcg.geodengue.client.eventos;

public class CadastrarNovoFocoEvento extends EventoBase {

	private String qntInfectados;
	
	public CadastrarNovoFocoEvento(String qnt) {
		super(TiposDeEventos.CADASTRAR_NOVO_FOCO);
		this.qntInfectados = qnt;
	}

	public String getQntInfectados() {
		return qntInfectados;
	}
	
}
