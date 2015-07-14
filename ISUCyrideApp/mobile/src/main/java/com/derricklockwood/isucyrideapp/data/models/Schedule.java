package com.derricklockwood.isucyrideapp.data.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Derrick Lockwood on 7/13/15.
 */
public class Schedule {
    private static final String SCHEDULES_ID = "schedules";
    private static final String DATE_FORMAT_PATTERN = "MM/d/yyyy";
    private static final String NULL_DATE = "N/A";
    private static final String START_DATE_ID = "start_date";
    private static final String END_DATE_ID = "end_date";
    private static final String NAME_ID = "name";

    private DateFormat dateFormatter;
    private Date startDate;
    private Date endDate;
    private String name;
    private String scheduleID;

    public Schedule(String scheduleID, String name, String startDate, String endDate) {
        dateFormatter = new SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.ENGLISH);
        this.startDate = getDateFromString(startDate);
        this.endDate = getDateFromString(endDate);
        this.name = name;
        this.scheduleID = scheduleID;

    }

    private Date getDateFromString(String dateString) {
        try {
            return dateFormatter.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    public String getName() {
        return name;
    }
    public String getScheduleID() {
        return scheduleID;
    }
    public Date getStartDate() {
        return startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public String getStartDateFormated() {
        if (startDate == null) {
            return NULL_DATE;
        }
        return dateFormatter.format(startDate);
    }
    public String getEndDateFormated() {
        if (endDate == null) {
            return NULL_DATE;
        }
        return dateFormatter.format(endDate);
    }

    public boolean isCurrentSchedule() {
        return doesDateFallInSchedule(new Date());
    }
    public boolean doesDateFallInSchedule(Date date) {
        if (date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0) {
            return true;
        }
        return false;
    }

    public long timeBetweenStartAndEnd() {
        return endDate.getTime()-startDate.getTime();
    }

    @Override
    public String toString() {
        String out = "";
        out += name + "\n";
        out += "\t" + getStartDateFormated() + "\n";
        out += "\t" + getEndDateFormated() + "\n";
        return out;
    }

    public static Schedule[] createSchedules(JSONObject jsonRaw) {
        JSONObject schedulesRaw = jsonRaw.optJSONObject(SCHEDULES_ID);
        JSONArray names = schedulesRaw.names();
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
        for (int i = 0; i<names.length(); i++) {
            String scheduleID = names.optString(i);
            Schedule schedule = createSchedule(schedulesRaw.optJSONObject(scheduleID), scheduleID);
            if (schedule != null) {
                schedules.add(schedule);
            }
        }
        return schedules.toArray(new Schedule[0]);
    }
    private static Schedule createSchedule(JSONObject scheduleRaw, String scheduleID) {
        String startDate = scheduleRaw.optString(START_DATE_ID);
        String endDate = scheduleRaw.optString(END_DATE_ID);
        if (startDate != null || endDate != null) {
            if ((startDate.length() == 0 || endDate.length() == 0)) {
                return null;
            }
            Schedule schedule =  new Schedule(scheduleID, scheduleRaw.optString(NAME_ID), startDate, endDate);
            if (schedule.timeBetweenStartAndEnd() <= 0) {
                return null;
            }
            return schedule;
        } else {
            return null;
        }
    }
    private class InvalidScheduleException extends Exception {

    }
}
