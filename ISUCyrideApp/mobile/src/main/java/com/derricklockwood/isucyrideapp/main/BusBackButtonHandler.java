package com.derricklockwood.isucyrideapp.main;

import android.view.View;
import android.widget.Button;

/**
 * Created by Derrick Lockwood on 7/21/15.
 */
public class BusBackButtonHandler implements Button.OnClickListener{

    private CyrideFragmentCallBack cyrideFragmentCallBack;
    public BusBackButtonHandler(CyrideFragmentCallBack cyrideFragmentCallBack) {
        this.cyrideFragmentCallBack = cyrideFragmentCallBack;
    }

    @Override
    public void onClick(View v) {
        cyrideFragmentCallBack.backToPrevFragment();
    }
}
