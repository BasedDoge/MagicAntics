package net.undergroundantics.magicantics.commands;

import java.util.ArrayList;
import java.util.List;
import net.undergroundantics.magicantics.plugin.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MagicAnticsCommandExecutor implements CommandExecutor{

    public MagicAnticsCommandExecutor(MagicAntics plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("spellscroll")) {
            if (args.length == 1 && sender instanceof Player) {
                Spell spell = plugin.getSpellFromName(args[0]);
                if (spell != null) {
                    Player p = (Player) sender;
                    p.getInventory().addItem(ItemRules.createSpellScroll(spell));
                    return true;
                }
            } else if (args.length == 2) {
                Spell spell = plugin.getSpellFromName(args[0]);
                Player p = plugin.getServer().getPlayer(args[1]);
                if (spell != null && p != null && p.isOnline()) {
                    p.getInventory().addItem(ItemRules.createSpellScroll(spell));
                    return true;
                }
            }
        } else if (cmd.getName().equalsIgnoreCase("spellbook")) {
            if (args.length == 1 && sender instanceof Player) {
                Spell spell = plugin.getSpellFromName(args[0]);
                if (spell != null && spell.isLearnable()) {
                    Player p = (Player) sender;
                    p.getInventory().addItem(ItemRules.createSpellBook(spell));
                    return true;
                }
            } else if (args.length == 2) {
                Spell spell = plugin.getSpellFromName(args[0]);
                Player p = plugin.getServer().getPlayer(args[1]);
                if (spell != null && p != null && p.isOnline()) {
                    p.getInventory().addItem(ItemRules.createSpellBook(spell));
                    return true;
                }
            }
        } else if (cmd.getName().equalsIgnoreCase("spelltome")) {
            if (args.length == 0 && sender instanceof Player) {
                Player p = (Player) sender;
                p.getInventory().addItem(ItemRules.createSpellTome());
                return true;
            } else if (args.length == 1) {
                Player p = plugin.getServer().getPlayer(args[0]);
                if (p != null && p.isOnline()) {
                    p.getInventory().addItem(ItemRules.createSpellTome());
                    return true;
                }
            }
        } else if (cmd.getName().equalsIgnoreCase("spellknowledge")) {
            if (args.length >= 1) {
                Player p = plugin.getServer().getPlayer(args[0]);
                if (p != null) {
                    if (args.length == 1) {
                        // List player knowledge
                        String msg = p.getName() + " ";
                        List<Spell> spells = plugin.getLearntSpells(p);
                        if (spells.isEmpty()) {
                            msg += "does not know any spells";
                        } else {
                            msg += "knows " + Integer.toString(spells.size());
                            msg += (spells.size() == 1 ? " spell: " : " spells: ");
                            boolean first = true;
                            for (Spell spell : spells) {
                                if (first) {
                                    msg += spell.getName();
                                    first = false;
                                } else {
                                    msg += ", " + spell.getName();
                                }
                            }
                        }
                        plugin.sendMessage(sender, msg);
                        return true;
                    } else if (args.length == 2) {
                        if (args[1].equalsIgnoreCase("clear")) {
                            plugin.setLearntSpells(p, new ArrayList<>());
                            plugin.sendMessage(sender, "Cleared all learnt spells for " + p.getName());
                            return true;
                        }
                    } else if (args.length == 3) {
                        Spell spell = plugin.getSpellFromName(args[2]);
                        if (spell != null) {
                            if (args[1].equalsIgnoreCase("add")) {
                                plugin.learnSpell(p, spell);
                                plugin.sendMessage(sender, "Added spell " + spell.getName() + " for " + p.getName());
                                return true;
                            } else if (args[1].equalsIgnoreCase("remove")) {
                                plugin.unlearnSpell(p, spell);
                                plugin.sendMessage(sender, "Removed spell " + spell.getName() + " for " + p.getName());
                                return true;
                            }
                        }
                    }
                }
            }
        }
        
        return false;
    }

    private final MagicAntics plugin;
}