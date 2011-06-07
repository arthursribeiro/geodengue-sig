package br.edu.ufcg.geodengue.client.utils;

import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;

public class GoogleMapsUtil {

	public static LatLng[] drawCircleFromRadius(LatLng center, double radius, int nbOfPoints) {

		LatLngBounds bounds = LatLngBounds.newInstance();
		LatLng[] circlePoints = new LatLng[nbOfPoints+1];

		double EARTH_RADIUS = 6371000;
		double d = (radius*100000) / EARTH_RADIUS;
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
