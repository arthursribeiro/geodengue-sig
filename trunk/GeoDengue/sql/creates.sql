CREATE TABLE bairros (
	nome varchar(64)
);

SELECT AddGeometryColumn('bairros','geometria',-1,'POLYGON',2);

CREATE TABLE agente (
	id SERIAL PRIMARY KEY,
	login varchar(10),
	senha varchar(10),
	nome varchar(30)
);
