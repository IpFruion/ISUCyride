package com.derricklockwood.isucyrideapp.busroutes;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.ExpandableListView;

import com.derricklockwood.isucyrideapp.busroutes.views.BusRoutesMainFragment;
import com.derricklockwood.isucyrideapp.R;

/**
 * Created by Derrick Lockwood on 7/13/15.
 */
public class BusRoutesActivity extends FragmentActivity {

    private BusRoutesMainFragment busRoutesMainFragment;
    private ExpandableListView busListView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment_container);
        createView(savedInstanceState);

        busListView = (ExpandableListView) findViewById(R.id.bus_list);
    }
    private void createView(Bundle savedInstanceState) {
        if (findViewById(R.id.main_fragment_container) != null && savedInstanceState == null) {
            busRoutesMainFragment = new BusRoutesMainFragment();

            busRoutesMainFragment.setArguments(getIntent().getExtras());

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.main_fragment_container, busRoutesMainFragment);
            transaction.commit();
        }
    }
}
