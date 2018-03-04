package net.undergroundantics.magicantics.spells;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vex;
import org.bukkit.event.entity.ProjectileHitEvent;

public class Vindication implements Spell{
    
    private static final String NAME = "Vindiction";
    private static final String DISPLAY_NAME = "&3Vindiction";
    private static final long COOLDOWN = 30;

    @Override
    public long cast(Player p) {
        List<LivingEntity> entList = new LinkedList();
        for (Entity ent : p.getNearbyEntities(10, 5, 10)){
            if (ent instanceof LivingEntity)
            {
                entList.add((LivingEntity)ent);
            }
        }
        for (int i = 0; i < 3; i++){
            Random rand = new Random();
            int n = rand.nextInt(entList.size());
            Vex vex = (Vex)p.getWorld().spawnEntity(p.getLocation(), EntityType.VEX);
            vex.setTarget(entList.get(n));
        }
        return COOLDOWN;
    }

    @Override
    public void onHit(ProjectileHitEvent e) {
        
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }  
}
