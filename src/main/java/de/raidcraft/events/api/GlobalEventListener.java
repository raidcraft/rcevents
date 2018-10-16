package de.raidcraft.events.api;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.action.trigger.TriggerListener;
import de.raidcraft.api.action.trigger.TriggerListenerConfigWrapper;
import de.raidcraft.events.RCEventsPlugin;
import de.raidcraft.events.tables.TGlobalEvent;
import de.raidcraft.events.tables.TPlayerEvent;
import io.ebean.EbeanServer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.Optional;

@Data
@EqualsAndHashCode(callSuper = true)
public class GlobalEventListener extends AbstractEventListener {

    public GlobalEventListener(ConfiguredEventTemplate template) {
        super(template);
    }

    @Override
    public void load() {

        TGlobalEvent event = RaidCraft.getDatabase(RCEventsPlugin.class)
                .find(TGlobalEvent.class)
                .where().eq("event", getTemplate().getIdentifier())
                .findOne();

        if (event == null) return;

        setExecutionCount(event.getExecutionCount());
        setLastActivation(event.getLastActivation());
    }

    @Override
    public void save() {

        EbeanServer database = RaidCraft.getDatabase(RCEventsPlugin.class);
        TGlobalEvent event = database
                .find(TGlobalEvent.class)
                .where().eq("event", getTemplate().getIdentifier())
                .findOne();

        if (event == null) {
            event = new TGlobalEvent();
            event.setEvent(getTemplate().getIdentifier());
        }

        event.setExecutionCount(getExecutionCount());
        getLastActivation().ifPresent(event::setLastActivation);

        database.save(event);
    }
}
