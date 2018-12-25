package de.raidcraft.events.api;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.action.trigger.TriggerListenerConfigWrapper;
import de.raidcraft.events.EventExecutedEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.Optional;

@Data
@EqualsAndHashCode(of = {"template"})
public abstract class AbstractEventListener implements EventListener {

    private final ConfiguredEventTemplate template;
    private long executionCount;
    private Instant lastActivation;

    public Optional<Instant> getLastActivation() {
        return Optional.ofNullable(lastActivation);
    }

    @Override
    public boolean isGlobal() {
        return getTemplate().isGlobal();
    }

    @Override
    public String getListenerId() {
        return getTemplate().getIdentifier();
    }

    public void registerListener() {
        load();
        getTemplate().getTriggerFactories().forEach(triggerFactory -> triggerFactory.registerListener(this));
    }

    public void unregisterListener() {
        getTemplate().getTriggerFactories().forEach(triggerFactory -> triggerFactory.unregisterListener(this));
        save();
    }

    @Override
    public boolean processTrigger(Player player, TriggerListenerConfigWrapper trigger) {
        if (!getTemplate().isEnabled()) return false;
        if (!getTemplate().getWorlds().contains(player.getWorld().getName())) return false;

        if (getTemplate().getCooldown() > 0 && getLastActivation()
                .map(instant -> instant.plusMillis(getTemplate().getCooldown()))
                .map(instant -> instant.isAfter(Instant.now()))
                .orElse(false)) {
            // do not trigger if the cooldown has not expired
            return false;
        }

        if (!getTemplate().getRequirements(Player.class).stream()
                .allMatch(requirement -> requirement.test(player))) {
            return false;
        }

        setLastActivation(Instant.now());
        setExecutionCount(getExecutionCount() + 1);
        save();

        if (getTemplate().getExecutionCount() > 0 && getExecutionCount() < getTemplate().getExecutionCount()) {
            return false;
        }

        getTemplate().getActions(Player.class).forEach(action -> action.accept(player));

        RaidCraft.callEvent(new EventExecutedEvent(player, this));
        return true;
    }

    public ConfiguredEventTemplate getTemplate() {
        return this.template;
    }

    public long getExecutionCount() {
        return this.executionCount;
    }

    public void setExecutionCount(long executionCount) {
        this.executionCount = executionCount;
    }

    public void setLastActivation(Instant lastActivation) {
        this.lastActivation = lastActivation;
    }
}
