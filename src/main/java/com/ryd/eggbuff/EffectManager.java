package com.ryd.eggbuff;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EffectManager {
    private static final HashMap<String, Collection<PotionEffect>> effectList = new HashMap<>();

    public static void applyEggEffects(Collection<PotionEffect> effects, Player player) {
        String playerName = player.getName();

        if (effectList.containsKey(playerName)) {
            removeEffects(effectList.get(playerName), player);
            effectList.remove(playerName);
        }

        effectList.put(playerName, effects);
        player.addPotionEffects(effects);
    }

    private static void removeEffects(Collection<PotionEffect> effects, Player player) {
        for (PotionEffect effect : effects) {
            player.removePotionEffect(effect.getType());
        }
    }

    public static void removePlayerEffects(Player player) {
        String playerName = player.getName();
        Collection<PotionEffect> effects = effectList.get(playerName);
        if (effects != null) {
            removeEffects(effects, player);
            effectList.remove(playerName);
        }
    }

    public static void removePlayerEffects() {
        for (Map.Entry<String, Collection<PotionEffect>> entry : effectList.entrySet()) {
            String playerName = entry.getKey();
            Player player = Bukkit.getPlayer(playerName);
            removeEffects(entry.getValue(), player);
            effectList.remove(playerName);
        }
    }
}
