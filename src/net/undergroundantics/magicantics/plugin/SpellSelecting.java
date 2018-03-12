package net.undergroundantics.magicantics.plugin;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

public class SpellSelecting implements Listener {

    private MagicAntics plugin;
    
    private static final String SPELL_INVENTORY_TITLE = "Equip Spells";
    private static final int ROW_SIZE = 9;
    private static final int NUM_EQUIP_ROWS = 1;
    private static final ItemStack BARRIER_SLOT;
    private static final ItemStack EMPTY_EQUIP_SLOT;
    private static final ItemStack UNKNOWN_SLOT;
    static {
        BARRIER_SLOT     = new ItemStack(Material.THIN_GLASS, 1);
        EMPTY_EQUIP_SLOT = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)13);
        UNKNOWN_SLOT     = new ItemStack(Material.BARRIER, 1);
        ItemMeta meta;
        meta = BARRIER_SLOT.getItemMeta();
        meta.setDisplayName(" ");
        BARRIER_SLOT.setItemMeta(meta);
        meta = EMPTY_EQUIP_SLOT.getItemMeta();
        meta.setDisplayName("Available Spell Slot");
        EMPTY_EQUIP_SLOT.setItemMeta(meta);
        meta = UNKNOWN_SLOT.getItemMeta();
        meta.setDisplayName("Unknown spell");
        UNKNOWN_SLOT.setItemMeta(meta);
    }
    private final int NUM_LEARNABLE_SPELLS;
    private final int NUM_LIBRARY_ROWS;

    public SpellSelecting(MagicAntics plugin) {
        this.plugin = plugin;
        NUM_LEARNABLE_SPELLS = plugin.getLearnableSpells().size();
        int n = NUM_LEARNABLE_SPELLS;
        int r = ROW_SIZE;
        NUM_LIBRARY_ROWS = (n % r == 0) ? n / r : 1 + (n / r);
    }
 
    private boolean isSpellInventory(Inventory inv) {
        return inv != null && inv.getTitle().equals(SPELL_INVENTORY_TITLE);
    }

    private boolean isSpellEquipSlot(int i) {
        return i >= (NUM_LIBRARY_ROWS + 1) * ROW_SIZE;
    }
    
    private boolean isSpellLibrarySlot(int i) {
        return i < NUM_LIBRARY_ROWS * ROW_SIZE;
    }

    private int equipSlotBegin() {
        return (NUM_LIBRARY_ROWS + 1) * ROW_SIZE;
    }
    
    private int equipSlotEnd() {
        return (NUM_LIBRARY_ROWS + 1 + NUM_EQUIP_ROWS) * ROW_SIZE;
    }

    private Spell spellFromSpellBook(ItemStack book) {
        return plugin.getSpellFromDisplayName(book.getItemMeta().getLore().get(0));
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onCloseSpellTome(InventoryCloseEvent e) {
        if (isSpellInventory(e.getInventory())) {
            List<Spell> spells = new LinkedList<>();
            Set<String> seen = new TreeSet<>();
            for (int i = equipSlotBegin(); i < equipSlotEnd(); i++) {
                ItemStack item = e.getInventory().getItem(i);
                if ( ItemRules.isSpellBook(item) ) {
                    Spell spell = spellFromSpellBook(item);
                    if ( ! seen.contains(spell.getName()) ) {
                        spells.add(spell);
                        seen.add(spell.getName());
                    }
                }
            }
            plugin.setSpells(e.getPlayer().getItemInHand(), spells);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEditSpellTome(InventoryClickEvent e) {
        Inventory inv = e.getView().getTopInventory();
        if ( isSpellInventory(inv) ) {
            e.setCancelled(true);
            if ( e.getClickedInventory() == inv ) {     
                if ( ItemRules.isSpellBook(e.getCurrentItem()) ) {
                    if ( isSpellEquipSlot(e.getSlot())) {
                        e.getInventory().setItem(e.getSlot(), null);
                    } else {
                        for (int i = equipSlotBegin(); i < equipSlotEnd(); i++) {
                            if (e.getInventory().getItem(i) == null) {
                                e.getInventory().setItem(i, e.getCurrentItem());
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onOpenSpellTome(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE) {
            Player p = e.getPlayer();
            ItemStack tome = p.getInventory().getItemInMainHand();
            if (ItemRules.isSpellTome(tome)) {
                e.setCancelled(true);
                Inventory inv = Bukkit.createInventory(null, (NUM_LIBRARY_ROWS + 1 + NUM_EQUIP_ROWS) * ROW_SIZE, SPELL_INVENTORY_TITLE);
                inv.setMaxStackSize(1);
                {
                    int i = 0;
                    for (Spell spell : plugin.getLearntSpells(p) ) {
                        inv.setItem(i, ItemRules.createSpellBook(spell));
                        i++;
                    }
                    while (i < NUM_LEARNABLE_SPELLS) {
                        inv.setItem(i, UNKNOWN_SLOT);
                        i++;
                    }
                    i = NUM_LIBRARY_ROWS * ROW_SIZE;
                    while (i < (NUM_LIBRARY_ROWS + 1) * ROW_SIZE) {
                        inv.setItem(i, BARRIER_SLOT);
                        i++;
                    }
                    for (Spell spell : plugin.getSpells(e.getPlayer().getItemInHand())) {
                        inv.setItem(i, ItemRules.createSpellBook(spell));
                        i++;
                    }
                }
                p.openInventory(inv);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChangeActiveSpell(PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            ItemStack tome = p.getInventory().getItemInMainHand();
            if (ItemRules.isSpellTome(tome)) {
                e.setCancelled(true);
                Spell active = plugin.getNextSpell(tome);
                if (active != null) {
                    plugin.setActiveSpell(tome, active);
                    p.sendTitle("", ChatColor.STRIKETHROUGH + "--"
                        + ChatColor.BOLD + ">"
                        + ChatColor.RESET + "  "
                        + ChatColor.ITALIC
                        + active.getDisplayName(), 10, 20, 10);
                    p.playSound(e.getPlayer().getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1f);
                }
            }
        }
    }

}
