package de.raidcraft.events.tables;

import de.raidcraft.api.ebean.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "rc_events_global")
@Entity
public class TGlobalEvent extends BaseModel {

    private String event;
    private long executionCount;
    private Instant lastActivation;
}
