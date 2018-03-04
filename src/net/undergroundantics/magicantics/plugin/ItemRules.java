package net.undergroundantics.magicantics.plugin;

import net.undergroundantics.magicantics.spells.Spell;
import java.util.Arrays;
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
    public static boolean SpellTomeCheck(ItemStack item) {
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
     This defines what a 'Incantation'/'spell sheet' is.
     */
    public static boolean SpellSheetCheck(ItemStack item) {
        boolean isSpell = false;
        if (item != null && item.getType() == Material.PAPER) {
            if (item.getItemMeta().hasItemFlag(ItemFlag.HIDE_POTION_EFFECTS)) {
                isSpell = true;
            }
        }
        return isSpell;
    }

    public static ItemStack newSpellSheet(Spell spell) {
        ItemStack spellSheet = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = spellSheet.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Incantation");
        meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', spell.getDisplayName())));
        meta.addEnchant(Enchantment.MENDING, 1, true);
        meta.addItemFlags(HIDE_POTION_EFFECTS);
        meta.addItemFlags(HIDE_ENCHANTS);
        spellSheet.setItemMeta(meta);
        return spellSheet;
    }

}
