package com.derricklockwood.isucyrideapp.main;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;

import com.derricklockwood.isucyrideapp.R;
import com.derricklockwood.isucyrideapp.busroutes.BusGroupListAdapter;
import com.derricklockwood.isucyrideapp.busroutes.views.BusFragment;
import com.derricklockwood.isucyrideapp.busroutes.views.BusMapFragment;
import com.derricklockwood.isucyrideapp.busroutes.views.BusRoutesMainFragment;
import com.derricklockwood.isucyrideapp.busroutes.views.MainFragment;
import com.derricklockwood.isucyrideapp.data.BusMenuItem;
import com.derricklockwood.isucyrideapp.data.CoreData;
import com.derricklockwood.isucyrideapp.data.models.Bus;
import com.derricklockwood.isucyrideapp.data.models.BusGroup;


public class MainActivity extends FragmentActivity implements CyrideFragmentCallBack {

    public static final String DEBUG_LOG_TAG = "Debugger- ";
    private CoreData coreData;
    private BusBackButtonHandler busBackButtonHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment_container);
        coreData = CoreData.createCoreData(getResources().openRawResource(R.raw.bus_tables), savedInstanceState,getSharedPreferences(CoreData.CORE_DATA_SAVE_FILE, MODE_PRIVATE));
        createMainView(savedInstanceState);
        busBackButtonHandler = new BusBackButtonHandler(this);
    }
    private void createMainView(Bundle savedInstanceState) {
        if (findViewById(R.id.main_fragment_container) != null && savedInstanceState == null) {
            MainFragment mainFragment = new MainFragment();

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(R.id.main_fragment_container, mainFragment);

            transaction.commit();
        }
    }

    public BusGroup[] getMyBusRoutes() {
        return coreData.getMyBusRoutes();
    }

    public void clearMyBusRoutes() {
        coreData.clearMyBusRoutes();
    }

    @Override
    public LayoutInflater getMainActivityLayoutInflater() {
        return getLayoutInflater();
    }

    @Override
    public BusGroup[] searchForBusGroups(String busSearch) {
        return coreData.searchForBusGroups(busSearch);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(CoreData.CORE_DATA_ID, coreData);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        coreData.saveData(getSharedPreferences(CoreData.CORE_DATA_SAVE_FILE, MODE_PRIVATE));
    }

    public void switchToMapView() {
        BusMapFragment mapFragment = new BusMapFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, mapFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            fragmentManager.popBackStack();
        }
    }

    public void backToPrevFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack();
    }

    public BusBackButtonHandler getBusBackButtonHandler() {
        return busBackButtonHandler;
    }

    @Override
    public void switchToBusRoutesView() {
        BusRoutesMainFragment fragment = new BusRoutesMainFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean switchToBusView(String busID) {
        BusFragment fragment = new BusFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BusFragment.BUS_POSITION_ID, busID);
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        return true;
    }

    public BusGroupListAdapter createBusRoutesListAdapter() {
        return new BusGroupListAdapter(BusGroup.createBusesSortedByColor(coreData.getBuses()), this);
    }
    public BusGroupListAdapter createMyBusRoutesListAdapter() {
        return new BusGroupListAdapter(coreData.getMyBusRoutes(), this);
    }
    public BusMenuHandler createBusMenuHandler() {
        BusMenuItem[] busMenuItems = {BusMenuItem.BUS_ROUTES, BusMenuItem.BUS_MAP, BusMenuItem.BUS_SETTINGS};
        return new BusMenuHandler(this, busMenuItems);
    }

    @Override
    public Bus getSelectedMapBus() {
        return coreData.getMyBusRoute(0).getBus(0);
    }

    @Override
    public Bus getBusByID(String busID) {
        return coreData.getBusByID(busID);
    }
}
