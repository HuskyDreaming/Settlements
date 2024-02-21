package com.huskydreaming.settlements.inventories;

import com.huskydreaming.settlements.storage.enumerations.Menu;

public enum InventoryAction {
    DISBAND(Menu.DISBAND_TITLE.parse());

    private final String title;

    InventoryAction(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
