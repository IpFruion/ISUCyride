package com.derricklockwood.isucyrideapp.data.models;

import android.graphics.Color;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Derrick Lockwood on 7/13/15.
 */
public class Bus {
    private static final String BUSES_ID = "buses";
    private static final String BUS_COLOR_ID = "color";


    private String busID;
    private String busColorName;
    private int busColor;
    private BusDirection busDirection;
    private Stop[] stops;

    public Bus (String busColorName, String busID, String busColorHex, Stop[] stops, BusDirection busDirection) {
        this.busID = busID;
        this.busColorName = busColorName;
        this.stops = stops;
        this.busDirection = busDirection;
        busColor = Color.parseColor("#" + busColorHex);
    }

    public String getFullBusName() {
        return busColorName + " " + busID + " " + busDirection.getDirectionName();
    }

    public static Bus[] createBuses(JSONObject busRaw, Schedule currentSchedule) {
        ArrayList<Bus> buses = new ArrayList<Bus>();
        //Buses {
        JSONObject busesRaw = busRaw.optJSONObject(BUSES_ID);
        //busses color names "Red", "Green"...
        JSONArray busColorNames = busesRaw.names();
        for (int i = 0; i<busColorNames.length(); i++) {
            String busColorName = busColorNames.optString(i);
            //Red...
            JSONObject busSet = busesRaw.optJSONObject(busColorName);
            String busHexColor = busSet.optString(BUS_COLOR_ID);
            //1...1A...
            JSONArray busIDNames = getBusIDNames(busSet.names());
            for (int j = 0; j<busIDNames.length(); j++) {
                String busID = busIDNames.optString(j);
                JSONObject finalBusRaw = busSet.optJSONObject(busID);
                BusDirection[] busDirections = BusDirection.createBusDirections(finalBusRaw);
                for (int k = 0; k<busDirections.length; k++) {
                    Stop[] stops = Stop.createStops(finalBusRaw, currentSchedule, busDirections[k]);
                    buses.add(new Bus(busColorName, busID, busHexColor, stops, busDirections[k]));
                }
            }
        }
        return buses.toArray(new Bus[0]);
    }

    private static JSONArray getBusIDNames(JSONArray busSetNames) {
        JSONArray array = new JSONArray();
        for (int i = 0; i<busSetNames.length(); i++) {
            String name = busSetNames.optString(i);
            if (Character.isDigit(name.charAt(0))) {
                array.put(name);
            }
        }
        return array;
    }

    @Override
    public String toString() {
        return getFullBusName() + "\n";
    }
}
