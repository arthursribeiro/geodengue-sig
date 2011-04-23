package br.edu.ufcg.geodengue.server.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

import org.postgis.PGgeometry;

public class GeoDengueDAO {

	private final String SELECT_BAIRROS = "SELECT nome, geometria FROM bairros;";
	private final String url = "jdbc:postgresql:mydb";
	private final String driver = "org.postgresql.Driver";
	private final String usuario = "raquel";
	private final String senha = "raquel";
	
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
		String sql = SELECT_BAIRROS; 
		try {
            PreparedStatement s = conn.prepareStatement(sql);      
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
	
}
