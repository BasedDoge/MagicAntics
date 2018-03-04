package net.undergroundantics.magicantics.spells;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;

public interface Spell {

    public long cast(Player p);
    public void onHit(ProjectileHitEvent e);
    public String getName();
    public String getDisplayName();

}
