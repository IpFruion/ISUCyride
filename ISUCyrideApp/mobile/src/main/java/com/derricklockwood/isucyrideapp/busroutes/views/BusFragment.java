package com.derricklockwood.isucyrideapp.busroutes.views;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.derricklockwood.isucyrideapp.R;
import com.derricklockwood.isucyrideapp.busroutes.BusStopListAdapter;
import com.derricklockwood.isucyrideapp.data.models.Bus;
import com.derricklockwood.isucyrideapp.main.CyrideFragmentCallBack;
import com.derricklockwood.isucyrideapp.main.MainActivity;

/**
 * Created by Derrick Lockwood on 7/15/15.
 */
public class BusFragment extends Fragment implements ToggleButton.OnCheckedChangeListener {

    public static final String BUS_POSITION_ID = "busID";
    public TextView busFullNameTextView;
    public ExpandableListView busTimesListView;
    public ToggleButton makeMyRouteButton;
    private Button backButton;
    private CyrideFragmentCallBack cyrideFragmentCallBack;
    private Bus loadedBus;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.bus_view, container, false);
        backButton = (Button) v.findViewById(R.id.back_button);
        backButton.setOnClickListener(cyrideFragmentCallBack.getBusBackButtonHandler());
        busFullNameTextView = (TextView) v.findViewById(R.id.bus_full_name);
        busTimesListView = (ExpandableListView) v.findViewById(R.id.bus_time_list);
        makeMyRouteButton = (ToggleButton) v.findViewById(R.id.my_bus_route_toggle);
        Bundle bundle = getArguments();
        loadedBus = cyrideFragmentCallBack.getBusByID(bundle.getString(BUS_POSITION_ID));
        fillInViewsWithBus(loadedBus, inflater);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        cyrideFragmentCallBack = (CyrideFragmentCallBack) activity;
    }
    private void fillInViewsWithBus(Bus bus, LayoutInflater inflater) {
        BusStopListAdapter stopListAdapter = new BusStopListAdapter(bus, inflater);
        busFullNameTextView.setText(bus.getFullBusName());
        busTimesListView.setAdapter(stopListAdapter);
        makeMyRouteButton.setOnCheckedChangeListener(this);
        if (loadedBus.isInMyBusRoute) {
            makeMyRouteButton.setChecked(true);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        cyrideFragmentCallBack = null;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d(MainActivity.DEBUG_LOG_TAG, "Loaded bus Status In My Routes: " + loadedBus.isInMyBusRoute);
        loadedBus.isInMyBusRoute = isChecked;
    }
}
