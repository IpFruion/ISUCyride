package com.derricklockwood.isucyrideapp.busroutes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.derricklockwood.isucyrideapp.R;
import com.derricklockwood.isucyrideapp.data.BusMenuItem;

/**
 * Created by Derrick Lockwood on 7/21/15.
 */
public class BusMenuListAdapter extends BaseExpandableListAdapter {

    private BusMenuItem[] busMenuItems;
    private LayoutInflater layoutInflater;
    public BusMenuListAdapter(BusMenuItem[] busMenuItems, LayoutInflater layoutInflater) {
        this.busMenuItems = busMenuItems;
        this.layoutInflater = layoutInflater;
    }
    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return busMenuItems.length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return busMenuItems[childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_menu, null);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView menuItemText;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_menu_item, null);
            menuItemText = (TextView) convertView.findViewById(R.id.menu_item_text_view);

            convertView.setTag(menuItemText);
        } else {
            menuItemText = (TextView) convertView.getTag();
        }
        menuItemText.setText(busMenuItems[childPosition].getMenuItemText());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
