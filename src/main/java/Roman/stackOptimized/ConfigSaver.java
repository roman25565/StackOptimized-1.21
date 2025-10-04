package Roman.stackOptimized;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class ConfigSaver {
    private final ConfigManager configManager;

    public ConfigSaver(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public void saveGroupStackSize(String groupName, int newSize) {
        ConfigurationSection groupsSection = configManager.getGroupsSection();
        if (newSize == 0) {
            groupsSection.set(groupName, "default");
            return;
        }
        groupsSection.set(groupName, newSize);
    }

    public void saveSingleItem(Material material, int maxStackSize) {
        ConfigurationSection itemsSection = configManager.getItemsSection();
        itemsSection.set(material.toString(), maxStackSize);
    }

    public void removeSingleItem(Material material) {
        ConfigurationSection itemsSection = configManager.getItemsSection();
        itemsSection.set(material.toString(), null);
    }

    public boolean canStackBuckets(Map<Material, Integer> items) {
        ItemGroup bucketsGroup = ItemGroup.BUCKETS;
        for (Material material : bucketsGroup.getMaterials()) {
            if (!items.containsKey(material)) {
                return false;
            }
        }
        return true;
    }
}
