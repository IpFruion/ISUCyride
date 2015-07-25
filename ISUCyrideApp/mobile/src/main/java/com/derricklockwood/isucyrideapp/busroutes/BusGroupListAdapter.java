package com.derricklockwood.isucyrideapp.busroutes;

import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.derricklockwood.isucyrideapp.R;
import com.derricklockwood.isucyrideapp.busroutes.views.viewholders.BusGroupViewHolder;
import com.derricklockwood.isucyrideapp.busroutes.views.viewholders.BusViewHolder;
import com.derricklockwood.isucyrideapp.data.models.Bus;
import com.derricklockwood.isucyrideapp.data.models.BusGroup;
import com.derricklockwood.isucyrideapp.main.CyrideFragmentCallBack;
import com.derricklockwood.isucyrideapp.main.MainActivity;

/**
 * Created by Derrick Lockwood on 7/13/15.
 */
public class BusGroupListAdapter extends BaseExpandableListAdapter {

    private static final String DEBUG_TAG_BUS_LIST_ADAPTER = MainActivity.DEBUG_LOG_TAG+"Adapter- ";
    private static final double RED_LUMINANCE_SCALAR = 0.2126;
    private static final double BLUE_LUMINANCE_SCALAR = 0.0722;
    private static final double GREEN_LUMINANCE_SCALAR = 0.7152;
    private BusGroup[] busGroups;
    private CyrideFragmentCallBack cyrideFragmentCallBack;

    public BusGroupListAdapter(BusGroup[] busGroups, CyrideFragmentCallBack cyrideFragmentCallBack) {
        this.busGroups = busGroups;
        this.cyrideFragmentCallBack = cyrideFragmentCallBack;
    }

    private boolean isMoreBlackLuminance(int color) {
        int red = Color.red(color);
        int blue = Color.blue(color);
        int green = Color.green(color);
        double luminance = red * RED_LUMINANCE_SCALAR + blue * BLUE_LUMINANCE_SCALAR + green * GREEN_LUMINANCE_SCALAR;
        if ((int)luminance >= 128) {
            return true;
        }
        return false;
    }
    public void setBusGroups(BusGroup[] busGroups) {
        this.busGroups = busGroups;
        notifyDataSetChanged();
    }
    public int getBusGroupsCount() {
        return busGroups.length;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return busGroups.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        BusGroup busGroup = busGroups[groupPosition];
        return busGroup.getBusCount();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return busGroups[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        BusGroup busGroup = busGroups[groupPosition];
        //Log.d(DEBUG_TAG_BUS_LIST_ADAPTER, "Get Child At: ("+groupPosition+","+childPosition+") Bus: "+busGroup.getBus(childPosition));
        return busGroup.getBus(childPosition);
    }

    public String getBusIDFromPosition(int groupPosition, int childPosition) {
        return busGroups[groupPosition].getBus(childPosition).getBusID();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
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
        BusGroup busGroup = busGroups[groupPosition];
        if (busGroup == null) {
            return null;
        }
        BusGroupViewHolder viewHolder;
        LayoutInflater layoutInflater = cyrideFragmentCallBack.getMainActivityLayoutInflater();
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_bus_group, null);
            viewHolder = new BusGroupViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder= (BusGroupViewHolder) convertView.getTag();
        }
        viewHolder.busLineBox.setBackgroundColor(busGroup.getBusGroupColor());
        viewHolder.colorNameTextView.setText(busGroup.getBusGroupName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        BusGroup busGroup = busGroups[groupPosition];
        Bus bus = busGroup.getBus(childPosition);
        if (busGroup == null) {
            return null;
        }
        BusViewHolder viewHolder;
        LayoutInflater layoutInflater = cyrideFragmentCallBack.getMainActivityLayoutInflater();
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_bus, null);
            viewHolder = new BusViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else  {
            viewHolder = (BusViewHolder) convertView.getTag();
        }

        convertView.setBackgroundColor(busGroup.getBusGroupColor());
        viewHolder.busIDView.setText("#"+bus.getBusIDName()+" Bus");
        viewHolder.busDirectionView.setText(bus.getBusDirection().getDirectionName());
        if (isMoreBlackLuminance(busGroup.getBusGroupColor())) {
            viewHolder.busIDView.setTextColor(Color.BLACK);
            viewHolder.busDirectionView.setTextColor(Color.BLACK);
        } else {
            viewHolder.busIDView.setTextColor(Color.WHITE);
            viewHolder.busDirectionView.setTextColor(Color.WHITE);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
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
