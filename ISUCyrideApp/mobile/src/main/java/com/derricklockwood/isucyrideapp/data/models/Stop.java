package com.derricklockwood.isucyrideapp.data.models;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
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
public class Stop implements Serializable{
    private static final String STOPS_ID = "stops";
    private static final String TIMES_ID = "times";
    private static final String STOP_LATITUDE_ID = "lat";
    private static final String STOP_LONGITUDE_ID = "lng";
    private static final String TWENTY_FOUR_HOUR_PATTERN = "k:mm";
    private static final String TWELVE_HOUR_PATTERN = "h:mm a";
    private static final String NEXT_STOP_TIME_PATTERN= "k:mm:ss";
    private static final String WEEKDAY = "Weekday";
    private static final String SATURDAY = "Saturday";
    private static final String SUNDAY = "Sunday";

    private Time[] stopTimes;
    private String stopName;
    private StopLocation stopLocation;
    private int currentTimes;
    private Time nextStopTime;
    private int nextStopTimeIndex;
    private DateFormat twelveHourFormatter;
    private DateFormat twentyFourHourFormatter;
    private DateFormat nextStopTimeFormatter;

    public Stop(String stopName, StopLocation stopLocation, String[] stopTimes, String currentTimes) {
        this.stopName = stopName;
        this.stopLocation = stopLocation;
        twelveHourFormatter = new SimpleDateFormat(TWELVE_HOUR_PATTERN, Locale.ENGLISH);
        nextStopTimeFormatter = new SimpleDateFormat(NEXT_STOP_TIME_PATTERN, Locale.ENGLISH);
        twentyFourHourFormatter = new SimpleDateFormat(TWENTY_FOUR_HOUR_PATTERN, Locale.ENGLISH);
        this.stopTimes = new Time[stopTimes.length];
        for (int i = 0; i<stopTimes.length; i++) {
            try {
                this.stopTimes[i] = new Time(twentyFourHourFormatter.parse(stopTimes[i]).getTime());
            } catch (ParseException e) {
                Log.e("Time Parse", e.toString());
            }
        }

    }
    public LatLng getStopLocation() {
        if (stopLocation == null) {
            return null;
        }
        return stopLocation.getLatLng();
    }

    public void updateNextStopTime() {
        nextStopTime = createNextStopTime();
    }

    public Time getNextStopTime() {
        return nextStopTime;
    }
    public int getNextStopTimeIndex() {
        return nextStopTimeIndex;
    }

    public Time createNextStopTime() {
        Date realDate = new Date();
        Time realTime = null;
        try {
            realTime = new Time(nextStopTimeFormatter.parse(nextStopTimeFormatter.format(realDate)).getTime());
        } catch (ParseException e) {
            Log.e("Time Parse", e.toString());
        }

        for (int i = 0; i<stopTimes.length; i++) {
            if (realTime.before(stopTimes[i])) {
                if (i == 0) {
                    nextStopTimeIndex = i;
                    return stopTimes[i];
                }
                if (realTime.after(stopTimes[i-1])) {
                    nextStopTimeIndex = i;
                    return stopTimes[i];
                }
            }
        }
        return null;
    }
    public boolean isNextStopTime(int timePosition) {
        if (timePosition < 0 || timePosition >= stopTimes.length) {
            return false;
        }
        if (stopTimes[timePosition] == getNextStopTime()) {
            return true;
        }
        return false;
    }

    public String getStopName() {
        return stopName;
    }
    public int getStopTimesCount() {
        return stopTimes.length;
    }
    public Time getStopTime(int position) {
        if (position >= 0 && position < stopTimes.length) {
            return stopTimes[position];
        }
        return null;
    }
    public String getStopTimeFormated(int position) {
        Time time = getStopTime(position);
        if (time == null) {
            return null;
        }
        return twelveHourFormatter.format(time);
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
    public static Stop[] createStops(JSONObject busDirectionRaw, Schedule currentSchedule) {
        ArrayList<Stop> stops = new ArrayList<Stop>();
        JSONObject stopsRaw = busDirectionRaw.optJSONObject(STOPS_ID);
        if (stopsRaw == null) {
            return null;
        }
        JSONArray stopsLocations = stopsRaw.names();
        if (stopsLocations == null) {
            return null;
        }
        for (int i = 0; i<stopsLocations.length(); i++) {
            String stopLocation = stopsLocations.optString(i);
            JSONObject stopRaw = stopsRaw.optJSONObject(stopLocation);
            Stop stop = createStop(stopRaw, stopLocation, currentSchedule);
            if (stop != null) {
                stops.add(stop);
            }
        }

        return stops.toArray(new Stop[0]);
    }
    public static Stop createStop(JSONObject stopRaw, String stopName, Schedule currentSchedule) {
        String currentTimeID = getCurrentTimeID();
        StopLocation stopLocation = null;
        double latitude = stopRaw.optDouble(STOP_LATITUDE_ID);
        double longitude = stopRaw.optDouble(STOP_LONGITUDE_ID);
        if (!Double.isNaN(latitude) && !Double.isNaN(longitude)){
            stopLocation = new StopLocation(latitude, longitude);
        }
        JSONArray stopTimesRaw = stopRaw.optJSONObject(TIMES_ID).optJSONObject(currentTimeID).optJSONArray(currentSchedule.getScheduleID());
        if (stopTimesRaw == null && stopTimesRaw.length() > 0) {
            return null;
        }
        ArrayList<String> stopTimes = new ArrayList<String>();
        for (int j = 0; j<stopTimesRaw.length(); j++) {
            String stopTime = stopTimesRaw.optString(j);
            if (stopTime != null) {
                stopTimes.add(stopTime);
            }
        }
        return new Stop(stopName, stopLocation, stopTimes.toArray(new String[0]), currentTimeID);
    }
}
