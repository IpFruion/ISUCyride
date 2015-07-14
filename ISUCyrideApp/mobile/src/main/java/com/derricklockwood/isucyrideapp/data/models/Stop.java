package com.derricklockwood.isucyrideapp.data.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Derrick Lockwood on 7/13/15.
 */
public class Stop {
    private static final String STOPS_ID = "stops";
    private static final String TIME_PATTERN = "k:m";
    private static final String WEEKDAY = "Weekday";
    private static final String SATURDAY = "Saturday";
    private static final String SUNDAY = "Sunday";

    private Time[] stopTimes;
    private String stopLocation;
    private int currentTimes;

    public Stop(String stopLocation, String[] stopTimes, String currentTimes) {
        this.stopLocation = stopLocation;
        DateFormat format = new SimpleDateFormat(TIME_PATTERN, Locale.ENGLISH);
        this.stopTimes = new Time[stopTimes.length];
        for (int i = 0; i<stopTimes.length; i++) {
            try {
                this.stopTimes[i] = new Time(format.parse(stopTimes[i]).getTime());
            } catch (ParseException e) {
                Log.e("Time Parse", e.toString());
            }
        }
    }

    private static String getCurrentTimeID() {
        DateFormat formatter = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        Date today = new Date();
        switch (formatter.format(today)) {
            case "Monday":
            case "Tuesday":
            case "Wednesday":
            case "Thursday":
            case "Friday":
                return WEEKDAY;
            case "Saturday":
                return SATURDAY;
            case "Sunday":
                return SUNDAY;
        }
        return null;
    }
    public static Stop[] createStops(JSONObject finalBusRaw, Schedule currentSchedule, BusDirection busDirection) {
        ArrayList<Stop> stops = new ArrayList<Stop>();
        JSONObject stopsRaw = finalBusRaw.optJSONObject(STOPS_ID);
        JSONArray stopsLocations = stopsRaw.names();
        for (int i = 0; i<stopsLocations.length(); i++) {
            String stopLocation = stopsLocations.optString(i);
            JSONObject stopRaw = stopsRaw.optJSONObject(stopLocation);
            stops.add(createStop(stopRaw, stopLocation, currentSchedule, busDirection));
        }

        return stops.toArray(new Stop[0]);
    }
    public static Stop createStop(JSONObject stopRaw, String stopLocation, Schedule currentSchedule, BusDirection busDirection) {
        String currentTimeID = getCurrentTimeID();
        JSONArray stopTimesRaw = stopRaw.optJSONObject(busDirection.getDirectionID()).optJSONObject(currentTimeID).optJSONArray(currentSchedule.getScheduleID());
        ArrayList<String> stopTimes = new ArrayList<String>();
        for (int j = 0; j<stopTimesRaw.length(); j++) {
            String stopTime = stopTimesRaw.optString(j);
            if (stopTime != null) {
                stopTimes.add(stopTime);
            }
        }
        return new Stop(stopLocation, stopTimes.toArray(new String[0]), currentTimeID);
    }
}
