package com.ryd.eggbuff.events;

import com.ryd.eggbuff.BuffInventory;
import com.ryd.eggbuff.EggBuff;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Logger;

public class InventoryClick implements Listener {
    private final EggBuff plugin;
    private final Logger logger;

    public InventoryClick(EggBuff plugin) {
        this.plugin = plugin;
        this.logger = this.plugin.getLogger();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if (!(inventory.getHolder(false) instanceof BuffInventory buffInventory)) {
            return;
        }

        int slot = event.getSlot();

        if (slot >= 9) {
            return;
        }

        ItemStack item = inventory.getItem(slot);

        event.setCancelled(true);

        if (item == null) {
            return;
        }

        if (slot == 6) {
            buffInventory.confirm(player);
        } else if (slot == 8) {
            buffInventory.cancel();
        } else if (slot < 6) {
            if (event.isLeftClick()) {
                buffInventory.selectPrimary(slot);
            } else if (event.isRightClick()) {
                buffInventory.selectSecondary(slot);
            }
        }
    }
}
