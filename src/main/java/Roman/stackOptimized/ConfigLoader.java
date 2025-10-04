package Roman.stackOptimized;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class ConfigLoader {
    private final ConfigManager configManager;

    public ConfigLoader(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public ConfigResult loadItemsConfig() {
        Map<Material, Integer> items = new HashMap<>();
        Map<ItemGroup, Integer> groups = new HashMap<>();
        boolean canPrint = configManager.getCanPrint();

        loadGroups(items, groups, canPrint);
        loadIndividualItems(items, canPrint);

        return new ConfigResult(items, groups);
    }

    private void loadGroups(Map<Material, Integer> items, Map<ItemGroup, Integer> groups, boolean canPrint) {
        if (canPrint) {
            System.out.println(ConfigManager.ANSI_GREEN + "Add Materials from Group Materials");
        }

        ConfigurationSection groupsSection = configManager.getGroupsSection();

        for (ItemGroup group : ItemGroup.getAllGroups()) {
            loadGroup(groupsSection, group, items, groups, canPrint);
        }
    }

    private void loadGroup(ConfigurationSection groupsSection, ItemGroup group,
            Map<Material, Integer> items, Map<ItemGroup, Integer> groups, boolean canPrint) {
        Object value = groupsSection.get(group.getConfigName());

        var groupSkipped = false;
        if (value == null) {
            if (canPrint) {
                System.out.println(ConfigManager.ANSI_RED + group.getConfigName() + " Group not found");
            }
            return;
        }

        if ("default".equals(value.toString().toLowerCase())) {
            groupSkipped = true;
            if (canPrint) {
                System.out.println(ConfigManager.ANSI_GREEN + group.getConfigName() + ": Group Skipped");
            }
        }

        int maxStackSize = 0;
        if (!groupSkipped) {
            maxStackSize = parseGroupStackSize(value, group.getConfigName());
            if (maxStackSize <= 0) {
                return;
            }
        }

        groups.put(group, maxStackSize);

        if (!groupSkipped) {
            printGroupHeader(group.getConfigName(), canPrint);
            addGroupMaterials(group, maxStackSize, items, canPrint);
        }
    }

    private int parseGroupStackSize(Object value, String groupName) {
        try {
            int maxStackSize = Integer.parseInt(value.toString());
            if (maxStackSize <= 0 || maxStackSize > 99) {
                System.out.println(ConfigManager.ANSI_RED + groupName +
                        " Group not loaded value must be between 0 and 99 or false");
                return -1;
            }
            return maxStackSize;
        } catch (NumberFormatException e) {
            System.out.println(ConfigManager.ANSI_RED + groupName +
                    " Group not loaded value must be between 0 and 99 or false");
            return -1;
        }
    }

    private void printGroupHeader(String groupName, boolean canPrint) {
        if (canPrint) {
            System.out.println(ConfigManager.ANSI_GREEN + groupName + " Group:" + ConfigManager.ANSI_RESET);
            System.out.println(ConfigManager.ANSI_RESET + "+------------------------+----------------+-------+");
            System.out.println(ConfigManager.ANSI_RESET + "| Material               | Max Stack Size |Status |");
            System.out.println(ConfigManager.ANSI_RESET + "+------------------------+----------------+-------+");
        }
    }

    private void addGroupMaterials(ItemGroup group, int maxStackSize,
            Map<Material, Integer> items, boolean canPrint) {
        for (Material material : group.getMaterials()) {
            if (canPrint) {
                System.out.printf(ConfigManager.ANSI_RESET + "[StackOptimized] " +
                        "| %-22s | %-14d |   %3s   | \n",
                        material.toString(),
                        maxStackSize,
                        ConfigManager.ANSI_GREEN + "+" + ConfigManager.ANSI_RESET);
            }
            items.put(material, maxStackSize);
        }

        if (canPrint) {
            System.out.println(
                    ConfigManager.ANSI_RESET + "[StackOptimized] +------------------------+----------------+-------+");
        }
    }

    private void loadIndividualItems(Map<Material, Integer> items, boolean canPrint) {
        if (canPrint) {
            printIndividualItemsHeader();
        }

        ConfigurationSection itemsSection = configManager.getItemsSection();

        for (String materialName : itemsSection.getKeys(false)) {
            Object value = itemsSection.get(materialName);
            if (value == null) {
                continue;
            }

            loadIndividualItem(materialName, value, items, canPrint);
        }

        if (canPrint) {
            System.out.println("[StackOptimized] +------------------------+----------------+-------+");
        }
    }

    private void printIndividualItemsHeader() {
        System.out
                .println(ConfigManager.ANSI_GREEN + "[StackOptimized] Додавання предметів:" + ConfigManager.ANSI_RESET);
        System.out.println(ConfigManager.ANSI_RESET
                + "[StackOptimized] ╔════════════════════════════════╦════════════════════╦════════╗");
        System.out.println(ConfigManager.ANSI_RESET
                + "[StackOptimized] ║      Матеріал                 ║  Макс. стак       ║ Статус ║");
        System.out.println(ConfigManager.ANSI_RESET
                + "[StackOptimized] ╠════════════════════════════════╬════════════════════╬════════╣");
    }

    private void loadIndividualItem(String materialName, Object value,
            Map<Material, Integer> items, boolean canPrint) {
        Material material = Material.matchMaterial(materialName);
        int maxStackSize = 0;
        boolean loadedSuccessfully = true;

        try {
            maxStackSize = Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            loadedSuccessfully = false;
            System.out.println(
                    "\u001B[31mMaxStackSize value format is incorrect with " + materialName + " material\u001B[0m");
        }

        if (material == null) {
            loadedSuccessfully = false;
            System.out.println("\u001B[31mIncorrect Material " + materialName + " Material\u001B[0m");
        }

        if (maxStackSize <= 0 || maxStackSize > 99) {
            loadedSuccessfully = false;
            System.out.println("\u001B[31mIncorrect MaxStackSize " + materialName + " Material\u001B[0m");
        }

        String materialString = loadedSuccessfully ? materialName : materialName;

        if (canPrint) {
            System.out.printf(ConfigManager.ANSI_RESET + "[StackOptimized] " + "| %-22s | %-14d |   %3s   | \n",
                    materialString,
                    maxStackSize,
                    loadedSuccessfully ? ConfigManager.ANSI_GREEN + "+" + ConfigManager.ANSI_RESET
                            : ConfigManager.ANSI_RED + "-" + ConfigManager.ANSI_RESET);
        }

        if (loadedSuccessfully) {
            items.put(material, maxStackSize);
        }
    }
}
