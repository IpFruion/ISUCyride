package com.derricklockwood.isucyrideapp.busroutes.views.viewholders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.derricklockwood.isucyrideapp.R;

/**
 * Created by Derrick Lockwood on 7/14/15.
 */
public class BusGroupViewHolder {
    public TextView colorNameTextView;
    public LinearLayout busLineBox;
    public BusGroupViewHolder(View convertView) {
        if (convertView != null) {
            busLineBox = (LinearLayout) convertView.findViewById(R.id.bus_line_color);
            colorNameTextView = (TextView) convertView.findViewById(R.id.bus_color_name_text_view);
        }
    }
}
