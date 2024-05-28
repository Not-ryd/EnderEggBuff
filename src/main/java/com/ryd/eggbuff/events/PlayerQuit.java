package com.ryd.eggbuff.events;

import com.ryd.eggbuff.EffectManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        EffectManager.removePlayerEffects(event.getPlayer());
    }
}
