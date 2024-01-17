package com.fernfog.happypaw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;

public class Map {

    private MapView map;
    int mapViewId;
    double latitude;
    double longitude;
    Context ctx;
    Drawable icon;

    Map(double latitude, double longitude, Context ctx, MapView map, Drawable icon) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.ctx = ctx;
        this.map = map;
        this.icon = icon;
    }

    public void initMap() {
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(false);
        map.setMultiTouchControls(true);
        map.setHorizontalMapRepetitionEnabled(false);
        map.setVerticalMapRepetitionEnabled(false);
        map.setMaxZoomLevel(20.0);
        map.setMinZoomLevel(4.0);
        map.setScrollableAreaLimitLatitude(MapView.getTileSystem().getMaxLatitude(), MapView.getTileSystem().getMinLatitude(), 0);

        MapController mapController = (MapController) map.getController();
        mapController.setZoom(15);

        GeoPoint startPoint = new GeoPoint(latitude, longitude);
        mapController.setCenter(startPoint);

        Marker startMarker = new Marker(map);
        startMarker.setIcon(icon);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);

        map.getOverlays().add(startMarker);

        CircleOverlay circleOverlay = new CircleOverlay(startPoint, 1000);
        map.getOverlays().add(circleOverlay);
    }

    private class CircleOverlay extends Overlay {

        private GeoPoint center;
        private float radius; // in meters

        CircleOverlay(GeoPoint center, float radius) {
            super();
            this.center = center;
            this.radius = radius;
        }

        @Override
        public void draw(Canvas canvas, MapView mapView, boolean shadow) {
            if (!shadow) {
                Paint paint = new Paint();
                paint.setColor(Color.parseColor("#510C7379"));
                paint.setStyle(Paint.Style.FILL);
                paint.setAntiAlias(true);

                // Convert radius from meters to pixels
                float radiusPixels = mapView.getProjection().metersToPixels(radius);

                // Get the screen coordinates of the center point
                android.graphics.Point centerPoint = mapView.getProjection().toPixels(center, null);

                // Draw the circle on the canvas
                canvas.drawCircle(centerPoint.x, centerPoint.y, radiusPixels, paint);
            }
        }
    }
}
