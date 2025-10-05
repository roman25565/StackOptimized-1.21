package Roman.stackOptimized;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class StackOptimized extends JavaPlugin {

    private Config config;
    private Events events;
    private Map<Material, Integer> _items = new HashMap<>();
    private Map<ItemGroup, Integer> _groups = new HashMap<>();

    public Map<Material, Integer> GetItems() {
        return _items;
    }

    public Map<ItemGroup, Integer> GetGroups() {
        return _groups;
    }

    public Config getStackConfig() {
        return config;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = new Config(this);
        events = new Events(this);
        boolean canPrint = config.GetCanPrint();
        ConfigResult configResult = config.LoadItemsConfig(canPrint);
        _items = configResult.getMaterials();
        _groups = configResult.getGroups();
        var itemsGui = new ItemsGui(this);
        var commands = new Commands(this, itemsGui);

        getCommand("setMaxStackSize").setExecutor(commands);
        getCommand("stackgui").setExecutor(commands);
        getCommand("name").setExecutor(commands);
        getCommand("resetstacks").setExecutor(commands);

        getServer().getPluginManager().registerEvents(events, this);
        getServer().getPluginManager().registerEvents(itemsGui, this);

        boolean stackBuckets = config.canStackBuckets(_items);
        if (stackBuckets) {
            getServer().getPluginManager().registerEvents(new InventoryEvents(stackBuckets, this), this);
        }
    }

    @Override
    public void onDisable() {
    }

    public void TryStack(ItemStack item) {
        Material material = item.getType();
        if (!_items.containsKey(material)) {
            return;
        }
        int maxStackSize = _items.get(material);
        if (item.getMaxStackSize() == maxStackSize) {return;}
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setMaxStackSize(maxStackSize);
        item.setItemMeta(itemMeta);
    }

    public boolean AddItem(Material material, int maxStackSize) {

        System.out.println("AddItem");
        // Перевіряємо, чи предмет вже існує
        if (_items.containsKey(material)) {
            return false; // Предмет вже існує
        }

        // Додаємо до внутрішньої Map
        _items.put(material, maxStackSize);

        // Зберігаємо в конфігурацію
        config.saveSingleItem(material, maxStackSize);
        this.saveConfig();

        return true;
    }

    public void updateItemStackSize(Material material, int newSize) {
        _items.put(material, newSize);
        config.saveSingleItem(material, newSize);
        this.saveConfig();
    }

    public void updateGroupStackSize(String groupName, int newSize) {
        config.SaveGroupStackSize(groupName, newSize);
        this.saveConfig();

        var materials = config.GetGroupMaterials(groupName);
        for (Material material : materials) {
            _items.put(material, newSize);
        }
        _groups.put(ItemGroup.fromConfigName(groupName), newSize);
    }

    public void removeItemFromSystem(Material material) {
        config.removeSingleItem(material);
        _items.remove(material);
        var groups = GetGroups();
        ItemGroup fromMaterial = ItemGroup.fromMaterial(material);
        if (fromMaterial != null || groups.containsKey(fromMaterial)) {
            _items.put(material, groups.get(fromMaterial));
        }
        this.saveConfig();
    }

    public void removeGroupFromSystem(String groupName) {
        config.SaveGroupStackSize(groupName, 0);
        var materials = config.GetGroupMaterials(groupName);
        for (var material : materials) {
            _items.remove(material);
        }
        _groups.put(ItemGroup.fromConfigName(groupName), 0);
        this.saveConfig();
    }

    public int resetAllItemStacks(Player player) {
        int resetCount = 0;

        // Reset items in main inventory
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() != org.bukkit.Material.AIR) {
                if (resetItemStack(item)) {
                    resetCount++;
                }
            }
        }

        return resetCount;
    }

    private boolean resetItemStack(ItemStack item) {
        if (item == null || item.getType() == org.bukkit.Material.AIR) {
            return false;
        }

        // Get default max stack size for this material
        int defaultMaxStackSize = item.getType().getMaxStackSize();

        // Check if the item's current max stack size is different from default
        if (item.getMaxStackSize() != defaultMaxStackSize) {
            // Reset to default max stack size
            org.bukkit.inventory.meta.ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setMaxStackSize(defaultMaxStackSize);
                item.setItemMeta(meta);
                return true;
            }
        }

        return false;
    }

    public int resetAllItemsOnServer() {
        int totalResetCount = 0;

        // Reset items for all online players
        for (Player player : getServer().getOnlinePlayers()) {
            totalResetCount += resetAllItemStacks(player);
        }

        // Note: Items in chests, droppers, etc. cannot be easily reset without
        // significant performance impact. This method focuses on player inventories.

        return totalResetCount;
    }

}