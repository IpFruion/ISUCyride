package com.derricklockwood.isucyrideapp.busroutes.views.viewholders;

import android.view.View;
import android.widget.TextView;

import com.derricklockwood.isucyrideapp.R;
/**
 * Created by Derrick Lockwood on 7/16/15.
 */
public class BusStopViewHolder {
    public TextView busStopView;
    public BusStopViewHolder(View convertView) {
        busStopView = (TextView) convertView.findViewById(R.id.bus_stop_text_view);
    }
}
