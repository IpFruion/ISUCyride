package com.derricklockwood.isucyrideapp.busroutes;

import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;

import com.derricklockwood.isucyrideapp.R;
import com.derricklockwood.isucyrideapp.busroutes.views.viewholders.BusGroupViewHolder;
import com.derricklockwood.isucyrideapp.busroutes.views.viewholders.BusStopTimeViewHolder;
import com.derricklockwood.isucyrideapp.busroutes.views.viewholders.BusStopViewHolder;
import com.derricklockwood.isucyrideapp.data.models.Bus;
import com.derricklockwood.isucyrideapp.data.models.Stop;

import java.sql.Time;
import java.util.Date;

/**
 * Created by Derrick Lockwood on 7/16/15.
 */
public class BusStopListAdapter implements ExpandableListAdapter {

    private Bus bus;
    private LayoutInflater layoutInflater;

    public BusStopListAdapter(Bus bus, LayoutInflater layoutInflater) {
        this.bus = bus;
        this.layoutInflater = layoutInflater;
    }
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return bus.getStopCount();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return bus.getStop(groupPosition).getStopTimesCount();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return bus.getStop(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return bus.getStop(groupPosition).getStopTime(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Stop stop = bus.getStop(groupPosition);
        if (stop == null) {
            return null;
        }
        BusStopViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_stop, null);
            viewHolder = new BusStopViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder= (BusStopViewHolder) convertView.getTag();
        }
        viewHolder.busStopView.setText(stop.getStopName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Stop stop = bus.getStop(groupPosition);
        String time = stop.getStopTimeFormated(childPosition);
        if (time == null) {
            return null;
        }
        BusStopTimeViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_stop_time, null);
            viewHolder = new BusStopTimeViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder= (BusStopTimeViewHolder) convertView.getTag();
        }
        viewHolder.busStopTimeView.setText(time);
        if (stop.getNextStopTimeIndex() == childPosition) {
            convertView.setBackgroundColor(Color.RED);
        } else {
            convertView.setBackgroundColor(Color.WHITE);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }
}
