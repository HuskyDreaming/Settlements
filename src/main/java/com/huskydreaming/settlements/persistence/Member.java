package com.huskydreaming.settlements.persistence;

public class Member {

    private String settlement;
    private String role;
    private String lastOnline;

    private boolean autoClaim;

    public static Member create(String name, String role) {
        return new Member(name, role);
    }

    public Member(String settlement, String role) {
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

    public String getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(String lastOnline) {
        this.lastOnline = lastOnline;
    }

    public void setAutoClaim(boolean autoClaim) {
        this.autoClaim = autoClaim;
    }

    public boolean hasAutoClaim() {
        return autoClaim;
    }
}
