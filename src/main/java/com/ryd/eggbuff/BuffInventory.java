package com.ryd.eggbuff;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Logger;

public class BuffInventory implements InventoryHolder {
    private static final HashMap<Integer, PotionEffectType> effectTypeHashMap = new HashMap<>() {{
        put(0, PotionEffectType.SPEED);
        put(1, PotionEffectType.FAST_DIGGING);
        put(2, PotionEffectType.DAMAGE_RESISTANCE);
        put(3, PotionEffectType.JUMP);
        put(4, PotionEffectType.INCREASE_DAMAGE);
        put(5, PotionEffectType.REGENERATION);
    }};
    private final Inventory buffInventory;
    private final EggBuff plugin;
    private final Logger logger;
    private int primary = 9;
    private int secondary = 9;

    public BuffInventory(EggBuff plugin) {
        buffInventory = plugin.getServer().createInventory(this, InventoryType.DROPPER, Component.text("Ender Egg Power Up"));
        buffInventory.addItem(
                createStack(Material.LEATHER_BOOTS, "Speed"), createStack(Material.GOLDEN_PICKAXE, "Haste"), createStack(Material.SHIELD, "Resistance"),
                createStack(Material.RABBIT_FOOT, "Jump Boost"), createStack(Material.DIAMOND_SWORD, "Strength"), createStack(Material.GOLDEN_APPLE, "Regeneration"),
                createStack(Material.GREEN_TERRACOTTA, "Confirm"), createStack(Material.GLASS_PANE), createStack(Material.RED_TERRACOTTA, "Cancel")
        );
        buffInventory.setItem(7, createStack(Material.AIR));
        this.plugin = plugin;
        this.logger = this.plugin.getLogger();
    }

    @Override
    public @NotNull Inventory getInventory() {
        return buffInventory;
    }

    public void selectPrimary(int i) {
        ItemStack item = getItemStack(buffInventory, i);
        if (primary != 9) {
            ItemStack oldItem = getItemStack(buffInventory, primary);
            removeEnchantGlint(oldItem);
            oldItem.lore(null);
        }

        if (secondary == i) {
            secondary = 9;
        }

        primary = i;
        setEnchantGlint(item);

        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text("Primary").color(TextColor.color(0,255,0)));
        item.lore(lore);
    }

    public void selectSecondary(int i) {
        ItemStack item = getItemStack(buffInventory, i);
        if (secondary != 9) {
            ItemStack oldItem = getItemStack(buffInventory, secondary);
            removeEnchantGlint(oldItem);
            oldItem.lore(null);
        }

        if (primary == i) {
            primary = 9;
        }

        secondary = i;
        setEnchantGlint(item);

        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text("Secondary").color(TextColor.color(255,255,0)));
        item.lore(lore);
    }

    public void confirm(Player player) {
        if (primary == 9 || secondary == 9) {
            return;
        }
        Collection<PotionEffect> effects = new ArrayList<>();
        effects.add(new PotionEffect(effectTypeHashMap.get(primary), -1, 1));
        effects.add(new PotionEffect(effectTypeHashMap.get(secondary), -1, 0));
        EffectManager.applyEggEffects(effects, player);
        buffInventory.close();
    }

    public void cancel() {
        buffInventory.close();
    }

    public static ItemStack createStack(Material material) {
        return new ItemStack(material);
    }

    public static ItemStack createStack(Material material, String name) {
        return setDisplayName(createStack(material), name);
    }

    public static ItemStack setDisplayName(ItemStack itemStack, String name) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null)  {
            itemMeta.displayName(Component.text(name));
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    public static String getDisplayName(ItemStack itemStack) {
        Component displayName = itemStack.getItemMeta().displayName();
        if (displayName != null) {
            return PlainTextComponentSerializer.plainText().serialize(displayName);
        }
        return "";
    }

    public static void setEnchantGlint(ItemStack item) {
        item.addUnsafeEnchantment(Enchantment.CHANNELING, 1);
        item.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    public static void removeEnchantGlint(ItemStack item) {
        item.removeEnchantment(Enchantment.CHANNELING);
        item.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    public static ItemStack getItemStack(Inventory inventory, int i) {
        ItemStack itemStack = inventory.getItem(i);
        if (itemStack != null) {
            return  itemStack;
        }
        return new ItemStack(Material.AIR);
    }
}
