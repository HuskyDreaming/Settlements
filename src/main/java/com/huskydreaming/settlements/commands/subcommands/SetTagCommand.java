package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.CommandAnnotation;
import com.huskydreaming.huskycore.commands.providers.PlayerCommandProvider;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.enumeration.types.SettlementDefaultType;
import com.huskydreaming.settlements.services.interfaces.ConfigService;
import com.huskydreaming.settlements.storage.persistence.Config;
import com.huskydreaming.settlements.storage.persistence.Member;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.storage.persistence.Role;
import com.huskydreaming.settlements.enumeration.RolePermission;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.types.Message;
import org.bukkit.entity.Player;

@CommandAnnotation(label = CommandLabel.SET_TAG, arguments = " [tag]")
public class SetTagCommand implements PlayerCommandProvider {

    private final ConfigService configService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public SetTagCommand(HuskyPlugin plugin) {
        configService = plugin.provide(ConfigService.class);
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void onCommand(Player player, String[] strings) {
        if (strings.length != 2) return;
        if (!memberService.hasSettlement(player)) {
            player.sendMessage(Message.PLAYER_NULL.prefix());
            return;
        }

        Member member = memberService.getCitizen(player);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());
        Role role = roleService.getRole(member);

        if(!(role.hasPermission(RolePermission.EDIT_TAGS) || settlement.isOwner(player))) {
            player.sendMessage(Message.GENERAL_NO_PERMISSIONS.prefix());
            return;
        }

        String string = strings[1];
        Config config = configService.getConfig();
        int minimumTagLength = config.getSettlementDefault(SettlementDefaultType.MIN_TAG_LENGTH);
        int maximumTagLength = config.getSettlementDefault(SettlementDefaultType.MAX_TAG_LENGTH);

        if (string.length() > maximumTagLength) {
            player.sendMessage(Message.TAG_LONG.prefix(maximumTagLength));
            return;
        }

        if (string.length() < minimumTagLength) {
            player.sendMessage(Message.TAG_SHORT.prefix(minimumTagLength));
            return;
        }

        settlement.setTag(string);
        player.sendMessage(Message.TAG_SET.prefix(string));
    }
}