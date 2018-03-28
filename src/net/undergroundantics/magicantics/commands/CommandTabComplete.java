package net.undergroundantics.magicantics.commands;

import java.util.LinkedList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import net.undergroundantics.magicantics.plugin.MagicAntics;
import net.undergroundantics.magicantics.plugin.Spell;
import org.bukkit.entity.Player;

public class CommandTabComplete implements TabCompleter {

    private MagicAntics plugin;
    
    public CommandTabComplete(MagicAntics plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String string, String[] args) {
        if (cmd.getName().equalsIgnoreCase("spellscroll") || cmd.getName().equalsIgnoreCase("spellbook")) {
            if ( args.length == 1 ) {
                return spellNames(args[0]);
            } else if ( args.length == 2 ) {
                return playerNames(args[1]);
            }
            List<String> spells = new LinkedList<>();
        } else if (cmd.getName().equalsIgnoreCase("spelltome")) {
            if ( args.length == 1 ) {
                return playerNames(args[0]);
            }
            
        }
        return null;        
    }

    private List<String> spellNames(String prefix) {
        List<String> spells = new LinkedList<>();
        for (Spell spell : plugin.getLearnableSpells())
            if (spell.getName().toLowerCase().startsWith(prefix.toLowerCase())){
                spells.add(spell.getName());
            }
        return spells;
    }
    
    private List<String> playerNames(String prefix) {
        List<String> players = new LinkedList<>();
        for (Player player : plugin.getServer().getOnlinePlayers() )
            if (player.getName().toLowerCase().startsWith(prefix.toLowerCase())){
                players.add(player.getName());
            }
        return players;
    }

}
