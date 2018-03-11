package net.undergroundantics.magicantics.spells;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;

public class Atomiser implements Spell {

    private static final String NAME = "Atomiser";
    private static final String DISPLAY_NAME = ChatColor.LIGHT_PURPLE + NAME;
    private static final long COOLDOWN = 15;

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
    public boolean cast(Player p) {
        boolean castSuccess = false;
        Block tBlock = p.getTargetBlock(null, 64);
        double dist = p.getLocation().distance(tBlock.getLocation());
        double h = ((tBlock.getLocation().getY()) - (p.getLocation().getY()) ) / dist;
        
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 0.5f, 2.0f);
        for (int i = 0; i <= dist; i++) {
            Location loc = p.getLocation().add(p.getLocation().getDirection().setY(h).normalize().multiply(i));
            p.getWorld().spawnParticle(Particle.REDSTONE, loc, 0, -0.3, 0, 0.5, 1);
        }
        
        if (tBlock.getState() instanceof Container || tBlock.getType() == Material.BEDROCK) {
            p.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, tBlock.getLocation(), 5, 0.5, 0.5, 0.5, 0.1);
        }
        else{
            p.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, tBlock.getLocation(), 5, 1, 1, 1, 0.1);
            tBlock.breakNaturally(null);
            castSuccess = true;
        }
        return castSuccess;
    }
}
