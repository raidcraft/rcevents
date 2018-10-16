package de.raidcraft.events.api;

import de.raidcraft.api.action.action.ActionHolder;
import de.raidcraft.api.action.requirement.Requirement;
import de.raidcraft.api.action.requirement.RequirementHolder;
import de.raidcraft.api.action.trigger.TriggerListener;
import org.bukkit.entity.Player;

import java.util.Collection;

public interface Event extends TriggerListener<Player> {

    @Override
    default Class<Player> getTriggerEntityType() {
        return Player.class;
    }
}
