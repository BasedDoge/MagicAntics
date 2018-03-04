package net.undergroundantics.magicantics.plugin;

import net.undergroundantics.magicantics.commands.*;
import net.undergroundantics.magicantics.spells.*;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import static org.bukkit.inventory.ItemFlag.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;


public class MagicAntics extends JavaPlugin {
 
    public static final String NAME = "MagicAntics";

    public MagicAntics() {
        super();
        spells = new Spell[] { new Fangs(), new Fireball(), new Icicle(this), new Inferno(), new Stasis(this), new Thunderstorm(), new Familiar(this), new Vindication(this) };
    }

    public Spell getSpell(String name) {
        for (Spell spell : spells) {
            if (spell.getName().equals(name)) {
                return spell;
            }
        }
        return null;
    }
    
    public Spell[] getSpells() {
        return spells;
    }

    public static void sendMessage(Player p, String msg) {
        p.sendMessage("[Spellbook] " + msg);
    }

    @Override
    public void onEnable() {
        MagicAnticsCommandExecutor ce = new MagicAnticsCommandExecutor(this);
        CommandTabComplete tc = new CommandTabComplete(this);
        getServer().getPluginManager().registerEvents(new SpellCasting(this), this);
        getServer().getPluginManager().registerEvents(new SpellCombining(), this);
        getServer().getPluginManager().registerEvents(new SpellSelecting(), this);
        getServer().getPluginManager().registerEvents(new ProjectileEffects(this), this);
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
    
    private final Spell[] spells;

}