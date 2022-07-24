package com.huskydreaming.settlements.services.implementations;

import com.google.inject.Singleton;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.services.DependencyService;
import com.huskydreaming.settlements.services.base.DependencyType;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.util.HashSet;
import java.util.Set;

@Singleton
public class DependencyServiceImpl implements DependencyService {

    private final Set<DependencyType> types = new HashSet<>();

    @Override
    public boolean isWorldGuard(Player player) {
        if (types.contains(DependencyType.WORLDGUARD)) {
            Location location = BukkitAdapter.adapt(player.getLocation());
            RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = regionContainer.createQuery();
            ApplicableRegionSet set = query.getApplicableRegions(location);
            return set.size() != 0;
        }
        return false;
    }

    @Override
    public void serialize(SettlementPlugin plugin) {

    }

    @Override
    public void deserialize(SettlementPlugin plugin) {
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        StringBuilder stringBuilder = new StringBuilder();

        for (DependencyType dependencyType : DependencyType.values()) {
            if (pluginManager.getPlugin(dependencyType.name()) != null) {
                types.add(dependencyType);
                stringBuilder.append("- ").append(dependencyType.name());
            }
        }

        if (types.size() > 0) {
            plugin.getLogger().info("Dependencies Found: ");
            plugin.getLogger().info(stringBuilder.toString());
        } else {
            plugin.getLogger().info("No dependencies found. Using basic version of the plugin. ");
        }
    }
}
