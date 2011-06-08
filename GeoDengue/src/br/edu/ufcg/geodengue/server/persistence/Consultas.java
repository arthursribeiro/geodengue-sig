package br.edu.ufcg.geodengue.server.persistence;

public class Consultas {

	public static final String SELECT_BAIRROS = "SELECT nome, geometria FROM bairros;";
	public static final String LOGIN_AGENTE = "SELECT * FROM agente WHERE login = ? AND senha = ?;";
	public static final String INSERT_PONTO = "INSERT INTO ponto (tipo, descricao, geom)  VALUES(?, ?, GeometryFromText(?,4326))";
	public static final String PESSOAS_RAIO = "SELECT COUNT(*) AS valor FROM ponto p WHERE p.tipo = 'P' AND Contains(Buffer(GeometryFromText(?, 4326), ?), p.geom)"; 
	
	public static final String DADOS_FOCO = "SELECT p.descricao AS desc FROM Ponto p WHERE p.tipo = 'F' AND Contains(Buffer(GeometryFromText(?, 4326), 0.001), p.geom)";
	public static final String DADOS_PESSOA = "SELECT p.descricao AS desc FROM Ponto p WHERE p.tipo = 'P' AND Contains(Buffer(GeometryFromText(?, 4326), 0.001), p.geom)";
	public static final String DADOS_BAIRRO = "SELECT b.nome AS desc FROM bairroscampina b WHERE Contains(b.the_geom, Buffer(Transform(GeometryFromText(?, 4326),29195), 0.001))";
	
}
