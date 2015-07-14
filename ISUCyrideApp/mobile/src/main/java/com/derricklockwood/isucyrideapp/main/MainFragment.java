package com.derricklockwood.isucyrideapp.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.derricklockwood.isucyrideapp.R;

/**
 * Created by Derrick Lockwood on 7/11/15.
 */
public class MainFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.activity_main, container, false);

        return mainView;
    }
}
