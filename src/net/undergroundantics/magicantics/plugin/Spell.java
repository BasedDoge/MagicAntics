package net.undergroundantics.magicantics.plugin;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;

public interface Spell {

    public boolean cast(Player p);
    default public void onHit(ProjectileHitEvent e) {}
    public String getName();
    public String getDisplayName();
    public long getCooldown();
    public boolean isLearnable();

}
