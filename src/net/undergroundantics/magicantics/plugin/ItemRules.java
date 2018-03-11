package net.undergroundantics.magicantics.plugin;

import java.util.Arrays;
import net.undergroundantics.magicantics.spells.Spell;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import static org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS;
import static org.bukkit.inventory.ItemFlag.HIDE_POTION_EFFECTS;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemRules {

    /*
     Used by events to check a player's Itemstack against these rules.
     This defines what a 'spell tome' is.
     */
    public static boolean isSpellTome(ItemStack item) {
        boolean isSpellTome = false;
        if (item != null) {
            if (item.getType() == Material.ENCHANTED_BOOK) {
                if (item.getItemMeta().hasItemFlag(ItemFlag.HIDE_POTION_EFFECTS)) {
                    isSpellTome = true;
                }
            }
        }
        return isSpellTome;
    }
    /*
     Used by events to check a player's Itemstack against these rules.
     This defines what a 'spell tome' is.
     */
    public static boolean isSpellBook(ItemStack item) {
        boolean isSpellTome = false;
        if (item != null) {
            if (item.getType() == Material.BOOK) {
                if (item.getItemMeta().hasItemFlag(ItemFlag.HIDE_POTION_EFFECTS)) {
                    isSpellTome = true;
                }
            }
        }
        return isSpellTome;
    }

    /*
     Used by events to check a player's Itemstack against these rules.
     This defines what a 'spell scroll' is.
     */
    public static boolean isSpellScroll(ItemStack item) {
        boolean isSpell = false;
        if (item != null && item.getType() == Material.PAPER) {
            if (item.getItemMeta().hasItemFlag(ItemFlag.HIDE_POTION_EFFECTS)) {
                isSpell = true;
            }
        }
        return isSpell;
    }

    public static ItemStack createSpellScroll(Spell spell) {
        ItemStack spellSheet = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = spellSheet.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Spell Scroll");
        meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', spell.getDisplayName())));
        meta.addEnchant(Enchantment.MENDING, 1, true);
        meta.addItemFlags(HIDE_POTION_EFFECTS);
        meta.addItemFlags(HIDE_ENCHANTS);
        spellSheet.setItemMeta(meta);
        return spellSheet;
    }

    public static ItemStack createSpellBook(Spell spell) {
        ItemStack spellBook = new ItemStack(Material.BOOK, 1);
        ItemMeta meta = spellBook.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Spell Book");
        meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', spell.getDisplayName())));
        meta.addEnchant(Enchantment.MENDING, 1, true);
        meta.addItemFlags(HIDE_POTION_EFFECTS);
        meta.addItemFlags(HIDE_ENCHANTS);
        spellBook.setItemMeta(meta);
        return spellBook;
    }

    public static ItemStack createSpellTome() {
        ItemStack spellBook = new ItemStack(Material.ENCHANTED_BOOK, 1);
        ItemMeta meta = spellBook.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Spell Tome");
        meta.addItemFlags(HIDE_POTION_EFFECTS);
        spellBook.setItemMeta(meta);
        return spellBook;
    }

}
