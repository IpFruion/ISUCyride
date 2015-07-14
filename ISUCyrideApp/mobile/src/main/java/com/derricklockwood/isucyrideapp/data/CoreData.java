package com.derricklockwood.isucyrideapp.data;

import android.util.Log;

import com.derricklockwood.isucyrideapp.R;
import com.derricklockwood.isucyrideapp.data.models.Bus;
import com.derricklockwood.isucyrideapp.data.models.Schedule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Derrick Lockwood on 7/13/15.
 */
public class CoreData {
    Schedule[] schedules;
    Bus[] buses;

    public CoreData(InputStream jsonInputStream) {
        JSONObject jsonRaw = getJsonRaw(jsonInputStream);
        schedules = Schedule.createSchedules(jsonRaw);
        buses = Bus.createBuses(jsonRaw, getCurrentSchedule());
    }

    private Schedule getCurrentSchedule() {
        Schedule currentSchedule = null;
        for (Schedule schedule : schedules) {
            if (schedule.isCurrentSchedule()) {
                if (currentSchedule != null) {
                    if (currentSchedule.timeBetweenStartAndEnd() > schedule.timeBetweenStartAndEnd()) {
                        currentSchedule = schedule;
                    }
                } else {
                    currentSchedule = schedule;
                }
            }
        }
        return currentSchedule;
    }
    private JSONObject getJsonRaw(InputStream jsonInputStream) {
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(jsonInputStream, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                jsonInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonString = writer.toString();
        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
