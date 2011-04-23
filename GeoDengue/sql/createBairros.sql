CREATE TABLE bairros (
	nome varchar(64)
);

SELECT AddGeometryColumn('bairros','geometria',-1,'POLYGON',2);
