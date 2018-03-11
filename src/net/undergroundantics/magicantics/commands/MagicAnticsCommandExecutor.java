package net.undergroundantics.magicantics.commands;

import net.undergroundantics.magicantics.plugin.*;
import net.undergroundantics.magicantics.spells.Spell;
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
        boolean doCmd = false;

        if (cmd.getName().equalsIgnoreCase("spellscroll")) {
            if (args.length == 1 && sender instanceof Player) {
                Spell spell = plugin.getSpellFromName(args[0]);
                if (spell != null) {
                    Player p = (Player) sender;
                    p.getInventory().addItem(ItemRules.createSpellScroll(spell));
                    doCmd = true;
                } // else Invalid spell name
            }
        } else if (cmd.getName().equalsIgnoreCase("spellbook")) {
            if (args.length == 1 && sender instanceof Player) {
                Spell spell = plugin.getSpellFromName(args[0]);
                if (spell != null) {
                    Player p = (Player) sender;
                    p.getInventory().addItem(ItemRules.createSpellBook(spell));
                    doCmd = true;
                } 
            }
        } else if (cmd.getName().equalsIgnoreCase("spelltome")) {
            if (args.length == 0 && sender instanceof Player) {
                Player p = (Player) sender;
                p.getInventory().addItem(ItemRules.createSpellTome());
                doCmd = true;
            }
        }
        return doCmd;
    }

    private final MagicAntics plugin;
}