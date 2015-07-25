package com.derricklockwood.isucyrideapp.busroutes.views;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.derricklockwood.isucyrideapp.R;
import com.derricklockwood.isucyrideapp.busroutes.BusGroupListAdapter;
import com.derricklockwood.isucyrideapp.busroutes.BusMenuListAdapter;
import com.derricklockwood.isucyrideapp.main.BusMenuHandler;
import com.derricklockwood.isucyrideapp.main.CyrideFragmentCallBack;

/**
 * Created by Derrick Lockwood on 7/11/15.
 */
public class MainFragment extends Fragment implements Button.OnClickListener, ExpandableListView.OnChildClickListener{

    private CyrideFragmentCallBack cyrideFragmentCallBack;
    private ExpandableListView myRoutesListView;
    private ExpandableListView busMenu;
    private BusGroupListAdapter myRoutesListAdapter;
    private Button clearMyRoutesButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.main_view, container, false);

        setupBusMenu(mainView);

        myRoutesListView = (ExpandableListView) mainView.findViewById(R.id.my_bus_routes);
        myRoutesListAdapter = cyrideFragmentCallBack.createMyBusRoutesListAdapter();
        myRoutesListView.setAdapter(myRoutesListAdapter);
        myRoutesListView.setOnChildClickListener(this);
        clearMyRoutesButton = (Button) mainView.findViewById(R.id.clear_my_routes_button);
        clearMyRoutesButton.setOnClickListener(this);
        return mainView;
    }
    private void setupBusMenu(View mainView) {
        busMenu = (ExpandableListView) mainView.findViewById(R.id.custom_main_menu);
        BusMenuHandler busMenuHandler = cyrideFragmentCallBack.createBusMenuHandler();
        busMenu.setAdapter(busMenuHandler.getBusMenuListAdapter());
        busMenu.setOnChildClickListener(busMenuHandler);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        cyrideFragmentCallBack = (CyrideFragmentCallBack) activity;
    }

    @Override
    public void onPause() {
        super.onPause();
        busMenu.collapseGroup(0);
    }

    @Override
    public void onClick(View v) {
        collapseAllViews();
        cyrideFragmentCallBack.clearMyBusRoutes();
        myRoutesListAdapter.setBusGroups(cyrideFragmentCallBack.getMyBusRoutes());
        myRoutesListView.invalidateViews();
    }

    private void collapseAllViews() {
        for (int i = 0; i<myRoutesListAdapter.getBusGroupsCount(); i++) {
            myRoutesListView.collapseGroup(i);
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        BusGroupListAdapter adapter = (BusGroupListAdapter) myRoutesListView.getExpandableListAdapter();
        return cyrideFragmentCallBack.switchToBusView(adapter.getBusIDFromPosition(groupPosition, childPosition));
    }
}
