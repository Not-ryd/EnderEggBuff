package com.ryd.eggbuff.events;

import com.ryd.eggbuff.BuffInventory;
import com.ryd.eggbuff.EggBuff;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.logging.Logger;

public class PlayerInteract implements Listener {
    private final EggBuff plugin;
    private final Logger logger;

    public PlayerInteract(EggBuff plugin) {
        this.plugin = plugin;
        this.logger = this.plugin.getLogger();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getMaterial() != Material.DRAGON_EGG || (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        Player player = event.getPlayer();
        BuffInventory buffInventory = new BuffInventory(plugin);

        event.setCancelled(true);
        player.openInventory(buffInventory.getInventory());
    }
}
