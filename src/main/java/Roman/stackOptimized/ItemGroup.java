package Roman.stackOptimized;

import org.bukkit.Material;
import java.util.*;

public enum ItemGroup {
    SHULKERS("Skulkers", Arrays.asList(
            Material.SHULKER_BOX,
            Material.WHITE_SHULKER_BOX, Material.GRAY_SHULKER_BOX, Material.ORANGE_SHULKER_BOX,
            Material.MAGENTA_SHULKER_BOX, Material.LIGHT_BLUE_SHULKER_BOX, Material.YELLOW_SHULKER_BOX,
            Material.LIME_SHULKER_BOX, Material.PINK_SHULKER_BOX, Material.GRAY_SHULKER_BOX,
            Material.LIGHT_GRAY_SHULKER_BOX,
            Material.CYAN_SHULKER_BOX, Material.PURPLE_SHULKER_BOX, Material.BLUE_SHULKER_BOX,
            Material.BROWN_SHULKER_BOX,
            Material.GREEN_SHULKER_BOX, Material.RED_SHULKER_BOX, Material.BLACK_SHULKER_BOX)),

    BEDS("Beds", Arrays.asList(
            Material.GREEN_BED, Material.GRAY_BED, Material.CYAN_BED, Material.BROWN_BED,
            Material.BLUE_BED, Material.BLACK_BED, Material.LIGHT_BLUE_BED, Material.LIGHT_GRAY_BED,
            Material.LIME_BED, Material.MAGENTA_BED, Material.ORANGE_BED, Material.PINK_BED,
            Material.PURPLE_BED, Material.RED_BED, Material.WHITE_BED, Material.YELLOW_BED)),

    POTIONS("Potions", Arrays.asList(
            Material.POTION, Material.SPLASH_POTION, Material.LINGERING_POTION)),

    ENCHANTED_BOOKS("Enchanted_Books", Arrays.asList(
            Material.ENCHANTED_BOOK)),

    MINECARTS("Minecarts", Arrays.asList(
            Material.CHEST_MINECART, Material.FURNACE_MINECART, Material.HOPPER_MINECART, Material.MINECART,
            Material.TNT_MINECART)),

    BOATS("Boats", Arrays.asList(
            Material.OAK_BOAT, Material.OAK_CHEST_BOAT, Material.SPRUCE_BOAT, Material.SPRUCE_CHEST_BOAT,
            Material.BIRCH_BOAT, Material.BIRCH_CHEST_BOAT, Material.JUNGLE_BOAT, Material.JUNGLE_CHEST_BOAT,
            Material.ACACIA_BOAT, Material.ACACIA_CHEST_BOAT, Material.CHERRY_BOAT, Material.CHERRY_CHEST_BOAT,
            Material.DARK_OAK_BOAT, Material.DARK_OAK_CHEST_BOAT, Material.MANGROVE_BOAT, Material.MANGROVE_CHEST_BOAT,
            Material.BAMBOO_RAFT, Material.BAMBOO_CHEST_RAFT)),

    BANNERS("Banners", Arrays.asList(
            Material.WHITE_BANNER, Material.ORANGE_BANNER, Material.MAGENTA_BANNER, Material.LIGHT_BLUE_BANNER,
            Material.YELLOW_BANNER, Material.LIME_BANNER, Material.PINK_BANNER, Material.GRAY_BANNER,
            Material.LIGHT_GRAY_BANNER, Material.CYAN_BANNER, Material.PURPLE_BANNER, Material.BLUE_BANNER,
            Material.BROWN_BANNER, Material.GREEN_BANNER, Material.RED_BANNER, Material.BLACK_BANNER)),

    MUSIC_DISC("Music_Disc", Arrays.asList(
            Material.MUSIC_DISC_13, Material.MUSIC_DISC_CAT, Material.MUSIC_DISC_BLOCKS, Material.MUSIC_DISC_CHIRP,
            Material.MUSIC_DISC_CREATOR, Material.MUSIC_DISC_CREATOR_MUSIC_BOX, Material.MUSIC_DISC_FAR,
            Material.MUSIC_DISC_MALL,
            Material.MUSIC_DISC_MELLOHI, Material.MUSIC_DISC_STAL, Material.MUSIC_DISC_STRAD, Material.MUSIC_DISC_WARD,
            Material.MUSIC_DISC_11, Material.MUSIC_DISC_WAIT, Material.MUSIC_DISC_OTHERSIDE, Material.MUSIC_DISC_RELIC,
            Material.MUSIC_DISC_5, Material.MUSIC_DISC_PIGSTEP, Material.MUSIC_DISC_PRECIPICE)),

    BUCKETS("Buckets", Arrays.asList(
            Material.BUCKET, Material.WATER_BUCKET, Material.LAVA_BUCKET, Material.AXOLOTL_BUCKET,
            Material.COD_BUCKET, Material.MILK_BUCKET, Material.TROPICAL_FISH_BUCKET, Material.POWDER_SNOW_BUCKET,
            Material.PUFFERFISH_BUCKET, Material.TADPOLE_BUCKET, Material.SALMON_BUCKET));

    private final String configName;
    private final List<Material> materials;

    ItemGroup(String configName, List<Material> materials) {
        this.configName = configName;
        this.materials = materials;
    }

    public String getConfigName() {
        return configName;
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public static ItemGroup fromConfigName(String configName) {
        for (ItemGroup group : values()) {
            if (group.configName.equals(configName)) {
                return group;
            }
        }
        return null;
    }

    public static ItemGroup[] getAllGroups() {
        return values();
    }
}
