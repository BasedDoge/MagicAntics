package net.undergroundantics.magicantics.commands;

import net.undergroundantics.magicantics.plugin.ItemRules;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MagicAnticsCommandExecutor implements CommandExecutor{

    ItemRules MAIR = new ItemRules();
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        boolean doCmd = false;

        if (cmd.getName().equalsIgnoreCase("NewSpell")) {
            if (args.length == 1) {
                if(sender instanceof Player){
                    Player p = (Player) sender;
                    p.getInventory().addItem(MAIR.newSpellSheet(args[0]));
                doCmd = true;
                }
            }
        }
        return doCmd;
    }
}