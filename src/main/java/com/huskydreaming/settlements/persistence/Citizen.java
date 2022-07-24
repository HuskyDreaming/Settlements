package com.huskydreaming.settlements.persistence;

public class Citizen {

    private String settlement;
    private String role;

    public static Citizen create(Settlement settlement, String role) {
        return new Citizen(settlement.getName(), role);
    }

    public Citizen(String settlement, String role) {
        this.settlement = settlement;
        this.role = role;
    }

    public String getSettlement() {
        return settlement;
    }

    public void setSettlement(String settlement) {
        this.settlement = settlement;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
