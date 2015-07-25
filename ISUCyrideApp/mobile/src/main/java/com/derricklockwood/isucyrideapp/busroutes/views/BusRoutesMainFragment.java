package com.derricklockwood.isucyrideapp.busroutes.views;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.derricklockwood.isucyrideapp.R;
import com.derricklockwood.isucyrideapp.busroutes.BusGroupListAdapter;
import com.derricklockwood.isucyrideapp.main.BusBackButtonHandler;
import com.derricklockwood.isucyrideapp.main.CyrideFragmentCallBack;
import com.derricklockwood.isucyrideapp.main.MainActivity;

/**
 * Created by Derrick Lockwood on 7/13/15.
 */
public class BusRoutesMainFragment extends Fragment implements ExpandableListView.OnChildClickListener, EditText.OnEditorActionListener{

    private static final String DEBUG_TAG_BUS_ROUTES_FRAGMENT = MainActivity.DEBUG_LOG_TAG+"Fragment- ";
    //public static final String BUS_ROUTES_FRAGMENT_KEY = "busRoutesMainFragmentKey";
    public ExpandableListView busListView;
    private Button backButton;
    private BusGroupListAdapter busGroupListAdapter;
    private CyrideFragmentCallBack cyrideFragmentCallBack;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.busroutes_view, container, false);
        backButton = (Button) v.findViewById(R.id.back_button);
        backButton.setOnClickListener(cyrideFragmentCallBack.getBusBackButtonHandler());
        busListView = (ExpandableListView) v.findViewById(R.id.bus_list);
        busListView.setAdapter(busGroupListAdapter);
        busListView.setOnChildClickListener(this);
        Log.d(DEBUG_TAG_BUS_ROUTES_FRAGMENT,"Created Bus Route Fragment");
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        cyrideFragmentCallBack = (CyrideFragmentCallBack) activity;
        busGroupListAdapter = cyrideFragmentCallBack.createBusRoutesListAdapter();
        Log.d(DEBUG_TAG_BUS_ROUTES_FRAGMENT, "Attached Bus Route Fragment");
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        return cyrideFragmentCallBack.switchToBusView(busGroupListAdapter.getBusIDFromPosition(groupPosition, childPosition));
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        String busSearch = v.getText().toString();
        busGroupListAdapter.setBusGroups(cyrideFragmentCallBack.searchForBusGroups(busSearch));
        busListView.invalidateViews();
        return false;
    }
}
