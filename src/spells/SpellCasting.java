package spells;

import main.ItemRules;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

public class SpellCasting implements Listener {

    Plugin plugin;
    int cooldownTime = 5;
    ItemRules MAIR = new ItemRules();
    HashMap<UUID, HashMap<String, Long>> playerCooldown = new HashMap<>(); //String = player, hashmap = <spell name, time in milliseconds since last cast>

    @EventHandler(priority = EventPriority.HIGH)
    public void onCast(PlayerInteractEvent e) {
        if (MAIR.SpellTomeCheck(e.getPlayer().getInventory().getItemInMainHand())) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                e.setCancelled(true);

                Player p = e.getPlayer();
                long currentTime = System.currentTimeMillis() / 1000;
                String activeSpell = "";

                if (p.getInventory().getItemInMainHand().getItemMeta().getLore().size() > 1) {
                    if (p.getInventory().getItemInMainHand().getItemMeta().getLore().get(1).split("#").length > 0) {
                        activeSpell = ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getLore().get(0).split("#")[1]);
                    }
                }

                Long lastUseTime = (long) 0;
                
                //if the spell has been cast before (if a cooldown time exists)
                if(playerCooldown.containsKey(p.getUniqueId()) && playerCooldown.get(p.getUniqueId()).containsKey(activeSpell)){
                    lastUseTime = playerCooldown.get(p.getUniqueId()).get(activeSpell);
                }
                
                //make sure hashmap has an entry for the player to avoid NPEs
                    HashMap<String, Long> spellCooldown = new HashMap<>();
                    spellCooldown.put(activeSpell, lastUseTime);
                    playerCooldown.put(p.getUniqueId(), spellCooldown);
                    
                //if the player is not on cooldown
                if (currentTime - lastUseTime > cooldownTime) {
                    castSpell(activeSpell, p);
                    spellCooldown.put(activeSpell, currentTime);
                    playerCooldown.put(p.getUniqueId(), spellCooldown);
                }
                
                //if the player is still on cooldown
                else if (currentTime - lastUseTime < cooldownTime) {
                    p.sendMessage("your spells are on cooldown for " + (cooldownTime - (currentTime - lastUseTime)) + " more seconds");
                }

            }
        }
    }

    public void castSpell(String spell, Player p) {
        switch (spell) { //switch statement likely isn't the best way to store all spells maybe move them to another class

            case "":
                break;

            case "FireBall":

                SmallFireball fireblast = p.launchProjectile(SmallFireball.class);
                fireblast.setShooter(p);
                fireblast.setIsIncendiary(false);
                fireblast.setVelocity(p.getLocation().getDirection().multiply(2));
                p.getWorld().spawnParticle(Particle.LAVA, p.getEyeLocation().subtract(0, 0.3, 0), 3, 0.1, 0.1, 0.1, 0.1);
                break;

            case "Ice Bolt":
                Snowball iceBolt = p.launchProjectile(Snowball.class);
                iceBolt.setShooter(p);
                iceBolt.setMetadata("IceBolt", new FixedMetadataValue(plugin, "MagicAntics"));
                iceBolt.setVelocity(p.getLocation().getDirection().multiply(2));
                p.getWorld().spawnParticle(Particle.SNOW_SHOVEL, p.getEyeLocation().subtract(0, 0.3, 0), 3, 0.1, 0.1, 0.1, 0.1);
                break;

            case "Lightning Storm":
                List<Entity> localMobsStorm = p.getNearbyEntities(6, 2, 6);
                for (Entity mob : localMobsStorm) {
                    if (mob instanceof LivingEntity && p.hasLineOfSight(mob)) {
                        mob.getWorld().strikeLightning(mob.getLocation().add(0, 1, 0));
                    }
                }
                if (localMobsStorm.size() > 0) {
                    p.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, p.getEyeLocation(), 50, 4, 1, 4, 0);
                } else {
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SILVERFISH_DEATH, SoundCategory.PLAYERS, 0.5f, 2.0f);
                }
                break;

            case "Fangs":
                for (int i = 1; i < 9; i++) {
                    Location l = p.getLocation().add(p.getLocation().getDirection().setY(0).normalize().multiply(i));
                    l.setY(l.getWorld().getHighestBlockAt(l).getY());
                    if (l.getY() >= p.getLocation().getY() - 4 && l.getY() <= p.getLocation().getY() + 4) {
                        p.getWorld().spawnEntity(l, EntityType.EVOKER_FANGS);
                    }
                }
                break;

            case "Inferno":
                List<Entity> localMobsInferno = p.getNearbyEntities(6, 2, 6);
                for (Entity mob : localMobsInferno) {
                    if (mob instanceof LivingEntity && p.hasLineOfSight(mob)) {
                        mob.setFireTicks(20 * localMobsInferno.size());
                    }
                    p.getWorld().spawnParticle(Particle.FLAME, p.getEyeLocation(), 30, 4, 1, 4, 1);
                }
                break;
        }
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }
}
