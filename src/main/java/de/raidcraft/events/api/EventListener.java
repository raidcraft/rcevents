package de.raidcraft.events.api;

import de.raidcraft.api.action.action.ActionHolder;
import de.raidcraft.api.action.requirement.Requirement;
import de.raidcraft.api.action.requirement.RequirementHolder;
import de.raidcraft.api.action.trigger.TriggerListener;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

public interface EventListener extends TriggerListener<Player> {

    @Override
    default Class<Player> getTriggerEntityType() {
        return Player.class;
    }

    boolean isGlobal();

    long getExecutionCount();

    Optional<Instant> getLastActivation();

    void registerListener();

    void unregisterListener();

    void load();

    void save();
}
