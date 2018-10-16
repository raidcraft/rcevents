package de.raidcraft.events.api;

import de.raidcraft.api.action.trigger.TriggerListenerConfigWrapper;
import lombok.Data;
import org.bukkit.entity.Player;

import java.util.Optional;

@Data
public class PlayerEventListener implements Event {

    private final ConfiguredEventTemplate template;
    private final Player player;

    @Override
    public Optional<Player> getEntity() {
        return Optional.ofNullable(getPlayer());
    }

    @Override
    public boolean processTrigger(Player player, TriggerListenerConfigWrapper trigger) {
        return getTemplate().isEnabled();
    }
}
