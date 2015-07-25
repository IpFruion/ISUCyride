package com.derricklockwood.isucyrideapp.busroutes.views.viewholders;

import android.view.View;
import android.widget.TextView;

import com.derricklockwood.isucyrideapp.R;
/**
 * Created by Derrick Lockwood on 7/16/15.
 */
public class BusStopTimeViewHolder {
    public TextView busStopTimeView;
    public BusStopTimeViewHolder(View convertView) {
        busStopTimeView = (TextView) convertView.findViewById(R.id.bus_stop_time_text_view);
    }
}
