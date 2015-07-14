package com.derricklockwood.isucyrideapp.data.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Derrick Lockwood on 7/13/15.
 */
public class BusDirection {
    private static final String BUS_DIRECTIONS_ID = "directions";
    private static final String DIRECTION_NAME_ID = "name";
    private String directionID;
    private String directionName;

    public BusDirection(String directionName, String directionID) {
        this.directionID = directionID;
        this.directionName = directionName;
    }
    public String getDirectionID() {
        return directionID;
    }
    public String getDirectionName() {
        return directionName;
    }

    public static BusDirection[] createBusDirections(JSONObject finalBusRaw) {
        JSONObject directionsRaw = finalBusRaw.optJSONObject(BUS_DIRECTIONS_ID);
        JSONArray directionsNames = directionsRaw.names();
        ArrayList<BusDirection> busDirections = new ArrayList<BusDirection>();
        for (int i = 0; i<directionsNames.length(); i++) {
            String busDirectionID = directionsNames.optString(i);
            JSONObject busDirection = directionsRaw.optJSONObject(busDirectionID);
            busDirections.add(new BusDirection(busDirection.optString(DIRECTION_NAME_ID), busDirectionID));
        }
        return busDirections.toArray(new BusDirection[0]);
    }
}
