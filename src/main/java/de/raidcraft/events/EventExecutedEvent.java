package de.raidcraft.events;

import de.raidcraft.events.api.EventListener;
import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Data
public class EventExecutedEvent extends Event  {

    private final Player player;
    private final EventListener event;

    //<-- Handler -->//
    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {

        return handlers;
    }

    public static HandlerList getHandlerList() {

        return handlers;
    }
}
