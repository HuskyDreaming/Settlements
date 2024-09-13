package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.annotations.CommandAnnotation;
import com.huskydreaming.huskycore.interfaces.command.providers.PlayerCommandProvider;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.database.entities.Member;
import com.huskydreaming.settlements.database.entities.Role;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.database.persistence.Config;
import com.huskydreaming.settlements.enumeration.types.SettlementDefaultType;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.enumeration.PermissionType;
import com.huskydreaming.settlements.enumeration.locale.Message;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Set;

@CommandAnnotation(label = CommandLabel.SET_DESCRIPTION, arguments = " [description]")
public class SetDescriptionCommand implements PlayerCommandProvider {

    private final ConfigService configService;
    private final MemberService memberService;
    private final PermissionService permissionService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public SetDescriptionCommand(HuskyPlugin plugin) {
        configService = plugin.provide(ConfigService.class);
        memberService = plugin.provide(MemberService.class);
        permissionService = plugin.provide(PermissionService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void onCommand(Player player, String[] strings) {
        if (strings.length <= 1) return;

        if (!memberService.hasSettlement(player)) {
            player.sendMessage(Message.PLAYER_NULL.prefix());
            return;
        }

        Member member = memberService.getMember(player);
        Settlement settlement = settlementService.getSettlement(member);
        Role role = roleService.getRole(member);

        Set<PermissionType> permissions = permissionService.getPermissions(role);
        if(!(permissions.contains(PermissionType.EDIT_DESCRIPTION) || settlement.isOwner(player))) {
            player.sendMessage(Message.GENERAL_NO_PERMISSIONS.prefix());
            return;
        }

        String[] array = Arrays.copyOfRange(strings, 1, strings.length);
        String string = String.join(" ", array);

        Config config = configService.getConfig();
        int minimumDescriptionLength = config.getSettlementDefault(SettlementDefaultType.MIN_DESCRIPTION_LENGTH);
        int maximumDescriptionLength = config.getSettlementDefault(SettlementDefaultType.MAX_DESCRIPTION_LENGTH);

        if (string.length() > maximumDescriptionLength) {
            player.sendMessage(Message.DESCRIPTION_LONG.prefix(maximumDescriptionLength));
            return;
        }

        if (string.length() < minimumDescriptionLength) {
            player.sendMessage(Message.DESCRIPTION_SHORT.prefix(minimumDescriptionLength));
            return;
        }

        settlement.setDescription(string);
        player.sendMessage(Message.DESCRIPTION.prefix(string));
    }
}