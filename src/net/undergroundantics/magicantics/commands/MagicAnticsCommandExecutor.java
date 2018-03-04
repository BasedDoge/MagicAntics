package net.undergroundantics.magicantics.commands;

import net.undergroundantics.magicantics.spells.Spell;
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
        boolean doCmd = false;

        if (cmd.getName().equalsIgnoreCase("NewSpell")) {
            if (args.length == 1 && sender instanceof Player) {
                Spell spell = plugin.getSpell(args[0]);
                if (spell != null) {
                    Player p = (Player) sender;
                    p.getInventory().addItem(ItemRules.newSpellSheet(spell));
                    doCmd = true;
                } // else Invalid spell name
            }
        }
        return doCmd;
    }

    private final MagicAntics plugin;
}