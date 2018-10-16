package de.raidcraft.events;

import com.google.common.base.Strings;
import de.raidcraft.api.BasePlugin;
import de.raidcraft.api.action.ActionAPI;
import de.raidcraft.api.config.Comment;
import de.raidcraft.api.config.ConfigLoader;
import de.raidcraft.api.config.ConfigurationBase;
import de.raidcraft.api.config.Setting;
import de.raidcraft.api.quests.Quests;
import de.raidcraft.events.api.ConfiguredEventTemplate;
import de.raidcraft.events.api.EventListener;
import de.raidcraft.events.api.GlobalEventListener;
import de.raidcraft.events.api.PlayerEventListener;
import de.raidcraft.events.art.EventTrigger;
import de.raidcraft.events.tables.TGlobalEvent;
import de.raidcraft.events.tables.TPlayerEvent;
import de.raidcraft.util.ConfigUtil;
import de.raidcraft.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.util.*;


public class RCEventsPlugin extends BasePlugin implements Listener {

    private final Map<String, ConfiguredEventTemplate> loadedEventTemplates = new HashMap<>();
    private final Map<String, EventListener> activeGlobalListeners = new HashMap<>();
    private final Map<UUID, Map<String, EventListener>> activePlayerListeners = new HashMap<>();

    @Override
    public void enable() {

        Quests.registerQuestLoader(new ConfigLoader(this, "event") {
            @Override
            public void loadConfig(String id, ConfigurationSection config) {
                registerEventTemplate(id, config);
            }
        });

        ActionAPI.register(this).global()
                .trigger(new EventTrigger());

        load();
    }

    private void load() {
        ConfigUtil.loadRecursiveConfigs(this, "events", new ConfigLoader(this) {
            @Override
            public void loadConfig(String id, ConfigurationSection config) {
                registerEventTemplate(id, config);
            }
        });

        registerEvents(this);
    }

    @Override
    public void disable() {
        unregisterEvents(this);

        activeGlobalListeners.values().forEach(EventListener::unregisterListener);
        activeGlobalListeners.clear();

        activePlayerListeners.values().forEach(eventListeners -> eventListeners.values().forEach(EventListener::unregisterListener));
        activePlayerListeners.clear();

        loadedEventTemplates.clear();
    }

    @Override
    public void reload() {
        disable();
        load();
    }

    public void registerEventTemplate(String id, ConfigurationSection config) {
        if (Strings.isNullOrEmpty(id) || config == null) return;

        if (loadedEventTemplates.containsKey(id)) {
            getLogger().warning("Cannot register duplicate event template with id: " + id);
            return;
        }
        ConfiguredEventTemplate template = new ConfiguredEventTemplate(id, config);
        loadedEventTemplates.put(id, template);
        getLogger().info("Registered event template: " + id);

        if (template.isGlobal()) {
            enableGlobalListener(template);
        } else {
            Bukkit.getOnlinePlayers().forEach(player -> enablePlayerListener(player, template));
        }
    }

    public void enableGlobalListener(ConfiguredEventTemplate template) {
        if (template == null || !template.isGlobal()) return;

        if (activeGlobalListeners.containsKey(template.getIdentifier())) {
            getLogger().warning("Cannot activate global template " + template.getIdentifier() + " twice!");
            return;
        }

        GlobalEventListener listener = new GlobalEventListener(template);
        listener.registerListener();
        activeGlobalListeners.put(template.getIdentifier(), listener);
        debug(listener, "activated");
    }

    public void enablePlayerListener(Player player, ConfiguredEventTemplate template) {
        if (template == null || template.isGlobal() || player == null) return;

        if (!activePlayerListeners.containsKey(player.getUniqueId())) {
            activePlayerListeners.put(player.getUniqueId(), new HashMap<>());
        }

        PlayerEventListener listener = new PlayerEventListener(template, player);
        if (activePlayerListeners.get(player.getUniqueId()).containsValue(listener)) {
            getLogger().warning("Cannot activate player template " + template.getIdentifier() + " for " + player.getName() + " twice!");
            return;
        }

        listener.registerListener();
        activePlayerListeners.get(player.getUniqueId()).put(listener.getListenerId(), listener);
        debug(listener, "activated for " + player.getName());
    }

    public void debug(EventListener listener, String message) {
        getLogger().info("[DEBUG][" + (listener.isGlobal() ? "GLOBAL" : "PLAYER") + "][" + listener.getListenerId() + "]: " + message);
    }

    public void unregisterPlayer(Player player) {
        Map<String, EventListener> listenerMap = activePlayerListeners.remove(player.getUniqueId());
        if (listenerMap == null) return;
        listenerMap.values().forEach(listener -> {
            listener.unregisterListener();
            debug(listener, "unregistered for " + player.getName());
        });
    }

    public void registerPlayer(Player player) {
        loadedEventTemplates.values().forEach(template -> enablePlayerListener(player, template));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {

        registerPlayer(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {

        unregisterPlayer(event.getPlayer());
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        ArrayList<Class<?>> tables = new ArrayList<>();
        tables.add(TGlobalEvent.class);
        tables.add(TPlayerEvent.class);
        return tables;
    }

    public class LocalConfiguration extends ConfigurationBase<RCEventsPlugin> {

        @Setting("debug")
        @Comment("Activates debug messages for events.")
        public boolean debug = false;

        public LocalConfiguration(RCEventsPlugin plugin) {
            super(plugin, "config.yml");
        }
    }

}
