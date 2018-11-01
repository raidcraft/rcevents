package de.raidcraft.events.tables;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.ebean.BaseModel;
import de.raidcraft.events.RCEventsPlugin;
import io.ebean.EbeanServer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "rc_events_players")
@Entity
public class TPlayerEvent extends BaseModel {

    private String event;
    private long executionCount;
    private Instant lastActivation;
    private UUID playerId;
    private String player;

    @Override
    protected EbeanServer database() {
        return RaidCraft.getDatabase(RCEventsPlugin.class);
    }
}
