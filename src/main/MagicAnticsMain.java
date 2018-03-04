package main;

import Commands.CommandTabComplete;
import Commands.MagicAnticsCommandExecutor;
import spells.SpellSelecting;
import spells.SpellCasting;
import spells.SpellCombining;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import static org.bukkit.inventory.ItemFlag.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import spells.ProjectileEffects;

public class MagicAnticsMain extends JavaPlugin {
    
    private static Plugin instance = null;

    public void MagicAnticsMain() throws Exception {
        if (instance == null) {
            instance = this;
        } else {
            throw new Exception("Constructed twice");
        }
    }

    public static Plugin getInstance() throws Exception {
        if (instance == null) {
            throw new Exception("Not constructed");
        } else {
            return instance;
        }
    }

    @Override
    public void onEnable() {
        try {
            getInstance();
        } catch (Exception ex) {
            Logger.getLogger(MagicAnticsMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        MagicAnticsCommandExecutor ce = new MagicAnticsCommandExecutor();
        CommandTabComplete tc = new CommandTabComplete();
        getServer().getPluginManager().registerEvents(new SpellCasting(), this);
        getServer().getPluginManager().registerEvents(new SpellCombining(), this);
        getServer().getPluginManager().registerEvents(new SpellSelecting(), this);
        getServer().getPluginManager().registerEvents(new ProjectileEffects(), this);
        getCommand("NewSpell").setExecutor(ce);
        getCommand("NewSpell").setTabCompleter(tc);

        this.registerSpellTomeRecipe();
    }

    public void registerSpellTomeRecipe() {
        ItemStack spellTome = new ItemStack(Material.ENCHANTED_BOOK, 1);
        ItemMeta meta = spellTome.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Spell Tome");
        meta.setLore(Arrays.asList(ChatColor.DARK_GRAY + "Active#"));
        meta.addItemFlags(HIDE_POTION_EFFECTS);
        spellTome.setItemMeta(meta);

        ShapedRecipe tomeRecipe = new ShapedRecipe(new NamespacedKey(this, "Spell_Tome"), spellTome);
        tomeRecipe.shape(" g ", "gbg", " g ");
        tomeRecipe.setIngredient('g', Material.GLOWSTONE_DUST);
        tomeRecipe.setIngredient('b', Material.BOOK);
        Bukkit.getServer().addRecipe(tomeRecipe);
    }
}