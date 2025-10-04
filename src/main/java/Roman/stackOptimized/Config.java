package Roman.stackOptimized;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class Config {
    private final ConfigManager configManager;
    private final ConfigLoader configLoader;
    private final ConfigSaver configSaver;
    private final GroupedItemManager groupedItemManager;

    public Config(Plugin plugin) {
        this.configManager = new ConfigManager(plugin);
        this.configLoader = new ConfigLoader(configManager);
        this.configSaver = new ConfigSaver(configManager);
        this.groupedItemManager = new GroupedItemManager();
    }

    public boolean GetCanPrint() {
        return configManager.getCanPrint();
    }

    public ConfigResult LoadItemsConfig(boolean canPrint) {
        return configLoader.loadItemsConfig();
    }

    public List<GroupedItem> getGroupedItems(Map<Material, Integer> items, Map<ItemGroup, Integer> groups) {
        return groupedItemManager.getGroupedItems(items, groups);
    }

    public Map<ItemGroup, Integer> getActiveGroups(ConfigResult configResult) {
        return configResult.getGroups();
    }

    public List<Material> GetGroupMaterials(String groupName) {
        return groupedItemManager.getGroupMaterials(groupName);
    }

    public boolean canStackBuckets(Map<Material, Integer> items) {
        return configSaver.canStackBuckets(items);
    }

    public void SaveGroupStackSize(String groupName, int newSize) {
        configSaver.saveGroupStackSize(groupName, newSize);
    }

    public void saveSingleItem(Material material, int maxStackSize) {
        configSaver.saveSingleItem(material, maxStackSize);
    }

    public void removeSingleItem(Material material) {
        configSaver.removeSingleItem(material);
    }
}