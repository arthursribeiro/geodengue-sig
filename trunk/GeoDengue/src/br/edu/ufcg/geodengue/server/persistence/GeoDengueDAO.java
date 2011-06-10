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
	
	public void insereAgente(String nome, PontoDTO ponto) throws SQLException {
		String pontoText = String.format("POINT(%f %f)", ponto.getLongitude(), ponto.getLatitude());
		pontoText = pontoText.replace(",", ".");
		
		int id = getBairroID(pontoText);
		if (id == -1) throw new IllegalArgumentException();
		
		int qnt = hasAgenteBairro(id);
		if (qnt > 0) throw new IllegalArgumentException();
		
        PreparedStatement s = conn.prepareStatement(Consultas.INSERT_AGENTE);      
        s.setString(1, nome);
        s.setInt(2, id);
        s.execute();
        s.close();
        
        inserePonto(ponto);
	}
	
	private int hasAgenteBairro(int idBairro) {
		int qnt = -1;
		
		try {
			PreparedStatement s = conn.prepareStatement(Consultas.TEM_AGENTE_BAIRRO);
			s.setInt(1, idBairro);
			ResultSet rs = s.executeQuery();
			while(rs.next()){
				qnt = rs.getInt(1);
			}
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}       
		return qnt;
	}
	
	private int getBairroID(String pontoTexto) {
		
		int id = -1;
		
		try {
			PreparedStatement s = conn.prepareStatement(Consultas.BAIRRO_ID);
			
			s.setString(1, pontoTexto);
			ResultSet rs = s.executeQuery();
			while(rs.next()){
				id = rs.getInt(1);
			}
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}       
		return id;
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
		tool.getAgentes().addAll(recuperaDados(pontoText, Consultas.BAIRRO_AGENTE));
		
		return tool;
	}
	
	public Map<String, String> recuperaBairrosSemResponsaveis() {
		Map<String, String> bairros = new TreeMap<String, String>();
		
		try {
            PreparedStatement s = conn.prepareStatement(Consultas.BAIRROS_SEM_RESP);      
            
            ResultSet rs = s.executeQuery();
            String id, nome;
        	while(rs.next()){
        		id = rs.getString("id");
        		nome = rs.getString("nome");
        		bairros.put(nome, id);
            }
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return bairros;
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
	
	public PontoDTO recuperaPonto(double latitude, double longitude) {
		String pontoText = String.format("POINT(%f %f)", longitude, latitude);
		pontoText = pontoText.replace(",", ".");
		
		String descricao = null;
		int id = 0;

		try {
            PreparedStatement s = conn.prepareStatement(Consultas.DADOS_FOCO);      
            s.setString(1, pontoText);
            
            ResultSet rs = s.executeQuery();
        	while(rs.next()){
            	descricao = (rs.getString("desc"));
            	id = (rs.getInt("id"));
            }
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
        if (descricao == null) return null;
        
        PontoDTO ponto = new PontoDTO(descricao, latitude, longitude, 'F');
        ponto.setId(id);
        
		return ponto;
	}

	public PontoDTO recuperaAgente(double latitude, double longitude) {
		String pontoText = String.format("POINT(%f %f)", longitude, latitude);
		pontoText = pontoText.replace(",", ".");
		
		String descricao = null;
		int id = 0;

		try {
            PreparedStatement s = conn.prepareStatement(Consultas.DADOS_AGENTE);      
            s.setString(1, pontoText);
            
            ResultSet rs = s.executeQuery();
        	while(rs.next()){
            	descricao = (rs.getString("desc"));
            	id = (rs.getInt("id"));
            }
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
        if (descricao == null) return null;
        
        PontoDTO ponto = new PontoDTO(descricao, latitude, longitude, 'A');
        ponto.setId(id);
        
		return ponto;
	}
	
	public double calculaDistanciaEntreFocos(PontoDTO p1, PontoDTO p2) {
		double distancia = 0;
		
		try {
			PreparedStatement s = conn.prepareStatement(Consultas.DISTANCIA_PONTOS);      
			s.setInt(1, p1.getId());
			s.setInt(2, p2.getId());
			ResultSet rs = s.executeQuery();
			while(rs.next()){
				distancia = rs.getDouble(1);
			}
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}       
		return distancia;

	}

	public List<String> getFocosEmBairro(double latitude, double longitude) {
		String pontoText = String.format("POINT(%f %f)", longitude, latitude);
		pontoText = pontoText.replace(",", ".");
		
		int id = getBairroID(pontoText);
		if (id == -1) throw new IllegalArgumentException();
		
		List<String> dados = new ArrayList<String>();
		try {
            PreparedStatement s = conn.prepareStatement(Consultas.FOCOS_NA_AREA);      
            s.setInt(1, id);

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
	
	public double getAreaBairro(double latitude, double longitude) {
		String pontoText = String.format("POINT(%f %f)", longitude, latitude);
		pontoText = pontoText.replace(",", ".");
		
		int id = getBairroID(pontoText);
		if (id == -1) throw new IllegalArgumentException();
		
		double distancia = 0;
		
		try {
			PreparedStatement s = conn.prepareStatement(Consultas.AREA_BAIRRO);   
			s.setInt(1, id);

			ResultSet rs = s.executeQuery();
			while(rs.next()){
				distancia = rs.getDouble(1);
			}
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}       
		return distancia;
	}

	public List<String> recuperaFocosDistancia(double latitude, double longitude, double distancia) {
		String pontoText = String.format("POINT(%f %f)", longitude, latitude);
		pontoText = pontoText.replace(",", ".");
		
		List<String> dados = new ArrayList<String>();
		try {
            PreparedStatement s = conn.prepareStatement(Consultas.FOCOS_DISTANCIA);      
            s.setString(1, pontoText);
            s.setDouble(2, distancia);

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

