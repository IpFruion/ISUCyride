package com.derricklockwood.isucyrideapp.data.models;

import android.graphics.Color;

import com.derricklockwood.isucyrideapp.data.BusGroupCallBack;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Derrick Lockwood on 7/15/15.
 */
public class BusGroup implements Serializable, BusGroupCallBack {
    private static final String BUSES_ID = "buses";
    private static final String BUS_COLOR_ID = "color";
    private static final String BUS_ROUTES_ID = "routes";

    private String busColorName;
    private ArrayList<Bus> buses;
    private int busGroupColor;

    public BusGroup(String busColorName, int busGroupHexColor, Bus[] buses) {
        this(busColorName, busGroupHexColor, BusGroup.busListFromArray(buses));
    }
    public BusGroup(String busColorName, String busGroupHexColor, Bus[] buses) {
        this(busColorName, busGroupHexColor, BusGroup.busListFromArray(buses));
    }
    public BusGroup(String busColorName, String busGroupHexColor, ArrayList<Bus> buses) {
        this(busColorName, Color.parseColor("#" + busGroupHexColor), buses);
    }
    public BusGroup(String busColorName, int busGroupHexColor, ArrayList<Bus> buses) {
        fillInBusData(buses);
        this.busColorName = busColorName;
        this.buses = buses;
        this.busGroupColor = busGroupHexColor;
    }
    private void fillInBusData(ArrayList<Bus> buses) {
        for (Bus bus : buses) {
            bus.setBusGroupCallBack(this);
        }
    }
    private static ArrayList<Bus> busListFromArray(Bus[] buses) {
        ArrayList<Bus> busArrayList = new ArrayList<Bus>();
        for (Bus bus : buses) {
            busArrayList.add(bus);
        }
        return busArrayList;
    }

    //Getters
    public int getBusGroupColor() {
        return busGroupColor;
    }
    public Bus[] getBuses() {
        return buses.toArray(new Bus[0]);
    }
    public void addBus(Bus bus) {
        buses.add(bus);
    }
    public void removeBus(Bus bus) {
        buses.remove(bus);
    }
    public int getBusCount() {
        return buses.size();
    }
    public Bus getBus(int position) {
        if (position >= 0 && position < buses.size()) {
            return buses.get(position);
        }
        return null;
    }
    public String getBusGroupName() {
        return busColorName;
    }
    public boolean containsBus(Bus bus) {
        for (Bus busIterator : buses) {
            if (busIterator.equals(bus)) {
                return true;
            }
        }
        return false;
    }

    //Create Methods
    public static BusGroup[] createBusesSortedByColor(Bus[] buses) {
        String[] busColorNames = createBusColorNames(buses);
        int[] busColors = createBusColors(buses);
        BusGroup[] busGroups = new BusGroup[busColorNames.length];
        for (int i = 0; i < busColorNames.length; i++) {
            busGroups[i] = createBusGroupFromBusColorName(busColorNames[i], buses);
        }
        return busGroups;
    }
    private static BusGroup createBusGroupFromBusColorName(String busColorName, Bus[] buses) {
        ArrayList<Bus> busesForBusGroup = new ArrayList<Bus>();
        for (Bus bus : buses) {
            if (bus.getBusColorName().equals(busColorName)) {
                busesForBusGroup.add(bus);
            }
        }
        if (busesForBusGroup.size() > 0) {
            return new BusGroup(busColorName, busesForBusGroup.get(0).getBusColor(), busesForBusGroup);
        }
        return null;
    }

    public static BusGroup[] createBusGroups(JSONObject busRaw, Schedule currentSchedule) {
        ArrayList<BusGroup> buses = new ArrayList<BusGroup>();
        //Buses {
        JSONObject busesRaw = busRaw.optJSONObject(BUSES_ID);
        if (busRaw == null) {
            return null;
        }
        //busses color names "Red", "Green"...
        JSONArray busColorNames = busesRaw.names();
        for (int i = 0; i<busColorNames.length(); i++) {
            String busColorName = busColorNames.optString(i);
            //Red...
            JSONObject busSet = busesRaw.optJSONObject(busColorName);
            BusGroup busGroup = createBusGroup(busSet, busColorName, currentSchedule);
            if (busGroup != null) {
                buses.add(busGroup);
            }
        }
        return buses.toArray(new BusGroup[0]);
    }
    public static Bus[] getAllBusesFromBusGroups(BusGroup[] busGroups) {
        ArrayList<Bus> buses = new ArrayList<Bus>();
        for (BusGroup busGroup : busGroups) {
            for (Bus bus : busGroup.getBuses()) {
                buses.add(bus);
            }
        }
        return buses.toArray(new Bus[0]);
    }
    private static BusGroup createBusGroup(JSONObject busGroupRaw, String busColorName, Schedule currentSchedule) {
        String busHexColor = busGroupRaw.optString(BUS_COLOR_ID);
        JSONObject routesRaw = busGroupRaw.optJSONObject(BUS_ROUTES_ID);
        if (routesRaw == null) {
            return null;
        }
        return new BusGroup(busColorName, busHexColor, Bus.createBuses(routesRaw, currentSchedule));
    }
    private static String[] createBusColorNames(Bus[] buses) {
        ArrayList<String> busColors = new ArrayList<String>();
        for (Bus b : buses) {
            if (!busColors.contains(b.getBusColorName())) {
                busColors.add(b.getBusColorName());
            }
        }
        return busColors.toArray(new String[0]);
    }
    private static int[] createBusColors(Bus[] buses) {
        ArrayList<Integer> busColors = new ArrayList<Integer>();
        for (Bus b : buses) {
            if (!busColors.contains(b.getBusColor())) {
                busColors.add(b.getBusColor());
            }
        }
        int[] busColorInts = new int[busColors.size()];
        Iterator<Integer> busColorIterator = busColors.iterator();
        int busColorIndex = 0;
        while (busColorIterator.hasNext()) {
            busColorInts[busColorIndex] = busColorIterator.next();
            busColorIndex++;
        }
        return busColorInts;
    }

    @Override
    public String getBusColorName() {
        return busColorName;
    }

    @Override
    public int getBusColor() {
        return busGroupColor;
    }
}
