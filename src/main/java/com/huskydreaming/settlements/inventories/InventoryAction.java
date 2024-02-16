package com.huskydreaming.settlements.inventories;

public enum InventoryAction {
    DISBAND("Disband Settlement?");

    private final String title;

    InventoryAction(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
