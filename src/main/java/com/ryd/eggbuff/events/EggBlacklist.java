package com.ryd.eggbuff.events;

import com.ryd.eggbuff.EffectManager;
import com.ryd.eggbuff.EggBuff;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.logging.Logger;

public class EggBlacklist implements Listener {
    private final EggBuff plugin;
    private final Logger logger;

    public EggBlacklist(EggBuff plugin) {
        this.plugin = plugin;
        this.logger = this.plugin.getLogger();
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        HumanEntity entity = event.getPlayer();
        if (!(entity instanceof Player player)) {
            return;
        }

        if (!player.getInventory().contains(Material.DRAGON_EGG)) {
            EffectManager.removePlayerEffects(player);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) {
            return;
        }

        if (clickedInventory.getType() == InventoryType.ENDER_CHEST && event.getCursor().getType() == Material.DRAGON_EGG) {
            event.setCancelled(true);
            return;
        }

        if (clickedInventory.getType() == InventoryType.ENDER_CHEST && event.getCursor().getItemMeta() instanceof BlockStateMeta meta && meta.getBlockState() instanceof Container container) {
            Inventory containerInventory = container.getInventory();
            if (checkContainer(containerInventory)) {
                event.setCancelled(true);
                return;
            }
        }

        HumanEntity entity = event.getWhoClicked();
        if (!(entity instanceof Player player)) {
            return;
        }

        ItemStack item = event.getCurrentItem();

        if (item == null) {
            return;
        }

        if (player.getOpenInventory().getType() == InventoryType.ENDER_CHEST && event.isShiftClick() && item.getType() == Material.DRAGON_EGG) {
            event.setCancelled(true);
            return;
        }

        if (player.getOpenInventory().getType() == InventoryType.ENDER_CHEST && event.isShiftClick() && item.getItemMeta() instanceof BlockStateMeta meta && meta.getBlockState() instanceof Container container) {
            Inventory containerInventory = container.getInventory();
            if (checkContainer(containerInventory)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        for (int slot : event.getRawSlots()) {
            Inventory inventory = event.getView().getInventory(slot);
            if (inventory == null) {
                continue;
            }

            if (inventory.getType() == InventoryType.ENDER_CHEST && event.getOldCursor().getType() == Material.DRAGON_EGG) {
                event.setCancelled(true);
                return;
            }

            if (inventory.getType() == InventoryType.ENDER_CHEST && event.getOldCursor().getItemMeta() instanceof BlockStateMeta meta && meta.getBlockState() instanceof Container container) {
                Inventory containerInventory = container.getInventory();
                if (checkContainer(containerInventory)) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().getType() == Material.DRAGON_EGG && !event.getPlayer().getInventory().contains(Material.DRAGON_EGG)) {
            EffectManager.removePlayerEffects(event.getPlayer());
        }
    }

    private static boolean checkContainer(Inventory inventory) {
        for (ItemStack item : inventory.getContents()) {
            if (item == null) {
                continue;
            }

            if (item.getItemMeta() instanceof BlockStateMeta meta && meta.getBlockState() instanceof Container container) {
                Inventory containerInventory = container.getInventory();
                if (checkContainer(containerInventory)) {
                    return true;
                }
            } else if (item.getType() == Material.DRAGON_EGG){
                return true;
            }
        }
        return false;
    }
}
