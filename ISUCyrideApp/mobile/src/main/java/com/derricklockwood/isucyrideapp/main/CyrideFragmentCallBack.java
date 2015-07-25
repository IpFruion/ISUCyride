package com.derricklockwood.isucyrideapp.main;

import android.view.LayoutInflater;

import com.derricklockwood.isucyrideapp.busroutes.BusGroupListAdapter;
import com.derricklockwood.isucyrideapp.data.models.Bus;
import com.derricklockwood.isucyrideapp.data.models.BusGroup;

/**
 * Created by Derrick Lockwood on 7/17/15.
 */
public interface CyrideFragmentCallBack {

    BusBackButtonHandler getBusBackButtonHandler();
    Bus getSelectedMapBus();
    void backToPrevFragment();
    void switchToBusRoutesView();
    void switchToMapView();
    BusMenuHandler createBusMenuHandler();
    boolean switchToBusView(String busID);
    BusGroupListAdapter createBusRoutesListAdapter();
    Bus getBusByID(String busID);
    BusGroupListAdapter createMyBusRoutesListAdapter();
    void clearMyBusRoutes();
    BusGroup[] getMyBusRoutes();
    LayoutInflater getMainActivityLayoutInflater();
    BusGroup[] searchForBusGroups(String busSearch);
}
