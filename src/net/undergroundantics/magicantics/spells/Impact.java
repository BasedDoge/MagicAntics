package net.undergroundantics.magicantics.spells;

import net.undergroundantics.magicantics.plugin.Spell;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Impact implements Spell{

    private static final String NAME = "Imapact";
    private static final String DISPLAY_NAME = ChatColor.YELLOW + NAME;
    private static final long COOLDOWN = 18;
    
    @Override
    public boolean cast(Player p) {
        boolean castSuccess = true;
        boolean isGround = false;
        int height = 0;
        while(isGround == false){
            if (p.getLocation().subtract(0, height, 0).getBlock().getType() == Material.AIR && height < 255){
                height++;
            }
            else{
                isGround = true;
                p.sendMessage("height:" + height);
                p.setFallDistance(-((height/5) * 4));
                p.setVelocity(new Vector(0, -height, 0));
            }
        }
        return castSuccess;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public long getCooldown() {
        return COOLDOWN;
    }

    @Override
    public boolean isLearnable() {
        return true;
    }
    
}
