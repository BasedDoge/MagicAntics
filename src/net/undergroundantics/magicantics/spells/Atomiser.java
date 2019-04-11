package net.undergroundantics.magicantics.spells;

import net.undergroundantics.magicantics.plugin.Spell;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;


public class Atomiser implements Spell {

    private static final String NAME = "Atomiser";
    private static final String DISPLAY_NAME = ChatColor.LIGHT_PURPLE + NAME;
    private static final long COOLDOWN = 6;

    private static final Material[] FORBIDDEN_TYPES = 
        { Material.BEDROCK, Material.BARRIER, Material.END_PORTAL_FRAME,
          Material.COMMAND_BLOCK, Material.CHAIN_COMMAND_BLOCK, Material.REPEATING_COMMAND_BLOCK,
          Material.STRUCTURE_BLOCK, Material.STRUCTURE_VOID
        };

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

    private boolean isForbidden(Material m1) {
        for ( Material m2 : FORBIDDEN_TYPES ) {
            if ( m1 == m2 )
                return true;
        }
        return false;
    }

    @Override
    public boolean cast(Player p) {
        Block block = p.getTargetBlock(null, 64);

        double dist = p.getLocation().distance(block.getLocation());
        double h = ((block.getLocation().getY()) - (p.getLocation().getY()) ) / dist;
        
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 0.5f, 2.0f);
        for (int i = 0; i <= dist; i++) {
            Location loc = p.getLocation().add(p.getLocation().getDirection().setY(h).normalize().multiply(i));
            p.getWorld().spawnParticle(Particle.REDSTONE, loc, 0, -0.3, 0, 0.5, 1);
        }
       
        boolean broken = false;

        if ( ! isForbidden(block.getType()) ) { 
            BlockBreakEvent event = new BlockBreakEvent(block, p);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if ( ! event.isCancelled() ) {
                block.breakNaturally(null);
                broken = true;
            }
        }

        if ( broken ) {      
            p.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, block.getLocation(), 5, 1, 1, 1, 0.1);
        } else {
            p.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, block.getLocation(), 5, 0.5, 0.5, 0.5, 0.1);
        }
        return broken;
    }
}
