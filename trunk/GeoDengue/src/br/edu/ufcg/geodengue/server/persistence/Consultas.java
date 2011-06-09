package br.edu.ufcg.geodengue.server.persistence;

public class Consultas {

	public static final String SELECT_BAIRROS = "SELECT nome, geometria " +
												"FROM bairros;";
	
	public static final String LOGIN_AGENTE = "SELECT * " +
												"FROM agente " +
												"WHERE login = ? " +
												"AND senha = ?;";
	
	public static final String INSERT_PONTO = "INSERT INTO ponto (tipo, descricao, geom)  VALUES(?, ?, GeometryFromText(?,4326))";
	
	public static final String INSERT_AGENTE = "INSERT INTO agente (nome, bairroResp)  VALUES(?, ?)";

	public static final String PESSOAS_RAIO = "SELECT COUNT(*) AS valor " +
												"FROM ponto p " +
												"WHERE p.tipo = 'P' " +
												"AND Contains(Buffer(GeometryFromText(?, 4326), ?), p.geom)"; 
	
	public static final String DADOS_FOCO = "SELECT p.descricao AS desc, p.id AS id " +
											"FROM Ponto p " +
											"WHERE p.tipo = 'F' " +
											"AND Contains(Buffer(GeometryFromText(?, 4326), 0.001), p.geom)";

	public static final String DADOS_PESSOA = "SELECT p.descricao AS desc " +
												"FROM Ponto p " +
												"WHERE p.tipo = 'P' " +
												"AND Contains(Buffer(GeometryFromText(?, 4326), 0.001), p.geom)";

	public static final String DADOS_BAIRRO = "SELECT b.nome AS desc " +
												"FROM bairroscampina b " +
												"WHERE Contains(b.the_geom, Buffer(Transform(GeometryFromText(?, 4326),29195), 0.001))";
	
	public static final String BAIRROS_SEM_RESP = "SELECT b.gid AS id, b.nome AS nome " +
												"FROM bairroscampina b " +
												"WHERE b.gid NOT IN (SELECT bairroResp FROM agente) ORDER BY b.nome";
	
	public static final String DISTANCIA_PONTOS = "SELECT distance_sphere(p1.geom, p2.geom) AS distancia " +
													"FROM Ponto p1, Ponto p2 " +
													"WHERE p1.tipo = 'F' " +
													"AND p2.tipo = 'F' " +
													"AND p1.id = ? " +
													"AND p2.id = ?";
	
	public static final String BAIRRO_AGENTE = "SELECT p.descricao AS desc, p.id AS id " +
												"FROM Ponto p, bairroscampina ba, Agente a " +
												"WHERE p.tipo = 'A' AND " +
												"a.bairroResp = ba.gid AND " +
												"Contains(ba.the_geom, Transform(p.geom, 29195)) AND " +
												"Contains(Buffer(GeometryFromText(?, 4326), 0.001), p.geom)";
	
	public static final String BAIRRO_ID = "SELECT ba.gid " +
											"FROM bairroscampina ba " +
											"WHERE Contains(ba.the_geom, Buffer(Transform(GeometryFromText(?, 4326),29195), 0.001))";
}
