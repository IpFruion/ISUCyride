package com.derricklockwood.isucyrideapp.data;

/**
 * Created by Derrick Lockwood on 7/21/15.
 */
public enum BusMenuItem {
    BUS_ROUTES("Bus Routes"), BUS_MAP("Bus Map"), BUS_SETTINGS("Settings");

    private String menuItemText;
    BusMenuItem(String menuItemText) {
        this.menuItemText = menuItemText;
    }
    public String getMenuItemText() {
        return menuItemText;
    }
}
