package com.derricklockwood.isucyrideapp.busroutes.views.viewholders;

import android.view.View;
import android.widget.TextView;

import com.derricklockwood.isucyrideapp.R;
/**
 * Created by Derrick Lockwood on 7/15/15.
 */
public class BusViewHolder {
    public TextView busIDView;
    public TextView busDirectionView;
    public BusViewHolder(View convertView) {
        busIDView = (TextView) convertView.findViewById(R.id.bus_ID_text_view);
        busDirectionView = (TextView) convertView.findViewById(R.id.bus_direction_text_view);
    }
}
