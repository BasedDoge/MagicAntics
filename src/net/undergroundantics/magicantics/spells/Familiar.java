package net.undergroundantics.magicantics.spells;

import net.md_5.bungee.api.ChatColor;
import net.undergroundantics.magicantics.plugin.MagicAntics;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Particle;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;


public class Familiar implements Spell{

    private static final String NAME = "Familiar";
    private static final String DISPLAY_NAME = "&aFamiliar";
    private static final long COOLDOWN = 45;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    public Familiar(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public long cast(Player p) {
        Egg dogEgg = p.launchProjectile(Egg.class);
        dogEgg.setShooter(p);
        dogEgg.setVelocity(p.getLocation().getDirection().multiply(3));
        dogEgg.setMetadata(NAME, new FixedMetadataValue(plugin, MagicAntics.NAME));
        return COOLDOWN;
    }

    @Override
    public void onHit(ProjectileHitEvent e) {
        Wolf wolf = (Wolf)e.getEntity().getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.WOLF);
        wolf.setOwner((Player)e.getEntity().getShooter());
        wolf.setCustomName(ChatColor.GREEN + "Wolf Familiar");
        wolf.setCollarColor(DyeColor.GREEN);
        
        Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                    public void run() {
                        wolf.getWorld().spawnParticle(Particle.CLOUD, wolf.getLocation(), 10, 0.2, 0.2, 0.2, 0.2);
                        wolf.remove();
                    }
                }, 500);
        
        if (e.getHitEntity() instanceof LivingEntity){
            wolf.setAngry(true);
            wolf.setTarget((LivingEntity) e.getHitEntity());
        }
    }
    
    private final Plugin plugin;
}
