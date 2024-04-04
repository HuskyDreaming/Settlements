package com.huskydreaming.settlements.enumeration;

public enum Placeholder {
    NAME,
    TAG,
    OWNER,
    CLAIMS_COUNT,
    ROLES_COUNT,
    MEMBERS_COUNT,
    CLAIMS_MAX,
    ROLES_MAX,
    MEMBERS_MAX,
    MEMBERS_TOP,
    CLAIMS_TOP;

    public boolean isPlaceholder(String string) {
        return string.equalsIgnoreCase(name());
    }

    public boolean containsPlaceholder(String string) {
        return string.contains(name().toLowerCase());
    }
}