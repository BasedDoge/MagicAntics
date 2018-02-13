package main;

import spells.SpellSelecting;
import spells.SpellCasting;
import spells.SpellCombining;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import static org.bukkit.inventory.ItemFlag.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class MagicAnticsMain extends JavaPlugin {

    public static String[] spells = new String[]{"Active#", "FireBall", "Ice Bolt", "Lightning Storm", "Fangs", "Inferno"};

    @Override
    public void onEnable() {
        SpellCasting MASpells = new SpellCasting();
        getServer().getPluginManager().registerEvents(MASpells, this);
        getServer().getPluginManager().registerEvents(new SpellCombining(), this);
        getServer().getPluginManager().registerEvents(new SpellSelecting(), this);
        MASpells.setPlugin(this);

        //remove these
        this.registerSpellTomeRecipe();
        this.registerFirstSpellRecipe();
        this.registerSecondSpellRecipe();
        this.registerThirdSpellRecipe();
        this.registerFourthSpellRecipe();
        this.registerFithSpellRecipe();
    }

    public void registerSpellTomeRecipe() {
        ItemStack spellTome = new ItemStack(Material.ENCHANTED_BOOK, 1);
        ItemMeta meta = spellTome.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Spell Tome");
        meta.setLore(Arrays.asList(ChatColor.DARK_GRAY + spells[0]));
        meta.addItemFlags(HIDE_POTION_EFFECTS);
        spellTome.setItemMeta(meta);

        ShapedRecipe tomeRecipe = new ShapedRecipe(new NamespacedKey(this, "Spell_Tome"), spellTome);
        tomeRecipe.shape(" g ", "gbg", " g ");
        tomeRecipe.setIngredient('g', Material.GLOWSTONE_DUST);
        tomeRecipe.setIngredient('b', Material.BOOK);
        Bukkit.getServer().addRecipe(tomeRecipe);
    }

    //these crafting recipes are for developer use only
    public void registerFirstSpellRecipe() {
        ItemStack spellSheet = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = spellSheet.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Incantation");
        meta.setLore(Arrays.asList(ChatColor.RED + spells[1]));
        meta.addEnchant(Enchantment.MENDING, 1, true);
        meta.addItemFlags(HIDE_POTION_EFFECTS);
        meta.addItemFlags(HIDE_ENCHANTS);
        spellSheet.setItemMeta(meta);

        ShapedRecipe sheetRecipe = new ShapedRecipe(new NamespacedKey(this, "Spell_Sheet0"), spellSheet);
        sheetRecipe.shape(" r ", " p ", "   ");
        sheetRecipe.setIngredient('r', Material.REDSTONE);
        sheetRecipe.setIngredient('p', Material.PAPER);
        Bukkit.getServer().addRecipe(sheetRecipe);
    }

    public void registerSecondSpellRecipe() {
        ItemStack spellSheet = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = spellSheet.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Incantation");
        meta.setLore(Arrays.asList(ChatColor.AQUA + spells[2]));
        meta.addEnchant(Enchantment.MENDING, 1, true);
        meta.addItemFlags(HIDE_POTION_EFFECTS);
        meta.addItemFlags(HIDE_ENCHANTS);
        spellSheet.setItemMeta(meta);

        ShapedRecipe sheetRecipe = new ShapedRecipe(new NamespacedKey(this, "Spell_Sheet1"), spellSheet);
        sheetRecipe.shape(" s ", " p ", "   ");
        sheetRecipe.setIngredient('s', Material.SUGAR);
        sheetRecipe.setIngredient('p', Material.PAPER);
        Bukkit.getServer().addRecipe(sheetRecipe);
    }

    public void registerThirdSpellRecipe() {
        ItemStack spellSheet = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = spellSheet.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Incantation");
        meta.setLore(Arrays.asList(ChatColor.YELLOW + spells[3]));
        meta.addEnchant(Enchantment.MENDING, 1, true);
        meta.addItemFlags(HIDE_POTION_EFFECTS);
        meta.addItemFlags(HIDE_ENCHANTS);
        spellSheet.setItemMeta(meta);

        ShapedRecipe sheetRecipe = new ShapedRecipe(new NamespacedKey(this, "Spell_Sheet2"), spellSheet);
        sheetRecipe.shape(" g ", " p ", "   ");
        sheetRecipe.setIngredient('g', Material.GLOWSTONE_DUST);
        sheetRecipe.setIngredient('p', Material.PAPER);
        Bukkit.getServer().addRecipe(sheetRecipe);
    }

    public void registerFourthSpellRecipe() {
        ItemStack spellSheet = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = spellSheet.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Incantation");
        meta.setLore(Arrays.asList(ChatColor.GRAY + spells[4]));
        meta.addEnchant(Enchantment.MENDING, 1, true);
        meta.addItemFlags(HIDE_POTION_EFFECTS);
        meta.addItemFlags(HIDE_ENCHANTS);
        spellSheet.setItemMeta(meta);

        ShapedRecipe sheetRecipe = new ShapedRecipe(new NamespacedKey(this, "Spell_Sheet3"), spellSheet);
        sheetRecipe.shape(" c ", " p ", "   ");
        sheetRecipe.setIngredient('c', Material.CLAY_BALL);
        sheetRecipe.setIngredient('p', Material.PAPER);
        Bukkit.getServer().addRecipe(sheetRecipe);
    }

    public void registerFithSpellRecipe() {
        ItemStack spellSheet = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = spellSheet.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Incantation");
        meta.setLore(Arrays.asList(ChatColor.DARK_RED + spells[5]));
        meta.addEnchant(Enchantment.MENDING, 1, true);
        meta.addItemFlags(HIDE_POTION_EFFECTS);
        meta.addItemFlags(HIDE_ENCHANTS);
        spellSheet.setItemMeta(meta);

        ShapedRecipe sheetRecipe = new ShapedRecipe(new NamespacedKey(this, "Spell_Sheet4"), spellSheet);
        sheetRecipe.shape(" f ", " p ", "   ");
        sheetRecipe.setIngredient('f', Material.FLINT);
        sheetRecipe.setIngredient('p', Material.PAPER);
        Bukkit.getServer().addRecipe(sheetRecipe);
    }
}
