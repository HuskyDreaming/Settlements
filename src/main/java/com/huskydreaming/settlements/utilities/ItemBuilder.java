package com.huskydreaming.settlements.utilities;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

    private boolean enchanted = false;
    private String displayName = "Item";
    private List<String> lore = null;
    private Material material = Material.STONE;
    private int amount = 1;

    public ItemBuilder() {

    }

    public static ItemBuilder create() {
        return new ItemBuilder();
    }

    public ItemBuilder setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public ItemBuilder setLore(String... strings) {
        this.lore = Arrays.asList(strings);
        return this;
    }

    public ItemBuilder setEnchanted(boolean enchanted) {
        this.enchanted = enchanted;
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemStack buildPlayer(OfflinePlayer offlinePlayer) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        if(skullMeta != null) {
            if(enchanted) skullMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            skullMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            skullMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            skullMeta.setDisplayName(displayName);
            skullMeta.setOwnerProfile(offlinePlayer.getPlayerProfile());
            if(lore != null) skullMeta.setLore(lore);
            itemStack.setItemMeta(skullMeta);
        }
        return itemStack;
    }

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if(itemMeta != null) {
            if(enchanted) itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.setDisplayName(displayName);
            if(lore != null) itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
        }
        itemStack.setAmount(amount);
        return itemStack;
    }
}
