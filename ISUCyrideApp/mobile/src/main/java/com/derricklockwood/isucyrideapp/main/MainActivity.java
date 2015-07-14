package com.derricklockwood.isucyrideapp.main;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.derricklockwood.isucyrideapp.R;
import com.derricklockwood.isucyrideapp.busroutes.BusRoutesActivity;
import com.derricklockwood.isucyrideapp.data.CoreData;


public class MainActivity extends FragmentActivity {

    private CoreData coreData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment_container);
        createView(savedInstanceState);
        coreData = new CoreData(getResources().openRawResource(R.raw.bus_tables));
    }
    private void createView(Bundle savedInstanceState) {
        if (findViewById(R.id.main_fragment_container) != null && savedInstanceState == null) {
            MainFragment mainFragment = new MainFragment();

            mainFragment.setArguments(getIntent().getExtras());

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.main_fragment_container, mainFragment);

            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_busRoutes:
                switchToBusRoutes();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    private void switchToBusRoutes() {
        Intent busSwitch = new Intent(this, BusRoutesActivity.class);
        startActivity(busSwitch);
    }
}
