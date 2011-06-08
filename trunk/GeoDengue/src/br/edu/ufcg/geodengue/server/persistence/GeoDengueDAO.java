package br.edu.ufcg.geodengue.server.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.postgis.PGgeometry;

import br.edu.ufcg.geodengue.shared.AgenteDTO;
import br.edu.ufcg.geodengue.shared.PontoDTO;
import br.edu.ufcg.geodengue.shared.RaioDTO;
import br.edu.ufcg.geodengue.shared.TooltipDTO;

public class GeoDengueDAO {

	private final String url = "jdbc:postgresql:mydb";
	private final String driver = "org.postgresql.Driver";
	private final String usuario = "raquel";
	private final String senha = "senha";
	
	private Connection conn;
	
	public GeoDengueDAO(){
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, usuario, senha);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Map<String,String> getMapaBairros() {
		Map<String,String> mapaBairros = new TreeMap<String, String>();
		try {
            PreparedStatement s = conn.prepareStatement(Consultas.SELECT_BAIRROS);      
            ResultSet rs = s.executeQuery();
            while(rs.next()){
            	String nomeBairro = (String)(rs.getObject("nome"));
            	PGgeometry pg = (PGgeometry)(rs.getObject("geometria"));
            	mapaBairros.put(nomeBairro, pg.toString());
            }
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mapaBairros;
	}
	
	public AgenteDTO getAgente(String login, String senha) {
		AgenteDTO agente = null;
		
		try {
            PreparedStatement s = conn.prepareStatement(Consultas.LOGIN_AGENTE);      
            s.setString(1, login);
            s.setString(2, senha);
            
            ResultSet rs = s.executeQuery();
            while(rs.next()){
            	agente = new AgenteDTO(((String)(rs.getObject("nome"))),10,10,10,10);
            }
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return agente;
	}
	
	public void inserePonto(PontoDTO ponto) throws SQLException {
		String pontoText = String.format("POINT(%f %f)", ponto.getLongitude(), ponto.getLatitude());
		pontoText = pontoText.replace(",", ".");
        PreparedStatement s = conn.prepareStatement(Consultas.INSERT_PONTO);      
        s.setString(1, ponto.getTipo()+"");
        s.setString(2, ponto.getDescricao());
        s.setString(3, pontoText);
        s.execute();
        s.close();
	}
	
	public long pessoasRaio(RaioDTO raio) {
		String pontoText = String.format("POINT(%f %f)", raio.getLongitude(), raio.getLatitude());
		pontoText = pontoText.replace(",", ".");
		long pessoasContaminadas = -1; // Erro
		try {
            PreparedStatement s = conn.prepareStatement(Consultas.PESSOAS_RAIO);      
            s.setString(1, pontoText);
            s.setDouble(2, raio.getRaio());
            
            ResultSet rs = s.executeQuery();
            while(rs.next()){
            	pessoasContaminadas = (Long) (rs.getObject("valor"));
            }
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return pessoasContaminadas;
	}
	
	public TooltipDTO recuperaDadosTooltip(double latitude, double longitude) {
		String pontoText = String.format("POINT(%f %f)", longitude, latitude);
		pontoText = pontoText.replace(",", ".");

		TooltipDTO tool = new TooltipDTO(latitude, longitude);
		
		tool.getFocos().addAll(recuperaDados(pontoText, Consultas.DADOS_FOCO));
		tool.getPessoas().addAll(recuperaDados(pontoText, Consultas.DADOS_PESSOA));
		tool.getBairros().addAll(recuperaDados(pontoText, Consultas.DADOS_BAIRRO));
		
		return tool;
	}
	
	private List<String> recuperaDados(String pontoText, String consulta) {
		List<String> dados = new ArrayList<String>();
		try {
            PreparedStatement s = conn.prepareStatement(consulta);      
            s.setString(1, pontoText);

            String descricao;
            
            ResultSet rs = s.executeQuery();
        	while(rs.next()){
            	descricao = (rs.getString("desc"));
            	if (!descricao.isEmpty()) dados.add(descricao);
            }
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return dados;
	}
	
}
