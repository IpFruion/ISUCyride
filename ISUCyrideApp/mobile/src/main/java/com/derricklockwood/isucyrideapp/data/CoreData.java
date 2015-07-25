package com.derricklockwood.isucyrideapp.data;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.derricklockwood.isucyrideapp.R;
import com.derricklockwood.isucyrideapp.data.models.Bus;
import com.derricklockwood.isucyrideapp.data.models.BusGroup;
import com.derricklockwood.isucyrideapp.data.models.Schedule;
import com.derricklockwood.isucyrideapp.main.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Derrick Lockwood on 7/13/15.
 */
public class CoreData implements Serializable{

    public static final String CORE_DATA_ID = "core_data";
    public static final String CORE_DATA_SAVE_FILE = "core_data_save_file";

    private Schedule[] schedules;
    private BusGroup[] buses;
    private ArrayList<BusGroup> myBusRoutes;

    public CoreData(InputStream jsonInputStream) {
        JSONObject jsonRaw = getJsonRaw(jsonInputStream);
        schedules = Schedule.createSchedules(jsonRaw);
        buses = BusGroup.createBusGroups(jsonRaw, getCurrentSchedule());
        myBusRoutes = new ArrayList<BusGroup>();
    }

    public static CoreData createCoreData(InputStream jsonInputStream, Bundle savedInstanceState, SharedPreferences sharedPreferences) {
        CoreData coreData;
        if (savedInstanceState == null) {
            coreData = new CoreData(jsonInputStream);
            coreData.loadData(sharedPreferences);
        } else {
            coreData = (CoreData) savedInstanceState.getSerializable(CoreData.CORE_DATA_ID);
        }
        return coreData;
    }

    public void saveData(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(CORE_DATA_ID, getMyBusRoutesIDs());
        editor.commit();
    }
    public void loadData(SharedPreferences sharedPreferences) {
        if (myBusRoutes == null) {
            Log.e("loadData", "ERROR LOAD DATA MUST COME AFTER INSTANTIATE OF CORE DATA");
            return;
        }
        if (sharedPreferences == null) {
            return;
        }
        Set<String> set =  sharedPreferences.getStringSet(CORE_DATA_ID, null);
        if (set == null) {
            return;
        } else {
            for(String s : set) {
                for (BusGroup busGroup : buses) {
                    for (Bus bus : busGroup.getBuses()) {
                        if (bus.getBusID().equalsIgnoreCase(s)) {
                            bus.isInMyBusRoute = true;
                            addToMyRoutes(bus);
                        }
                    }
                }
            }
        }
    }
    private void addToMyRoutes(Bus bus) {
        addToBusGroup(myBusRoutes, bus);
    }
    private void addToMyRoutes(Bus[] buses) {
        for (Bus bus : buses) {
            addToBusGroup(myBusRoutes, bus);
        }
    }
    private void removeBusFromMyRoutes(Bus bus) {
        for (int i = 0; i<myBusRoutes.size(); i++) {
            BusGroup myBusGroup = myBusRoutes.get(i);
            if (myBusGroup.containsBus(bus)) {
                myBusGroup.removeBus(bus);
            }
            if (myBusGroup.getBusCount() == 0) {
                myBusRoutes.remove(myBusGroup);
            }
        }
    }

    public BusGroup[] searchForBusGroups(String busSearch) {
        ArrayList<BusGroup> busesSearched = new ArrayList<>();
        for (BusGroup busGroup : buses) {
            for (Bus bus : busGroup.getBuses()) {
                String busFullName = bus.getFullBusName().toLowerCase().replace(" ", "");
                if (busFullName.contains(busSearch)) {
                    addToBusGroup(busesSearched, bus);
                }
            }
        }
        return busesSearched.toArray(new BusGroup[0]);
    }
    private void addToBusGroup(ArrayList<BusGroup> busGroups, Bus bus) {
        if (!busGroupContains(busGroups, bus)) {
            for (BusGroup busGroup : busGroups) {
                if (!busGroup.containsBus(bus) && busGroup.getBusColorName().equalsIgnoreCase(bus.getBusColorName())) {
                    busGroup.addBus(bus);
                    return;
                }
            }
            Bus[] buses = { bus };
            busGroups.add(new BusGroup(bus.getBusColorName(), bus.getBusColor(), buses));
        }
    }
    private boolean busGroupContains(ArrayList<BusGroup> busGroups, Bus bus) {
        for (BusGroup busGroup : busGroups) {
            if (busGroup.containsBus(bus)) {
                return true;
            }
        }
        return false;
    }
    private boolean myBusRoutesContains(Bus bus) {
        return busGroupContains(myBusRoutes, bus);
    }

    public void reloadMyBusRouteList() {
        for (BusGroup busGroup : buses) {
            for (Bus bus : busGroup.getBuses()) {
                if (!myBusRoutesContains(bus)) {
                    if (bus.isInMyBusRoute) {
                        addToMyRoutes(bus);
                    }
                } else if (!bus.isInMyBusRoute) {
                    removeBusFromMyRoutes(bus);
                }
            }
        }
    }
    public void clearMyBusRoutes() {
        for (BusGroup busGroup : buses) {
            for (Bus bus : busGroup.getBuses()) {
                if (bus.isInMyBusRoute) {
                    bus.isInMyBusRoute = false;
                }
            }
        }
        myBusRoutes.clear();
    }

    //Getters
    public Bus getBusByID(String id) {
        for (BusGroup busGroup : buses) {
            for (Bus bus : busGroup.getBuses()) {
                if (bus.getBusID().equalsIgnoreCase(id)) {
                    return bus;
                }
            }
        }
        return null;
    }
    public BusGroup getMyBusRoute(int position) {
        if (position >= 0 && position < myBusRoutes.size()) {
            return myBusRoutes.get(position);
        }
        return null;
    }
    public BusGroup[] getMyBusRoutes() {
        reloadMyBusRouteList();
        return myBusRoutes.toArray(new BusGroup[0]);
    }
    public Bus[] getBuses() {
        ArrayList<Bus> allBuses = new ArrayList<>();
        for (BusGroup busGroup : buses) {
            for (Bus bus : busGroup.getBuses()) {
                allBuses.add(bus);
            }
        }
        return allBuses.toArray(new Bus[0]);
    }
    public Schedule[] getSchedules() {
        return schedules;
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
    private Set<String> getMyBusRoutesIDs() {
        reloadMyBusRouteList();
        Set<String> ids = new HashSet<String>();
        for (BusGroup busGroup : myBusRoutes) {
            for (Bus bus : busGroup.getBuses()) {
                ids.add(bus.getBusID());
            }
        }
        return ids;
    }

    //Print Methods
    private void printSchedules() {
        for (Schedule schedule : schedules) {
            Log.println(Log.ASSERT, "schedules", schedule.toString());
        }
    }
}
