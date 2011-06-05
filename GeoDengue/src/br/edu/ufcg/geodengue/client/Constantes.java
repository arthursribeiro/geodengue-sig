package br.edu.ufcg.geodengue.client;

public class Constantes {

	private static final String E_COMERCIAL = "&";

	private static final String GEOSERVER = "http://localhost:8080/geoserver/";
	private static final String GEOSERVER_BAIRROS_LAYER = "layers=geodengue:bairroscampina";
	private static final String GEOSERVER_FOCOS_LAYER = "layers=geodengue:focos";
	private static final String GEOSERVER_GET_MAP = "request=GetMap";
	private static final String GEOSERVER_PNG = "format=image/png";
	private static final String GEOSERVER_SRS = "srs=EPSG:4326";
	private static final String GEOSERVER_TRANSPARENT = "transparent=true";
	private static final String GEOSERVER_WMS = "wms?service=WMS&version=1.1.0";

	public static final String GEOSERVER_MAPA_BAIRROS = GEOSERVER
			+ GEOSERVER_WMS + E_COMERCIAL
			+ GEOSERVER_GET_MAP + E_COMERCIAL
			+ GEOSERVER_BAIRROS_LAYER + E_COMERCIAL
			+ GEOSERVER_SRS	+ E_COMERCIAL
			+ GEOSERVER_PNG + E_COMERCIAL
			+ GEOSERVER_TRANSPARENT;

	public static final String GEOSERVER_MAPA_FOCOS = GEOSERVER 
			+ GEOSERVER_WMS + E_COMERCIAL
			+ GEOSERVER_GET_MAP + E_COMERCIAL
			+ GEOSERVER_FOCOS_LAYER + E_COMERCIAL 
			+ GEOSERVER_SRS + E_COMERCIAL
			+ GEOSERVER_PNG + E_COMERCIAL
			+ GEOSERVER_TRANSPARENT;

}
