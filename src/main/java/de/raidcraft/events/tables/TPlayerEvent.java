package de.raidcraft.events.tables;

import de.raidcraft.api.ebean.BaseModel;
import lombok.Data;

import java.time.Instant;

@Data
public class TPlayerEvent extends BaseModel {

    private String playerId;
    private String player;
    private String event;
    private Instant lastActivation;
}
