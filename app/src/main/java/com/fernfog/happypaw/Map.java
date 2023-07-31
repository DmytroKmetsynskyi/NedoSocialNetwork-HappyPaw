package com.fernfog.happypaw;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

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
        mapController.setZoom(22);

        GeoPoint startPoint = new GeoPoint(latitude, longitude);
        mapController.setCenter(startPoint);

        Marker startMarker = new Marker(map);
        startMarker.setIcon(icon);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        map.getOverlays().add(startMarker);
    }
}
