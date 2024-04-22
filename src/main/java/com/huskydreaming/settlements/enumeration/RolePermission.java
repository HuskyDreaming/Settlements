package com.huskydreaming.settlements.enumeration;

import com.huskydreaming.huskycore.interfaces.Permission;
import com.huskydreaming.huskycore.utilities.Util;
import com.huskydreaming.settlements.storage.types.Message;

public enum RolePermission implements Permission {
    DEFAULT(""),
    // CLAIMS
    CLAIM_BREAK(Message.PERMISSIONS_CLAIM_BREAK.parse()),
    CLAIM_PLACE(Message.PERMISSIONS_CLAIM_PLACE.parse()),
    CLAIM_INTERACT(Message.PERMISSIONS_CLAIM_INTERACT.parse()),
    CLAIM_LAND(Message.PERMISSIONS_CLAIM_LAND.parse()),
    CLAIM_TELEPORT(Message.PERMISSIONS_CLAIM_TELEPORT.parse()),

    // EDIT
    EDIT_MEMBERS(Message.PERMISSIONS_EDIT_MEMBERS.parse()),
    EDIT_CLAIMS(Message.PERMISSIONS_EDIT_CLAIMS.parse()),
    EDIT_HOMES(Message.PERMISSIONS_EDIT_HOMES.parse()),
    EDIT_SPAWN(Message.PERMISSIONS_EDIT_SPAWN.parse()),
    EDIT_ROLES(Message.PERMISSIONS_EDIT_ROLES.parse()),
    EDIT_FLAGS(Message.PERMISSIONS_EDIT_FLAGS.parse()),
    EDIT_DESCRIPTION(Message.PERMISSIONS_EDIT_DESCRIPTION.parse()),
    EDIT_TAGS(Message.PERMISSIONS_EDIT_TAGS.parse()),

    // MEMBER
    MEMBER_KICK(Message.PERMISSIONS_MEMBER_KICK.parse()),
    MEMBER_TRUST(Message.PERMISSIONS_MEMBER_TRUST.parse()),
    MEMBER_INVITE(Message.PERMISSIONS_MEMBER_INVITE.parse()),
    MEMBER_KICK_EXEMPT(Message.PERMISSIONS_MEMBER_KICK_EXEMPT.parse()),
    HOME_TELEPORT(Message.PERMISSIONS_HOME_TELEPORT.parse()),
    SPAWN_TELEPORT(Message.PERMISSIONS_SPAWN_TELEPORT.parse());

    private final String description;

    RolePermission(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return Util.capitalize(name().toLowerCase().replace("_", " "));
    }
}