CREATE TABLE bairros (
	nome varchar(64)
);

SELECT AddGeometryColumn('bairros','geometria',-1,'POLYGON',2);

CREATE TABLE agente (
	id SERIAL PRIMARY KEY,
	login varchar(10),
	senha varchar(10),
	nome varchar(30),
	bairroresp integer references bairroscampina(gid)
);

CREATE TABLE Ponto (
	id serial PRIMARY KEY,
	descricao varchar(100),
	tipo char
);
SELECT AddGeometryColumn('ponto','geom',4326,'POINT',2);
