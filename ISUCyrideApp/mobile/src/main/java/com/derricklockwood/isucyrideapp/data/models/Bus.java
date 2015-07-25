package com.derricklockwood.isucyrideapp.data.models;

import com.derricklockwood.isucyrideapp.data.BusGroupCallBack;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Derrick Lockwood on 7/13/15.
 */
public class Bus implements Serializable {

    private String busID;
    private String busIDName;
    private BusGroupCallBack busGroupCallBack;
    private BusDirection busDirection;
    private Stop[] stops;
    public boolean isInMyBusRoute;

    public Bus (String busIDName, BusDirection busDirection, Stop[] stops) {
        this.busIDName = busIDName;
        this.stops = stops;
        this.busDirection = busDirection;
        isInMyBusRoute = false;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass().getName().equalsIgnoreCase(this.getClass().getName())) {
            Bus bus = (Bus) o;
            if (bus.getBusID().equalsIgnoreCase(this.getBusID())) {
                return true;
            }
        }
        return false;
    }

    public Stop[] getStops() {
        return stops;
    }

    public LatLng[] getStopLocations() {
        ArrayList<LatLng> stopLocations = new ArrayList<LatLng>();
        for (Stop stop : stops) {
            stopLocations.add(stop.getStopLocation());
        }
        return stopLocations.toArray(new LatLng[0]);
    }

    private void makeBusID() {
        String id = getFullBusName();
        if (id == null) {
            return;
        }
        busID = id.replace(" ","_");
    }
    public String getFullBusName() {
        if (busGroupCallBack == null) {
            return null;
        }
        return busGroupCallBack.getBusColorName() + " " + busIDName + " " + busDirection.getDirectionName();
    }
    public String getBusID() {
        if (busID == null) {
            makeBusID();
        }
        return busID;
    }
    public int getStopCount() {
        if (stops != null) {
            return stops.length;
        }
        return 0;
    }
    public Stop getStop(int position) {
        if (stops != null) {
            if (position >= 0 && position < stops.length) {
                Stop stop = stops[position];
                stop.updateNextStopTime();
                return stop;
            }
        }
        return null;
    }
    public String getBusIDName() {
        return busIDName;
    }
    public BusDirection getBusDirection() {
        return busDirection;
    }
    public String getBusColorName() {
        return busGroupCallBack.getBusColorName();
    }
    public int getBusColor() {
        return busGroupCallBack.getBusColor();
    }
    public void setBusGroupCallBack(BusGroupCallBack busGroupCallBack) {
        this.busGroupCallBack = busGroupCallBack;
    }
    public static ArrayList<Bus> createBuses(JSONObject busRoutes, Schedule currentSchedule) {
        ArrayList<Bus> buses = new ArrayList<>();
        JSONArray busRouteNames = busRoutes.names();
        for (int i = 0; i<busRouteNames.length(); i++) {
            String busID = busRouteNames.optString(i);
            JSONObject busDirectionsRaw = busRoutes.optJSONObject(busID);
            JSONArray busDirectionIDsRaw = busDirectionsRaw.names();
            for (int j = 0; j<busDirectionIDsRaw.length(); j++) {
                //east_north
                String busDirectionId = busDirectionIDsRaw.optString(j);
                //name...stops
                JSONObject busDirectionRaw = busDirectionsRaw.optJSONObject(busDirectionId);
                BusDirection busDirection = BusDirection.createBusDirection(busDirectionRaw, busDirectionId);
                Stop[] stops = Stop.createStops(busDirectionRaw, currentSchedule);
                buses.add(new Bus(busID, busDirection, stops));
            }
        }
        return buses;
    }

    @Override
    public String toString() {
        return getFullBusName();
    }
}
