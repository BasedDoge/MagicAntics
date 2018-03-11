package net.undergroundantics.magicantics.commands;

import java.util.LinkedList;
import java.util.List;
import net.undergroundantics.magicantics.plugin.MagicAntics;
import net.undergroundantics.magicantics.spells.Spell;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class CommandTabComplete implements TabCompleter {

    public CommandTabComplete(MagicAntics plugin) {
        for (Spell spell : plugin.getSpells()) {
            spells.add(spell.getName());
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String string, String[] args) {
        List<String> list = null;
        if (cmd.getName().equalsIgnoreCase("spellscroll") || cmd.getName().equalsIgnoreCase("spellbook")) {
            list = spells;
        }
        return list;
    }

    private final List<String> spells = new LinkedList<>();

}
