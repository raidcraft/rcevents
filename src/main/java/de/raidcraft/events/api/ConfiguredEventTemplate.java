package de.raidcraft.events.api;

import de.raidcraft.api.action.ActionAPI;
import de.raidcraft.api.action.TriggerFactory;
import de.raidcraft.api.action.action.Action;
import de.raidcraft.api.action.action.ActionHolder;
import de.raidcraft.api.action.requirement.Requirement;
import de.raidcraft.api.action.requirement.RequirementHolder;
import de.raidcraft.api.action.trigger.TriggerListenerConfigWrapper;
import de.raidcraft.util.ConfigUtil;
import de.raidcraft.util.TimeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.eclipse.aether.util.ConfigUtils;

import java.util.Collection;

@Data
@EqualsAndHashCode(of = {"identifier"})
public class ConfiguredEventTemplate implements RequirementHolder, ActionHolder {

    private final String identifier;
    private final boolean enabled;
    private final long cooldown;
    private final boolean global;
    private final int executionCount;
    private final Collection<String> worlds;
    private final Collection<Requirement<?>> requirements;
    private final Collection<Action<?>> actions;
    private final Collection<TriggerFactory> triggerFactories;

    public ConfiguredEventTemplate(String identifier, ConfigurationSection config) {
        this.identifier = identifier;
        this.enabled = config.getBoolean("enabled", true);
        this.cooldown = TimeUtil.parseTimeAsMillis(config.getString("cooldown", "0"));
        this.global = config.getBoolean("global", false);
        this.executionCount = config.getInt("execution-count", 0);
        this.worlds = config.getStringList("worlds");
        this.requirements = ActionAPI.createRequirements(getIdentifier(), config.getConfigurationSection("requirements"));
        this.actions = ActionAPI.createActions(config.getConfigurationSection("actions"));
        this.triggerFactories = ActionAPI.createTrigger(config.getConfigurationSection("trigger"));
    }
}
