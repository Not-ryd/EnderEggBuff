package com.ryd.eggbuff;

import com.ryd.eggbuff.events.EggBlacklist;
import com.ryd.eggbuff.events.InventoryClick;
import com.ryd.eggbuff.events.PlayerInteract;
import com.ryd.eggbuff.events.PlayerQuit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class EggBuff extends JavaPlugin {

    @Override
    public void onEnable() {
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new PlayerInteract(this), this);
        pluginManager.registerEvents(new InventoryClick(this), this);
        pluginManager.registerEvents(new EggBlacklist(this), this);
        pluginManager.registerEvents(new PlayerQuit(), this);
    }

    @Override
    public void onDisable() {
        EffectManager.removePlayerEffects();
    }
}
