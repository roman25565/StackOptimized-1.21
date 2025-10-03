package Roman.stackOptimized;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class StackOptimized extends JavaPlugin {

    private Config config;
    private Events events;
    private Map<Material, Integer> _items = new HashMap<>();

    public Map<Material, Integer> GetItems() {
        return _items;
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
        _items = config.LoadItemsConfig(canPrint);
        var itemsGui = new ItemsGui(this);
        var commands = new Commands(this, itemsGui);
        getCommand("setMaxStackSize").setExecutor(commands);
        getCommand("stackgui").setExecutor(commands);
        getCommand("name").setExecutor(commands);

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
        if (item.getMaxStackSize() == maxStackSize) {
            return;
        }
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setMaxStackSize(maxStackSize);
        item.setItemMeta(itemMeta);
    }

    public boolean AddItem(Material material, int maxStackSize) {
        _items.putIfAbsent(material, maxStackSize);
        int size = _items.get(material);
        if (size != maxStackSize) {
            _items.put(material, maxStackSize);
        }
        return true;
    }

    public void SaveItems() {
        Map<Material, Integer> itemsCopy = new HashMap<>(_items);
        config.Save(itemsCopy);
        this.saveConfig();
    }

    public void updateItemStackSize(Material material, int newSize) {
        _items.put(material, newSize);
        SaveItems();
    }

    public void updateGroupStackSize(String groupName, int newSize) {
        config.SaveGroupStackSize(groupName, newSize);
        this.saveConfig();

        var materials = config.GetGroupMaterials(groupName);
        for (Material material : materials) {
            _items.put(material, newSize);
        }
    }

}