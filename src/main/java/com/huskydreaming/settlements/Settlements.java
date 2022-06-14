package com.huskydreaming.settlements;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.huskydreaming.settlements.modules.PluginModule;
import org.bukkit.plugin.java.JavaPlugin;

public class Settlements extends JavaPlugin implements Module {

    @Override
    public void onEnable() {
        getLogger().info("This plugin does nothing at the moment ;)");

        Injector injector = Guice.createInjector(new PluginModule(this));
        injector.injectMembers(this);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void configure(Binder binder) {

    }
}
