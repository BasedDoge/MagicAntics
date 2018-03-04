package net.undergroundantics.magicantics.spells;

import net.undergroundantics.magicantics.plugin.MagicAntics;
import org.bukkit.Bukkit;
import static org.bukkit.Bukkit.getServer;
import org.bukkit.EntityEffect;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ProjectileEffects implements Listener {
    
    Plugin plugin;
    
    @EventHandler
    public void onHit(ProjectileHitEvent e) {
        try{
            plugin = MagicAntics.getInstance();
        }catch(Exception ex){
            getServer().getConsoleSender().sendMessage(ex.getMessage());
        }
        
        if (e.getEntity().hasMetadata("IceBolt")) {
            Entity p = e.getEntity();
            p.getWorld().spawnParticle(Particle.SNOW_SHOVEL, p.getLocation(), 10, 0.2, 0.2, 0.2, 0.2);
            p.getWorld().playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 0.5f, 2.0f);
            if (e.getHitEntity() instanceof LivingEntity) {
                PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 60, 1, true);
                slow.apply((LivingEntity) e.getHitEntity());
            }
        }

        if (e.getEntity().hasMetadata("Stasis")) {
            Entity p = e.getEntity();
            p.getWorld().spawnParticle(Particle.END_ROD, p.getLocation(), 10, 1, 1, 1, 0);
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 0.5f, 2.0f);
            if (e.getHitEntity() instanceof LivingEntity) {
                PotionEffect lev = new PotionEffect(PotionEffectType.LEVITATION, 1, 1, true);
                for (int i = 0; i < 100; i++){
                    Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                        public void run() {
                            lev.apply((LivingEntity) e.getHitEntity());
                            e.getHitEntity().playEffect(EntityEffect.HURT);
                        }
                    }, 1);
                }
            }
        }
    }
}
