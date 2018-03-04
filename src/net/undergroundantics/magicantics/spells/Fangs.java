package net.undergroundantics.magicantics.spells;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;

public class Fangs implements Spell {

    private static final String NAME = "Fangs";
    private static final String DISPLAY_NAME = "&7Fangs";
    private static final long COOLDOWN = 5;

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
        for (int i = 1; i < 9; i++) {
            Location l = p.getLocation().add(p.getLocation().getDirection().setY(0).normalize().multiply(i));
            l.setY(l.getWorld().getHighestBlockAt(l).getY());
            if (l.getY() >= p.getLocation().getY() - 4 && l.getY() <= p.getLocation().getY() + 4) {
                p.getWorld().spawnEntity(l, EntityType.EVOKER_FANGS);
            }
        }
        return COOLDOWN;
    }

    @Override
    public void onHit(ProjectileHitEvent e) {
    }

}
