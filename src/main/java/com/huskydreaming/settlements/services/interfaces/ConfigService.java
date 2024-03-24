package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.interfaces.Service;
import com.huskydreaming.settlements.persistence.Flag;
import com.huskydreaming.settlements.persistence.roles.Role;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ConfigService extends Service {

    List<Role> deserializeDefaultRoles(Plugin plugin);

    Set<Flag> deserializeDefaultFlags(Plugin plugin);

    List<String> deserializeDisabledWorlds(Plugin plugin);

    String deserializeEmptyPlaceholder(Plugin plugin);

    boolean deserializeTeleportation(Plugin plugin);

    Map<String, Integer> deserializeDefaults(Plugin plugin);
}