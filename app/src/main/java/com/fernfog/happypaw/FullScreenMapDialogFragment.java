package com.fernfog.happypaw;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.osmdroid.views.MapView;

import javax.annotation.Nullable;

public class FullScreenMapDialogFragment extends DialogFragment {

    private double latitude;
    private double longitude;
    private MapView map;
    private Drawable icon;

    public FullScreenMapDialogFragment(double latitude, double longitude, Drawable icon) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.icon = icon;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_full_screen_map, container, false);

        map = view.findViewById(R.id.fullScreenMapView);
        Map map1 = new Map(latitude, longitude, view.getContext(), map, icon);
        map1.initMap();

        return view;
    }
}
