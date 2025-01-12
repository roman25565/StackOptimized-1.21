package Roman.stackOptimized;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class Config {
    public static final String ANSI_RESET = "\u001B[0m";

    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED   = "\u001B[31m";

    FileConfiguration config;


    public Config(Plugin plugin) {
        config = plugin.getConfig();
    }

    public boolean GetCanPrint() {
        ConfigurationSection settings = config.getConfigurationSection("Settings");
        if (settings == null) {
            settings = config.createSection("Groups");
        }
        Object value = settings.get("Enable_Print_Console");
        if(!(settings.contains("Enable_Print_Console"))) {
            settings.set("Enable_Print_Console", true);
            return true;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }else {
            settings.set("Enable_Print_Console", true);
            return true;
        }
    }

    public Map<Material, Integer> GetItemsConfig(boolean CanPrint) {
        var items = new HashMap<Material, Integer>();

        AddGroups(CanPrint, items);
        AddItems(CanPrint, items);

        return  items;
    }

    private void AddGroups(boolean CanPrint, HashMap<Material, Integer> items){
        if (CanPrint) {
            System.out.println(ANSI_GREEN + "Add Materials from Group Materiadls");
        }

        ConfigurationSection GroupsSection = config.getConfigurationSection("Groups");
        if (GroupsSection == null) {
            GroupsSection = config.createSection("Groups");
        }
        AddGroup(CanPrint, GroupsSection, "Skulkers", items);
        AddGroup(CanPrint, GroupsSection, "Beds", items);
        AddGroup(CanPrint, GroupsSection, "Potions", items);
        AddGroup(CanPrint, GroupsSection, "Enchanted_Books", items);
        AddGroup(CanPrint, GroupsSection, "Minecarts", items);
        AddGroup(CanPrint, GroupsSection, "Boats", items);
        AddGroup(CanPrint, GroupsSection, "Banners", items);
        AddGroup(CanPrint, GroupsSection, "Music_Disc", items);
        AddGroup(CanPrint, GroupsSection, "Buckets", items);
    }

    private void AddGroup(boolean canPrint, ConfigurationSection groupSection, String groupName, HashMap<Material, Integer> items) {
        var value = groupSection.get(groupName);
        if (value == "default" || value == "Default") {
            if (canPrint) {
                System.out.println(ANSI_GREEN + groupName + ": Group Skipped");
            }
            return;
        }
        if (value == null) {
            if (canPrint) {
                System.out.println(ANSI_RED + groupName + " Group not found");
            }
            return;
        }

        int maxStackSize = 0;

        try {
            maxStackSize = Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            System.out.println(ANSI_RED + groupName + " Group not loaded value must be between 0 and 100 or false");
            return;
        }
        if (maxStackSize <= 0 || maxStackSize > 100) {
            System.out.println(ANSI_RED + groupName + " Group not loaded value must be between 0 and 100 or false");
            return;
        }

        if (canPrint) {
            System.out.println("[StackOptimized] "+ ANSI_GREEN + groupName + " Group:" + ANSI_RESET);
            System.out.println(ANSI_RESET +"[StackOptimized] +------------------------+----------------+-------+");
            System.out.println(ANSI_RESET +"[StackOptimized] | Material               | Max Stack Size |Status |");
            System.out.println(ANSI_RESET +"[StackOptimized] +------------------------+----------------+-------+");
        }
        List<Material> materials = GetGroupMaterials(groupName);

        for (Material material : materials) {
            if (canPrint) {
                System.out.printf(ANSI_RESET + "[StackOptimized] " + "| %-22s | %-14d |   %3s   | \n",
                        material.toString(),
                        maxStackSize,
                        ANSI_GREEN + "+" + ANSI_RESET);
            }
            items.put(material, maxStackSize);
        }
        if (canPrint) {
        System.out.println(ANSI_RESET +"[StackOptimized] +------------------------+----------------+-------+");
        }
    }

    public List<Material> GetGroupMaterials(String GroupName) {
        return switch (GroupName) {
            case "Skulkers" -> new ArrayList<>(Arrays.asList(
                    Material.SHULKER_BOX ,
                    Material.WHITE_SHULKER_BOX, Material.GRAY_SHULKER_BOX, Material.ORANGE_SHULKER_BOX,
                    Material.MAGENTA_SHULKER_BOX, Material.LIGHT_BLUE_SHULKER_BOX, Material.YELLOW_SHULKER_BOX,
                    Material.LIME_SHULKER_BOX, Material.PINK_SHULKER_BOX, Material.GRAY_SHULKER_BOX, Material.LIGHT_GRAY_SHULKER_BOX,
                    Material.CYAN_SHULKER_BOX, Material.PURPLE_SHULKER_BOX, Material.BLUE_SHULKER_BOX, Material.BROWN_SHULKER_BOX,
                    Material.GREEN_SHULKER_BOX, Material.RED_SHULKER_BOX, Material.BLACK_SHULKER_BOX));
            case "Beds" -> new ArrayList<>(Arrays.asList(
                    Material.GREEN_BED, Material.GRAY_BED, Material.CYAN_BED, Material.BROWN_BED,
                    Material.BLUE_BED, Material.BLACK_BED, Material.LIGHT_BLUE_BED, Material.LIGHT_GRAY_BED,
                    Material.LIME_BED, Material.MAGENTA_BED, Material.ORANGE_BED, Material.PINK_BED,
                    Material.PURPLE_BED, Material.RED_BED, Material.WHITE_BED, Material.YELLOW_BED));

            case "Potions" -> new ArrayList<>(List.of(Material.POTION, Material.SPLASH_POTION, Material.LINGERING_POTION));

            case "Enchanted_Books" -> new ArrayList<>(
                    List.of(Material.ENCHANTED_BOOK));
            case "Minecarts" -> new ArrayList<>(List.of(
                    Material.CHEST_MINECART, Material.FURNACE_MINECART, Material.HOPPER_MINECART, Material.MINECART, Material.TNT_MINECART));
            case "Boats" -> new ArrayList<>(
                    List.of(Material.OAK_BOAT, Material.OAK_CHEST_BOAT, Material.SPRUCE_BOAT, Material.SPRUCE_CHEST_BOAT,
                            Material.BIRCH_BOAT, Material.BIRCH_CHEST_BOAT, Material.JUNGLE_BOAT, Material.JUNGLE_CHEST_BOAT,
                            Material.ACACIA_BOAT, Material.ACACIA_CHEST_BOAT, Material.CHERRY_BOAT, Material.CHERRY_CHEST_BOAT,
                            Material.DARK_OAK_BOAT, Material.DARK_OAK_CHEST_BOAT, Material.MANGROVE_BOAT, Material.MANGROVE_CHEST_BOAT,
                            Material.BAMBOO_RAFT , Material.BAMBOO_CHEST_RAFT));
            case "Banners" -> new ArrayList<>(
                    List.of(Material.WHITE_BANNER, Material.ORANGE_BANNER, Material.MAGENTA_BANNER , Material.LIGHT_BLUE_BANNER ,
                            Material.YELLOW_BANNER, Material.LIME_BANNER, Material.PINK_BANNER, Material.GRAY_BANNER,
                            Material.LIGHT_GRAY_BANNER, Material.CYAN_BANNER, Material.PURPLE_BANNER, Material.BLUE_BANNER,
                            Material.BROWN_BANNER, Material.GREEN_BANNER, Material.RED_BANNER, Material.BLACK_BANNER));
            case "Music_Disc" -> new ArrayList<>(
                    List.of(Material.MUSIC_DISC_13, Material.MUSIC_DISC_CAT, Material.MUSIC_DISC_BLOCKS, Material.MUSIC_DISC_CHIRP,
                            Material.MUSIC_DISC_CREATOR, Material.MUSIC_DISC_CREATOR_MUSIC_BOX, Material.MUSIC_DISC_FAR, Material.MUSIC_DISC_MALL,
                            Material.MUSIC_DISC_MELLOHI, Material.MUSIC_DISC_STAL, Material.MUSIC_DISC_STRAD, Material.MUSIC_DISC_WARD,
                            Material.MUSIC_DISC_11, Material.MUSIC_DISC_WAIT, Material.MUSIC_DISC_OTHERSIDE, Material.MUSIC_DISC_RELIC,
                            Material.MUSIC_DISC_5, Material.MUSIC_DISC_PIGSTEP, Material.MUSIC_DISC_PRECIPICE));
            case "Buckets" -> new ArrayList<>(
                    List.of(Material.BUCKET,Material.WATER_BUCKET,Material.LAVA_BUCKET,Material.AXOLOTL_BUCKET,Material.COD_BUCKET,Material.MILK_BUCKET,
                            Material.TROPICAL_FISH_BUCKET,Material.POWDER_SNOW_BUCKET,Material.PUFFERFISH_BUCKET,Material.TADPOLE_BUCKET,Material.SALMON_BUCKET
            ));
            default -> new ArrayList<>();
        };
    }

    private void AddItems(boolean CanPrint, HashMap<Material, Integer> items) {
        if (CanPrint) {
            System.out.println(ANSI_GREEN + "[StackOptimized] Add Items:" + ANSI_RESET);
            System.out.println(ANSI_RESET + "[StackOptimized] +------------------------+----------------+-------+");
            System.out.println(ANSI_RESET + "[StackOptimized] | Material               | Max Stack Size |Status |");
            System.out.println(ANSI_RESET + "[StackOptimized] +------------------------+----------------+-------+");
        }

        ConfigurationSection itemsSection = config.getConfigurationSection("Items");
        if (itemsSection == null) {
            itemsSection = config.createSection("Items");
            return;
        }
        for (String key : itemsSection.getKeys(false)) {
            if (config.getConfigurationSection("Items." + key) == null) {
                continue;
            }
            ConfigurationSection itemData = config.getConfigurationSection("Items." + key);
            int maxStackSize = 0;
            boolean LoadedSuccessfully = true;

            Material material = Material.matchMaterial(itemData.get("Material").toString());

            try {
                maxStackSize = Integer.parseInt(itemData.get("MaxStackSize").toString());
            } catch (NumberFormatException e) {
                LoadedSuccessfully = false;
                System.out.println("\u001B[31mMaxStackSize value format is incorrect with " + Material.matchMaterial(itemData.get("Material").toString() + " material\u001B[0m"));
            }
            if (material == null) {
                LoadedSuccessfully = false;
                System.out.println("\u001B[31mIncorrect Material " + itemData.get("Material").toString() + " Material\u001B[0m");
            }
            if (maxStackSize <= 0 || maxStackSize > 100) {
                LoadedSuccessfully = false;
                System.out.println("\u001B[31mIncorrect MaxStackSize " + itemData.get("Material").toString() + " Material\u001B[0m");
            }

            String materialString = LoadedSuccessfully ? material.toString() : itemData.get("Material").toString();
            if (CanPrint) {
                System.out.printf(ANSI_RESET + "[StackOptimized] " + "| %-22s | %-14d |   %3s   | \n",
                        materialString,
                        maxStackSize,
                        LoadedSuccessfully ? ANSI_GREEN + "+" + ANSI_RESET : ANSI_RED + "-" + ANSI_RESET);
            }
            if (LoadedSuccessfully) {
                items.put(material, maxStackSize);
            }
        }

        if (CanPrint) {
            System.out.println("[StackOptimized] +------------------------+----------------+-------+");
        }
    }

    public Map<Material, Integer> AddData(Map<Material, Integer> items, Material material, int maxStackSize) {
        items.putIfAbsent(material, maxStackSize);
        int size = items.get(material);
        if (size != maxStackSize) {
            items.put(material, maxStackSize);
        }

        return items;
    }

    public boolean Save(Map<Material, Integer> items) {
        ClearFromGroups(items);
        List<Map.Entry<Material, Integer>> itemsList = AlphabeticalSorting(items);

        ConfigurationSection itemsSection = config.getConfigurationSection("Items");
        if (itemsSection == null) {
            itemsSection = config.createSection("Items");
        }

        for (int i = 0; i < itemsList.size(); i++) {
            ConfigurationSection itemSection = itemsSection.getConfigurationSection(Integer.toString(i));
            if (itemSection == null) {
                itemSection = config.createSection("Items." + i);
            }

            itemSection.set("Material", itemsList.get(i).getKey().toString());
            itemSection.set("MaxStackSize", itemsList.get(i).getValue());
        }

        return true;
    }
    public List<Map.Entry<Material, Integer>> AlphabeticalSorting(Map<Material, Integer> items) {
        List<Map.Entry<Material, Integer>> list = new ArrayList<>(items.entrySet());

        list.sort(Comparator.comparing(entry -> entry.getKey().toString()));

        return list;
    }

    public void ClearFromGroups(Map<Material, Integer> items) {
        List<Material> materialsToClear = new ArrayList<>();
        ConfigurationSection GroupsSection = config.getConfigurationSection("Groups");
        ClearFromGroup(items, GroupsSection, materialsToClear, "Skulkers");
        ClearFromGroup(items, GroupsSection, materialsToClear, "Beds");
        ClearFromGroup(items, GroupsSection, materialsToClear, "Potions");
        ClearFromGroup(items, GroupsSection, materialsToClear, "Enchanted_Books");
        ClearFromGroup(items, GroupsSection, materialsToClear, "Minecarts");
        ClearFromGroup(items, GroupsSection, materialsToClear, "Boats");
        ClearFromGroup(items, GroupsSection, materialsToClear, "Banners");
        ClearFromGroup(items, GroupsSection, materialsToClear, "Music_Disc");

        for (Material material : materialsToClear) {
            items.remove(material);
        }
    }

    private void ClearFromGroup(Map<Material, Integer> items, ConfigurationSection GroupsSection, List<Material> materialsToClear, String groupName) {
        List<Material> materials = GetGroupMaterials(groupName);
        var groupValue = GroupsSection.get(groupName);
        if (groupValue == "default" || groupValue == "Default" || groupValue == null) {return;}

        try {
            int maxStackSize = Integer.parseInt(groupValue.toString());
            for (Material material : materials) {
                if (items.get(material) == null) {return;}
                int value = items.get(material);

                if (maxStackSize == value) {
                    materialsToClear.add(material);
                }
            }
        } catch (NumberFormatException e) {
        }
    }

}
