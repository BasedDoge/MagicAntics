package net.undergroundantics.magicantics.commands;

import java.util.LinkedList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import net.undergroundantics.magicantics.plugin.MagicAntics;
import net.undergroundantics.magicantics.plugin.Spell;

public class CommandTabComplete implements TabCompleter {

    private MagicAntics plugin;
    
    public CommandTabComplete(MagicAntics plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String string, String[] args) {
        if (cmd.getName().equalsIgnoreCase("spellscroll")) {
            List<String> spells = new LinkedList<>();
            for (Spell spell : plugin.getSpells())
                if (spell.getName().toLowerCase().startsWith(args[0].toLowerCase())){
                    spells.add(spell.getName());
                }
            return spells;
        } else if (cmd.getName().equalsIgnoreCase("spellbook")) {
            List<String> spells = new LinkedList<>();
            for (Spell spell : plugin.getLearnableSpells())
                if (spell.getName().toLowerCase().startsWith(args[0].toLowerCase())){
                    spells.add(spell.getName());
                }
            return spells;
        }
        return null;        
    }


}
