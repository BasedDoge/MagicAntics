package net.undergroundantics.magicantics.spells;

import net.undergroundantics.magicantics.plugin.Spell;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Nimbus implements Spell {

    private static final String NAME = "Nimbus";
    private static final String DISPLAY_NAME = ChatColor.AQUA + NAME;
    private static final long COOLDOWN = 12;
    private static final int CLOUD_RADIUS = 2;

    public Nimbus(Plugin plugin) {
        this.plugin = plugin;
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

    @Override
    public boolean cast(Player p) {
        p.setFallDistance(-1);
        Location cloudSeed = p.getLocation().subtract(CLOUD_RADIUS / 2, 1, CLOUD_RADIUS / 2);
         for (int z = -CLOUD_RADIUS; z <= CLOUD_RADIUS; z++) {
            for (int x = -CLOUD_RADIUS; x <= CLOUD_RADIUS; x++) {
                Location cloud = new Location(cloudSeed.getWorld(), p.getLocation().getX() + x, p.getLocation().getY() - 1, p.getLocation().getZ() + z);
                if ( Math.pow(cloud.getX() - p.getLocation().getX(), 2) + Math.pow(cloud.getZ() - p.getLocation().getZ(), 2) <= Math.pow(CLOUD_RADIUS, 2) ) {
                    if (cloud.getBlock().getType() == Material.AIR) {
                        cloud.getBlock().setType(Material.STAINED_GLASS);
                        Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
                            cloud.getBlock().setType(Material.AIR);
                            cloud.getWorld().spawnParticle(Particle.CLOUD, cloud, 4, 0.25, 0.25, 0.25, 0.2);
                        }, 60);
                    }
                }
            }
        }
        return true;
    }
    private final Plugin plugin;
}
