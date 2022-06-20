package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.services.SettlementService;
import com.huskydreaming.settlements.services.base.ServiceRegistry;
import com.huskydreaming.settlements.services.base.ServiceType;
import com.huskydreaming.settlements.utilities.Chat;
import com.huskydreaming.settlements.utilities.Locale;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.CREATE)
public class CreateCommand implements CommandInterface {

    @Override
    public void run(Player player, String[] strings) {
        if (strings.length == 2) {
            SettlementService settlementService = (SettlementService) ServiceRegistry.getService(ServiceType.SETTLEMENT);

            String string = strings[1];
            if (settlementService.hasSettlement(player)) {
                player.sendMessage(Chat.parameterize(Locale.SETTLEMENT_PLAYER_EXISTS));
                return;
            }

            if (settlementService.isSettlement(string)) {
                player.sendMessage(Chat.parameterize(Locale.SETTLEMENT_EXIST));
                return;
            }

            if (settlementService.isSettlement(player.getLocation().getChunk())) {
                player.sendMessage(Chat.parameterize(Locale.SETTLEMENT_ESTABLISHED));
                return;
            }

            player.sendMessage(Chat.parameterize(Locale.SETTLEMENT_CREATED, string));
            settlementService.createSettlement(player, string);
        }
    }
}
