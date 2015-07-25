package com.derricklockwood.isucyrideapp.main;

import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import com.derricklockwood.isucyrideapp.busroutes.BusMenuListAdapter;
import com.derricklockwood.isucyrideapp.data.BusMenuItem;

/**
 * Created by Derrick Lockwood on 7/21/15.
 */
public class BusMenuHandler implements ExpandableListView.OnChildClickListener {

    private CyrideFragmentCallBack cyrideFragmentCallBack;
    private BusMenuListAdapter busMenuListAdapter;
    private BusMenuItem[] busMenuItems;

    public BusMenuHandler(CyrideFragmentCallBack cyrideFragmentCallBack, BusMenuItem[] busMenuItems) {
        this.cyrideFragmentCallBack = cyrideFragmentCallBack;
        this.busMenuItems = busMenuItems;
        busMenuListAdapter = new BusMenuListAdapter(busMenuItems, cyrideFragmentCallBack.getMainActivityLayoutInflater());
    }
    public BusMenuListAdapter getBusMenuListAdapter() {
        return busMenuListAdapter;
    }
    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        BusMenuItem busMenuItem = busMenuItems[childPosition];
        switch (busMenuItem) {
            case BUS_ROUTES:
                cyrideFragmentCallBack.switchToBusRoutesView();
                break;
            case BUS_MAP:
                cyrideFragmentCallBack.switchToMapView();
                break;
            case BUS_SETTINGS:
                break;
            default:
                Log.e("Bus Menu Item", "No Bus Item Action for bus Item: "+ busMenuItem.getMenuItemText());
        }
        return true;
    }
}
