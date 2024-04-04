package com.huskydreaming.settlements.inventories.base;

import com.huskydreaming.settlements.storage.types.Menu;

public enum InventoryActionType {
    DISBAND(Menu.DISBAND_TITLE.parse()),
    UN_TRUST(Menu.UN_TRUST_TITLE.parse());

    private final String title;

    InventoryActionType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}