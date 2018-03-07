package net.undergroundantics.magicantics.spells;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;

public class Atomiser implements Spell {

    private static final String NAME = "Atomiser";
    private static final String DISPLAY_NAME = "&dAtomiser";
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
    public long cast(Player p) {
        Block tBlock = p.getTargetBlock(null, 64);
        double dist = p.getLocation().distance(tBlock.getLocation());
        double h = ((tBlock.getLocation().getY()) - (p.getLocation().getY()) ) / dist;
        
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 0.5f, 2.0f);
        for (int i = 0; i <= dist; i++) {
            Location loc = p.getLocation().add(p.getLocation().getDirection().setY(h).normalize().multiply(i));
            p.getWorld().spawnParticle(Particle.REDSTONE, loc, 0, -0.3, 0, 0.5, 1);
            //to create coloured particles count must be 0
            //and the 3 following varibales should be between 0
            //and 1, the first of which automatically is increaed by 1 when spawned
        }
        
        if (tBlock.getState() instanceof Container || tBlock.getType() == Material.BEDROCK) {
            p.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, tBlock.getLocation(), 5, 0.5, 0.5, 0.5, 0.1);
        }
        else{
            p.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, tBlock.getLocation(), 5, 1, 1, 1, 0.1);
            tBlock.breakNaturally(null);
        }
        return COOLDOWN;
    }

    @Override
    public void onHit(ProjectileHitEvent e) {
    }
}
