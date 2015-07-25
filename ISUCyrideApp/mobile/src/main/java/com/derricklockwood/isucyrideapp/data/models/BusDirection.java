package com.derricklockwood.isucyrideapp.data.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Derrick Lockwood on 7/13/15.
 */
public class BusDirection implements Serializable {
    private static final String DIRECTION_NAME_ID = "name";
    private String directionID;
    private String directionName;

    public BusDirection(String directionID, String directionName) {
        this.directionID = directionID;
        this.directionName = directionName;
    }
    public String getDirectionID() {
        return directionID;
    }
    public String getDirectionName() {
        return directionName;
    }

    public static BusDirection createBusDirection(JSONObject directionRaw, String busDirectionID) {
        String busDirectionName = directionRaw.optString(DIRECTION_NAME_ID);
        return new BusDirection(busDirectionID, busDirectionName);
    }
}
