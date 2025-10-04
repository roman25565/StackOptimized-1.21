package Roman.stackOptimized;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";

    public static final String GROUPS_SECTION = "Groups";
    public static final String ITEMS_SECTION = "Items";
    public static final String SETTINGS_SECTION = "Settings";
    public static final String ENABLE_PRINT_CONSOLE = "Enable_Print_Console";

    private final FileConfiguration config;

    public ConfigManager(Plugin plugin) {
        this.config = plugin.getConfig();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public ConfigurationSection getGroupsSection() {
        ConfigurationSection section = config.getConfigurationSection(GROUPS_SECTION);
        if (section == null) {
            section = config.createSection(GROUPS_SECTION);
        }
        return section;
    }

    public ConfigurationSection getItemsSection() {
        ConfigurationSection section = config.getConfigurationSection(ITEMS_SECTION);
        if (section == null) {
            section = config.createSection(ITEMS_SECTION);
        }
        return section;
    }

    public ConfigurationSection getSettingsSection() {
        ConfigurationSection section = config.getConfigurationSection(SETTINGS_SECTION);
        if (section == null) {
            section = config.createSection(SETTINGS_SECTION);
        }
        return section;
    }

    public boolean getCanPrint() {
        ConfigurationSection settings = getSettingsSection();
        Object value = settings.get(ENABLE_PRINT_CONSOLE);

        if (!settings.contains(ENABLE_PRINT_CONSOLE)) {
            settings.set(ENABLE_PRINT_CONSOLE, true);
            return true;
        }

        if (value instanceof Boolean) {
            return (Boolean) value;
        } else {
            settings.set(ENABLE_PRINT_CONSOLE, true);
            return true;
        }
    }
}
