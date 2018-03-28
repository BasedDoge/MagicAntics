package net.undergroundantics.magicantics.commands;

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
        }
        
        return false;
    }

    private final MagicAntics plugin;
}