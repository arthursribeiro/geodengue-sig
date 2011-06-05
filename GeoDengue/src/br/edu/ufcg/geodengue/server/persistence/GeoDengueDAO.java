package br.edu.ufcg.geodengue.server.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

import org.postgis.PGgeometry;

import br.edu.ufcg.geodengue.shared.AgenteDTO;
import br.edu.ufcg.geodengue.shared.PontoDTO;

public class GeoDengueDAO {

	private final String SELECT_BAIRROS = "SELECT nome, geometria FROM bairros;";
	private final String LOGIN_AGENTE = "SELECT * FROM agente WHERE login = ? AND senha = ?;";
	private final String INSERT_PONTO = "INSERT INTO ponto (tipo, descricao, geom)  VALUES(?, ?, GeometryFromText(?,4326))";	
	
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
            PreparedStatement s = conn.prepareStatement(SELECT_BAIRROS);      
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
            PreparedStatement s = conn.prepareStatement(LOGIN_AGENTE);      
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
		String pontoText = String.format("POINT(%f %f)", ponto.getLatitude(), ponto.getLongitude());
		
        PreparedStatement s = conn.prepareStatement(INSERT_PONTO);      
        s.setString(1, ponto.getTipo()+"");
        s.setString(2, ponto.getDescricao());
        s.setString(3, pontoText);
        s.execute();
        s.close();
	}
	
}
