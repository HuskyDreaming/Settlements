package com.huskydreaming.settlements.persistence;

import com.huskydreaming.settlements.persistence.roles.Role;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Request {

    public enum Type { ROLE_CREATE, ROLE_RENAME }
    public enum Response { CANCEL, OK, ERROR }

    private String response;
    private Type type;

    public static Request create(Type type) {
        return new Request(type);
    }

    public Request(Type type) {
        this.type = type;
    }

    public void send(Player player) {
        switch (type) {
            case ROLE_CREATE -> player.sendMessage(ChatColor.GREEN + "Type a name for the role you want to create:");
            case ROLE_RENAME -> player.sendMessage(ChatColor.GREEN + "Type a name for the role you want to rename:");
        }
    }

    public Response response(Player player, Settlement settlement, String response) {
        if(response.equalsIgnoreCase("cancel")) {
            player.sendMessage(ChatColor.GRAY + "You have cancelled the request.");
            return Response.CANCEL;
        }

        this.response = response;
        switch (type) {
            case ROLE_CREATE:
            case ROLE_RENAME:
                if (settlement.hasRole(response)) {
                    player.sendMessage(ChatColor.GRAY + "A role with that name already exists...");
                } else {
                    return Response.OK;
                }
                break;
        }
        send(player);
        return Response.ERROR;
    }

    public Response process(Settlement settlement, Response response) {
        if(response == Response.OK) {
            if (type == Type.ROLE_CREATE) {
                settlement.add(Role.create(this.response));
            }
        }
        return response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
