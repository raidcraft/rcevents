package de.raidcraft.events.api;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.action.trigger.TriggerListenerConfigWrapper;
import de.raidcraft.events.RCEventsPlugin;
import de.raidcraft.events.tables.TPlayerEvent;
import io.ebean.EbeanServer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.Optional;

@Data
@EqualsAndHashCode(callSuper = true, of = {"player"})
public class PlayerEventListener extends AbstractEventListener {

    private final Player player;

    public PlayerEventListener(ConfiguredEventTemplate template, Player player) {
        super(template);
        this.player = player;
    }

    @Override
    public Optional<Player> getEntity() {
        return Optional.ofNullable(getPlayer());
    }

    @Override
    public void load() {

        TPlayerEvent playerEvent = RaidCraft.getDatabase(RCEventsPlugin.class)
                .find(TPlayerEvent.class)
                .where().eq("player_id", getPlayer().getUniqueId())
                .and().eq("event", getTemplate().getIdentifier())
                .findOne();

        if (playerEvent == null) return;

        setExecutionCount(playerEvent.getExecutionCount());
        setLastActivation(playerEvent.getLastActivation());
    }

    @Override
    public void save() {

        EbeanServer database = RaidCraft.getDatabase(RCEventsPlugin.class);
        TPlayerEvent playerEvent = database
                .find(TPlayerEvent.class)
                .where().eq("player_id", getPlayer().getUniqueId())
                .and().eq("event", getTemplate().getIdentifier())
                .findOne();
        if (playerEvent == null) {
            playerEvent = new TPlayerEvent();
            playerEvent.setEvent(getTemplate().getIdentifier());
            playerEvent.setPlayer(getPlayer().getName());
            playerEvent.setPlayerId(getPlayer().getUniqueId());
        }
        playerEvent.setExecutionCount(getExecutionCount());
        getLastActivation().ifPresent(playerEvent::setLastActivation);

        database.save(playerEvent);
    }
}
