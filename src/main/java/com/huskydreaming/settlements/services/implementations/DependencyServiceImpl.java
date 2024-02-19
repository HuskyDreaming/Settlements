package com.huskydreaming.settlements.services.implementations;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.dependencies.SettlementPlaceholderExpansion;
import com.huskydreaming.settlements.services.interfaces.DependencyService;
import com.huskydreaming.settlements.services.base.DependencyType;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.util.HashSet;
import java.util.Set;

public class DependencyServiceImpl implements DependencyService {

    private final Set<String> types = new HashSet<>();

    @Override
    public boolean isWorldGuard(Player player) {
        if (types.contains(DependencyType.WORLDGUARD.toString())) {
            Location location = BukkitAdapter.adapt(player.getLocation());
            RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = regionContainer.createQuery();
            ApplicableRegionSet set = query.getApplicableRegions(location);
            return set.size() != 0;
        }
        return false;
    }

    @Override
    public void deserialize(SettlementPlugin plugin) {
        PluginManager pluginManager = plugin.getServer().getPluginManager();

        for(String softDependency : plugin.getDescription().getSoftDepend()) {
            if (pluginManager.getPlugin(softDependency) != null) types.add(softDependency);
        }

        if (!types.isEmpty()) {
            plugin.getLogger().info("Dependencies Found: ");
            types.forEach(type -> plugin.getLogger().info("- " + type));
        } else {
            plugin.getLogger().info("No dependencies found. Using basic version of the plugin.");
        }

        if (Bukkit.getPluginManager().isPluginEnabled(DependencyType.PLACEHOLDER_API.toString())) {
            new SettlementPlaceholderExpansion(plugin).register();
        }
    }
}