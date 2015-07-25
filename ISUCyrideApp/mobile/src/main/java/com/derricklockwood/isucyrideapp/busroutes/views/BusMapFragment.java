package com.derricklockwood.isucyrideapp.busroutes.views;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.derricklockwood.isucyrideapp.R;
import com.derricklockwood.isucyrideapp.data.models.Bus;
import com.derricklockwood.isucyrideapp.data.models.Stop;
import com.derricklockwood.isucyrideapp.main.CyrideFragmentCallBack;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Created by Derrick Lockwood on 7/22/15.
 */
public class BusMapFragment extends Fragment implements OnMapReadyCallback {

    CyrideFragmentCallBack cyrideFragmentCallBack;
    Button backButton;
    MapView mapView;
    GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bus_map_view, null);
        mapView = (MapView) view.findViewById(R.id.bus_map_view);
        backButton = (Button) view.findViewById(R.id.back_button);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        backButton.setOnClickListener(cyrideFragmentCallBack.getBusBackButtonHandler());

        MapsInitializer.initialize(this.getActivity());

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng ISU = new LatLng(42.02662,-93.64647);

        googleMap.setMyLocationEnabled(false);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ISU, 15));
        googleMap.setIndoorEnabled(false);
        Bus selectedMapBus = cyrideFragmentCallBack.getSelectedMapBus();
        for (MarkerOptions markerOption : getBusMarkers(selectedMapBus.getStops())) {
            googleMap.addMarker(markerOption);
        }
        googleMap.addPolyline(getRouteLine(selectedMapBus.getStops(), selectedMapBus.getBusColor()));

    }
    private PolylineOptions getRouteLine(Stop[] stops, int busColor) {
        PolylineOptions polylineOptions = new PolylineOptions();
        for (Stop stop : stops) {
            LatLng stopLocation = stop.getStopLocation();
            if (stopLocation != null) {
                polylineOptions.add(stopLocation);
            }
        }
        polylineOptions.color(busColor);
        return polylineOptions;
    }
    private void setupMapWithMarkers(GoogleMap googleMap, MarkerOptions[] markerOptions) {

    }
    private MarkerOptions[] getBusMarkers(Stop[] stops) {
        ArrayList<MarkerOptions> markerOptions = new ArrayList<>();
        for (Stop stop : stops) {
            LatLng stopLocation = stop.getStopLocation();
            if (stopLocation == null) {
                continue;
            }
            MarkerOptions markerOption = new MarkerOptions();
            markerOption.draggable(false);
            markerOption.alpha(0.7f);
            markerOption.position(stopLocation);
            markerOption.title(stop.getStopName());
            markerOptions.add(markerOption);
        }
        return markerOptions.toArray(new MarkerOptions[0]);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        cyrideFragmentCallBack = (CyrideFragmentCallBack) activity;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
