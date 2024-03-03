package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.registries.CommandRegistry;
import com.huskydreaming.settlements.storage.enumerations.Locale;
import com.huskydreaming.settlements.utilities.Remote;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Command(label = CommandLabel.HELP, arguments = " [page]")
public class HelpCommand implements CommandInterface {

    private final CommandRegistry commandRegistry;

    public HelpCommand(SettlementPlugin plugin) {
        commandRegistry = plugin.getCommandRegistry();
    }

    @Override
    public void run(Player player, String[] strings) {
        int index = 1;

        if(strings.length > 1) {
            index = Integer.parseInt(strings[1]);
            if (strings[1].equals("0")) index = 1;
        }

        Set<CommandInterface> commandInterfaces = commandRegistry.getCommands();
        int page = (int) Math.ceil((double) commandInterfaces.size() / 6);

        if (index > page) {
            player.sendMessage(Remote.prefix(Locale.HELP_PAGE_LIMIT, page));
        } else {
            for(int i = 0; i < 10; i++) {
                player.sendMessage("");
            }

            player.sendMessage(Remote.parameterize(Locale.HELP_PAGE_HEADER, index, page));
            player.sendMessage("");

            List<String> stringList = new ArrayList<>();

            commandInterfaces.forEach(command -> stringList.add("/settlements " + command.getLabel().name().toLowerCase() + command.getArguments()));
            Collections.sort(stringList);

            for (int i = 0; i < stringList.size(); i++) {
                if (i >= 5) break;
                int number = i + ((index - 1) * 5);
                if (number >= stringList.size()) {
                    player.sendMessage("");
                } else {
                    String string = stringList.get(number);
                    TextComponent message = new TextComponent(Remote.parameterize(Locale.HELP_PAGE_FORMAT, number + 1, string));
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, string));
                    player.spigot().sendMessage(message);
                }
            }

            player.sendMessage("");

            TextComponent next = new TextComponent(Locale.HELP_PAGE_NEXT.parse());
            TextComponent previous = new TextComponent(Locale.HELP_PAGE_PREVIOUS.parse());
            TextComponent spacer = new TextComponent("            ");

            if(index == 1) {
                previous.setText(Remote.parameterize(Locale.HELP_PAGE_DISABLED) + previous.getText());
                next.setText(Remote.parameterize(Locale.HELP_PAGE_ENABLED) + next.getText());
                next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/s help " + (index + 1)));
            } else if((index + 1) > page) {
                next.setText(Remote.parameterize(Locale.HELP_PAGE_DISABLED) + next.getText());
                previous.setText(Remote.parameterize(Locale.HELP_PAGE_ENABLED) + previous.getText());
                previous.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/s help " + (index - 1)));
            } else {
                next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/s help " + (index + 1)));
                next.setText(Remote.parameterize(Locale.HELP_PAGE_ENABLED) + next.getText());
                previous.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/s help " + (index - 1)));
                previous.setText(Remote.parameterize(Locale.HELP_PAGE_ENABLED) + previous.getText());
            }

            player.spigot().sendMessage(spacer,previous, spacer , next, spacer);
            player.sendMessage("");
        }
    }
}
