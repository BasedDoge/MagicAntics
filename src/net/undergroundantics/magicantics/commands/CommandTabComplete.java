package net.undergroundantics.magicantics.commands;

import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class CommandTabComplete implements TabCompleter {

    public static String[] spells = new String[]{"&cFireball", "&bIcicle", "&eThunderstorm", "&7Fangs", "&4Inferno", "&eStasis"};

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String string, String[] args) {
        List<String> list = null;
        if (cmd.getName().equalsIgnoreCase("NewSpell")) {
            list = Arrays.asList(spells);
        }
        return list;
    }
}
