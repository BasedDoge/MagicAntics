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
    
    public List<Spell> getSpells() {
        List<Spell> list = new LinkedList<>();
        for (Spell spell : spells) {
            list.add(spell);
        }
        return list;
    }

    public List<Spell> getLearnableSpells() {
        List<Spell> list = new LinkedList<>();
        for (Spell spell : spells) {
            if (spell.isLearnable())
                list.add(spell);
        }
        return list;
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

    /**
     * @pre ItemRules.isSpellScroll(item)
     */
    public Spell getSpellFromSpellScroll(ItemStack item) {
        return getSpellFromDisplayName(item.getItemMeta().getLore().get(0));
    }
    
    /**
     * @pre ItemRules.isSpellBook(item)
     */
    public Spell getSpellFromSpellBook(ItemStack item) {
        return getSpellFromDisplayName(item.getItemMeta().getLore().get(0));
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
    
        
    /**
     * Set the spells on a tome, changing the active spell only if necessary
     * @pre isSpellTome(tome)
     */
    public void setSpells(ItemStack tome, List<Spell> spells) {
        if ( spells.isEmpty() ) {
            ItemMeta meta = tome.getItemMeta();
            meta.setLore(new LinkedList<>());
            tome.setItemMeta(meta);
        } else {
            Spell active = getActiveSpell(tome);
            if (active == null || !spells.contains(active)) {
                active = spells.get(0);
            }
            ItemMeta meta = tome.getItemMeta();
            List<String> lore = new LinkedList<>();
            lore.add(ChatColor.DARK_GRAY + "Active#" + active.getDisplayName());
            for (Spell spell : spells) {
                lore.add(spell.getDisplayName());
            }
            meta.setLore(lore);
            tome.setItemMeta(meta);
        }
        
    }

    /**
     * Return list of all spells on tome
     * @pre isSpellTome(tome)
     */
    public List<Spell> getSpells(ItemStack tome) {
        List<Spell> spells = new LinkedList<Spell>();
        List<String> lines = tome.getItemMeta().getLore();
        if ( lines == null || lines.isEmpty() )
            return spells;
        for (String line : lines.subList(1, lines.size())) {
            spells.add(getSpellFromDisplayName(line));
        }
        return spells;
    }

    /**
     * Return the active spell on tome
     * @pre isSpellTome(tome)
     */
    public Spell getActiveSpell(ItemStack tome) {
        List<String> lore = tome.getItemMeta().getLore();
        if ( lore == null || lore.isEmpty() )
            return null;
        return getSpellFromDisplayName(lore.get(0).split("#")[1]);
    }

    /**
     * Changes the active spell of tome to spell
     * @pre isSpellTome(tome)
     * @pre spell != null
     */
    public void setActiveSpell(ItemStack tome, Spell spell) {
        ItemMeta meta = tome.getItemMeta();
        List<String> lore = meta.getLore();
        String active = ChatColor.DARK_GRAY + "Active#" + spell.getDisplayName();
        if ( lore == null ) {
            lore = new LinkedList<>();
        }
        if ( lore.isEmpty() ) {
            lore.add(active);
        } else {
            lore.set(0, active);
        }
        meta.setLore(lore);
        tome.setItemMeta(meta);
    }

    /**
     * Return next spell
     * @pre isSpellTome(tome)
     * @returns null if tome is empty, otherwise the next spell on the list after active
     */
    public Spell getNextSpell(ItemStack tome) {
        List<Spell> spells = getSpells(tome);
        Spell active = getActiveSpell(tome);
        for (int i = 0; i < spells.size(); i++) {
            if (spells.get(i).equals(active))
                return spells.get((i+1) % spells.size());
        }
        return null;
    }

    private final Spell[] spells;
    private static final String PLAYER_KNOWLEDGE_FILE = "playerknowledge.dat";
    private Map<UUID, Set<String>> learntSpells;
    
}