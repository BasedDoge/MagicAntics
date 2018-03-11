package net.undergroundantics.magicantics.spells;

import net.undergroundantics.magicantics.plugin.Spell;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vex;
import org.bukkit.plugin.Plugin;

public class Vindication implements Spell {

    private static final String NAME = "Vindication";
    private static final String DISPLAY_NAME = ChatColor.DARK_AQUA + NAME;
    private static final long COOLDOWN = 30;

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

    public Vindication(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean cast(Player p) {
        boolean castSuccess = false;
        List<LivingEntity> localMobsVindication = new LinkedList();
        for (Entity ent : p.getNearbyEntities(10, 5, 10)) {
            if (ent instanceof LivingEntity) {
                localMobsVindication.add((LivingEntity) ent);
            }
        }
        if (localMobsVindication.size() > 0) {
            castSuccess = true;
            for (int i = 0; i < 6; i++) {
                Random rand = new Random();
                int n = rand.nextInt(localMobsVindication.size());
                Vex vex = (Vex) p.getWorld().spawnEntity(p.getLocation(), EntityType.VEX);
                
                Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
                    vex.setTarget(localMobsVindication.get(n));
                }, 20);
                
                Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
                    p.getWorld().spawnParticle(Particle.END_ROD, p.getLocation(), 1, 0.1, 0.1, 0.1, 0);
                    vex.remove();
                }, 180);
            }
        }
        return castSuccess;
    }

    private final Plugin plugin;
}
