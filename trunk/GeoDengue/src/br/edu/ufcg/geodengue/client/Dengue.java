package br.edu.ufcg.geodengue.client;

import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.overlay.Polygon;

// FIXME APAGAR ESSA CLASSE

public class Dengue {

	public static Map<String, Polygon> criaMapaDengue(){
		
		Map<String, Polygon> dengues = new TreeMap<String, Polygon>();
		
		Polygon tipo1 = new Polygon(drawCircleFromRadius(LatLng.newInstance(-7.235615495848395, -35.899715423583984), 100,100), "green", 1, 1, "green", 0.1);
		dengues.put("Tipo 1", tipo1);

		Polygon tipo2 = new Polygon(drawCircleFromRadius(LatLng.newInstance(-7.214626227248793, -35.907912254333496), 255,100), "green", 1, 1, "green", 0.1);
		dengues.put("Tipo 2", tipo2);
		
		Polygon tipo3 = new Polygon(drawCircleFromRadius(LatLng.newInstance(-7.238723357379842, -35.919198989868164), 92,100), "green", 1, 1, "green", 0.1);
		dengues.put("Tipo 3", tipo3);
		
		return dengues;
	}
	
	private static LatLng[] drawCircleFromRadius(LatLng center, double radius, int nbOfPoints) {

		LatLngBounds bounds = LatLngBounds.newInstance();
		LatLng[] circlePoints = new LatLng[nbOfPoints+1];

		double EARTH_RADIUS = 6371000;
		double d = radius / EARTH_RADIUS;
		double lat1 = Math.toRadians(center.getLatitude());
		double lng1 = Math.toRadians(center.getLongitude());

		double a = 0;
		double step = 360.0 / (double) nbOfPoints;
		for (int i = 0; i <= nbOfPoints; i++) {
			double tc = Math.toRadians(a);
			double lat2 = Math.asin(Math.sin(lat1) * Math.cos(d) + Math.cos(lat1)
					* Math.sin(d) * Math.cos(tc));
			double lng2 = lng1
			+ Math.atan2(Math.sin(tc) * Math.sin(d) * Math.cos(lat1),
					Math.cos(d) - Math.sin(lat1) * Math.sin(lat2));
			LatLng point = LatLng.newInstance(Math.toDegrees(lat2), Math
					.toDegrees(lng2));
			circlePoints[i] = point;
			bounds.extend(point);
			a += step;
		}

		return circlePoints;
	}
	
}
