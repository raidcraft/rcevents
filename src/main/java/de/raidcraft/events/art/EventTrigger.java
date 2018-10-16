package de.raidcraft.events.art;

import de.raidcraft.api.action.trigger.Trigger;
import de.raidcraft.events.EventExecutedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EventTrigger extends Trigger implements Listener {

    public EventTrigger() {
        super("event");
    }

    @Information(
            value = "event",
            desc = "Gets triggered when an event has been executed.",
            conf = {
                    "event: id of the event"
            }
    )
    @EventHandler
    public void onEventExecuted(EventExecutedEvent event) {

        informListeners(event.getPlayer(), config -> config.getString("event").equalsIgnoreCase(event.getEvent().getListenerId()));
    }
}
