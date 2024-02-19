package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.services.base.ServiceInterface;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;

public interface ConfigService extends ServiceInterface {

    List<Role> deserializeDefaultRoles(Plugin plugin);

    List<String> deserializeDisabledWorlds(Plugin plugin);

    Map<String, Integer> deserializeDefaultMaximum(Plugin plugin);
}
