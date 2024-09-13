package com.huskydreaming.settlements.enumeration.types;

public enum SettlementDefaultType {
    MAX_CLAIMS(15),
    MAX_HOMES(3),
    MAX_MEMBERS(10),
    MAX_ROLES(5),
    MAX_NAME_LENGTH(12),
    MIN_NAME_LENGTH(4),
    MAX_DESCRIPTION_LENGTH(36),
    MIN_DESCRIPTION_LENGTH(8),
    MAX_TAG_LENGTH(4),
    MIN_TAG_LENGTH(2);

    private final int value;

    SettlementDefaultType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name().toLowerCase().replace("_", "-");
    }
}