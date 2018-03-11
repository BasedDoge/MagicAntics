package net.undergroundantics.magicantics.plugin;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.undergroundantics.magicantics.spells.Spell;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;

public class SpellSelecting implements Listener {

    private MagicAntics plugin;
    
    private static final String SPELL_INVENTORY_TITLE = "Equip Spells";
    private static final ItemStack BARRIER_SLOT = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)15);

    public SpellSelecting(MagicAntics plugin) {
        this.plugin = plugin;
    }
 
    private boolean isSpellInventory(Inventory inv) {
        return inv != null && inv.getTitle().equals(SPELL_INVENTORY_TITLE);
    }

    private boolean isSpellEquipSlot(int i) {
        return i >= 3*9;
    }
    
    private boolean isSpellLibrarySlot(int i) {
        return i < 2*9;
    }

    private Spell spellFromSpellBook(ItemStack book) {
        return plugin.getSpellFromDisplayName(book.getItemMeta().getLore().get(0));
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onCloseSpellTome(InventoryCloseEvent e) {
        if (isSpellInventory(e.getInventory())) {
            List<Spell> spells = new LinkedList<>();
            Set<String> seen = new TreeSet<>();
            for (int i = 3*9; i < 4*9; i++) {
                ItemStack item = e.getInventory().getItem(i);
                if ( ItemRules.isSpellBook(item) ) {
                    Spell spell = spellFromSpellBook(item);
                    if ( ! seen.contains(spell.getName()) ) {
                        spells.add(spell);
                        seen.add(spell.getName());
                    }
                }
            }
            setSpells(e.getPlayer().getItemInHand(), spells);
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
        if (e.getPlayer().getOpenInventory() != null && isSpellInventory(e.getPlayer().getOpenInventory().getTopInventory())) {
            e.setCancelled(true);
        }
    }
            

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryDrag(InventoryDragEvent e) {
        if (isSpellInventory(e.getInventory())) {
            if ( e.getInventorySlots().size() == 1 ) {
                int slot = e.getInventorySlots().iterator().next();
                ItemStack book = e.getNewItems().get(slot);
                if ( ! isSpellEquipSlot(slot) ) {
                    e.setCancelled(true);
                }
            } else {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEditSpellTome(InventoryClickEvent e) {
        if (isSpellInventory(e.getInventory())) {
            if (isSpellInventory(e.getClickedInventory())) {
                Inventory spellInv = e.getClickedInventory();
                switch (e.getAction()) {
                    case PICKUP_ALL:
                    case PICKUP_HALF:
                    case PICKUP_SOME:
                    case PICKUP_ONE:
                        if ( isSpellEquipSlot(e.getSlot()) ) {
                            
                        } else if ( e.getCurrentItem().getType() == Material.BOOK ) {
                            e.setCursor(e.getCurrentItem());
                        } else {
                            e.setCancelled(true);
                        }
                        break;
                    case SWAP_WITH_CURSOR:
                        if ( ! isSpellEquipSlot(e.getSlot())) {
                            e.setCancelled(true);
                        }
                        break;
                    case PLACE_ALL:
                    case PLACE_SOME:
                    case PLACE_ONE:
                        plugin.getLogger().log(Level.SEVERE, "place: " + e.getAction().toString());
                        if ( ! isSpellEquipSlot(e.getSlot()) ) {
                            e.setCurrentItem(null);
                            e.setCancelled(false);
                        }
                        break;
                    case NOTHING:
                        break;
                    default:
                        e.setCancelled(true);
                        break;
                }
            } else if (e.getClickedInventory() == null && e.getAction() == InventoryAction.DROP_ALL_CURSOR) {
                e.setCursor(null);
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
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
                Inventory inv = Bukkit.createInventory(null, 36, SPELL_INVENTORY_TITLE);
                inv.setMaxStackSize(1);
                {
                    int i = 0;
                    for (Spell spell : plugin.getLearntSpells(p) ) {
                        inv.setItem(i, ItemRules.createSpellBook(spell));
                        i++;
                    }
                    while (i < 3*9) {
                        inv.setItem(i, BARRIER_SLOT);
                        i++;
                    }
                    for (Spell spell : getSpells(e.getPlayer().getItemInHand())) {
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
                Spell active = getNextSpell(tome);
                if (active != null) {
                    setActiveSpell(tome, active);
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

    
    /**
     * Set the spells on a tome, changing the active spell only if necessary
     * @pre isSpellTome(tome)
     */
    private void setSpells(ItemStack tome, List<Spell> spells) {
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
    private List<Spell> getSpells(ItemStack tome) {
        List<Spell> spells = new LinkedList<Spell>();
        List<String> lines = tome.getItemMeta().getLore();
        if ( lines == null )
            return spells;
        for (String line : lines.subList(1, lines.size())) {
            spells.add(plugin.getSpellFromDisplayName(line));
        }
        return spells;
    }

    /**
     * Return the active spell on tome
     * @pre isSpellTome(tome)
     */
    private Spell getActiveSpell(ItemStack tome) {
        List<String> lore = tome.getItemMeta().getLore();
        if ( lore == null )
            return null;
        return plugin.getSpellFromDisplayName(lore.get(0).split("#")[1]);
    }

    /**
     * Changes the active spell of tome to spell
     * @pre isSpellTome(tome)
     * @pre spell != null
     */
    private void setActiveSpell(ItemStack tome, Spell spell) {
        ItemMeta meta = tome.getItemMeta();
        List<String> lore = meta.getLore();
        String active = ChatColor.DARK_GRAY + "Active#" + spell.getDisplayName();
        if ( lore == null ) {
            lore = new LinkedList<>();
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
    private Spell getNextSpell(ItemStack tome) {
        List<Spell> spells = getSpells(tome);
        Spell active = getActiveSpell(tome);
        for (int i = 0; i < spells.size(); i++) {
            if (spells.get(i).equals(active))
                return spells.get((i+1) % spells.size());
        }
        return null;
    }

}
