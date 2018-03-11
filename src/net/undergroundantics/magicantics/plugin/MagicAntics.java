package net.undergroundantics.magicantics.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.logging.Level;
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
import net.undergroundantics.magicantics.spells.*;
import net.undergroundantics.magicantics.commands.*;

public class MagicAntics extends JavaPlugin {
 
    public static final String NAME = "MagicAntics";

    public MagicAntics() {
        super();
        spells = new Spell[] { new Fangs(this), new Fireball(), new Icicle(this),
                 new Inferno(), new Stasis(this), new Thunderstorm(),
                 new Familiar(this), new Vindication(this), new Atomiser(), new Phase()};
    }
    
    public Spell[] getSpells() {
        return spells;
    }

    public Spell getSpellFromName(String name) {
        for (Spell spell : spells) {
            if (spell.getName().equals(name)) {
                return spell;
            }
        }
        return null;
    }

    public Spell getSpellFromDisplayName(String displayName) {
        for (Spell spell : spells) {
            if (spell.getDisplayName().equals(displayName)) {
                return spell;
            }
        }
        return null;
    }

    public static void sendMessage(Player p, String msg) {
        p.sendMessage("[Spellbook] " + msg);
    }

    @Override
    public void onEnable() {
        MagicAnticsCommandExecutor ce = new MagicAnticsCommandExecutor(this);
        CommandTabComplete tc = new CommandTabComplete(this);
        getServer().getPluginManager().registerEvents(new SpellCasting(this), this);
        getServer().getPluginManager().registerEvents(new SpellLearning(this), this);
        getServer().getPluginManager().registerEvents(new SpellSelecting(this), this);
        getServer().getPluginManager().registerEvents(new ProjectileEffects(this), this);
        getCommand("spellbook").setExecutor(ce);
        getCommand("spellscroll").setExecutor(ce);
        getCommand("spelltome").setExecutor(ce);
        getCommand("spellbook").setTabCompleter(tc);
        getCommand("spellscroll").setTabCompleter(tc);
        getCommand("spelltome").setTabCompleter(tc);
        this.registerSpellTomeRecipe();
        readPlayerKnowledge();
    }

    private void readPlayerKnowledge() {
        File file = new File(getDataFolder(), PLAYER_KNOWLEDGE_FILE);
        if ( file.exists() ) {
            try ( FileInputStream fis = new FileInputStream(file);
                  ObjectInputStream ois = new ObjectInputStream(fis)) {
                learntSpells = (HashMap) ois.readObject();
            } catch(IOException | ClassNotFoundException ex) {
                getLogger().log(Level.SEVERE, "Error reading player spell knowledge", ex);
            }
        } else {
            learntSpells = new HashMap<>();
        }
    }

    private void writePlayerKnowledge() {
        try {
            File file = new File(getDataFolder(), PLAYER_KNOWLEDGE_FILE);
            if ( ! getDataFolder().exists() )
                getDataFolder().mkdirs();
            if (! file.exists())
                file.createNewFile();
            try (FileOutputStream fos = new FileOutputStream(file); 
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(learntSpells);
            }
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Error writing player spell knowledge", ex);
        }
    }
    
    public List<Spell> getLearntSpells(Player p) {
        List<Spell> spells = new LinkedList<>();
        if ( ! learntSpells.containsKey(p.getUniqueId()) )
            return spells;
        for (String spellName : learntSpells.get(p.getUniqueId()) ) {
            spells.add(getSpellFromName(spellName));
        }
        return spells;
    }

    public boolean hasLearntSpell(Player p, Spell spell) {
        return learntSpells.containsKey(p.getUniqueId()) && learntSpells.get(p.getUniqueId()).contains(spell.getName());
    }
    
    public void learnSpell(Player p, Spell spell) {
        UUID key = p.getUniqueId();
        if ( ! learntSpells.containsKey(key))
            learntSpells.put(key, new TreeSet<>());
        if ( learntSpells.get(p.getUniqueId()).add(spell.getName()) )
            writePlayerKnowledge();
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
    private static final String PLAYER_KNOWLEDGE_FILE = "playerknowledge.dat";
    private Map<UUID, Set<String>> learntSpells;
    
}