package Roman.stackOptimized;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class StackOptimized extends JavaPlugin {

    Config config;
    Events events;
    Map<Material, Integer> _items = new HashMap<>();
    public static StackOptimized Instance;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        Instance = this;
        config = new Config(this);
        boolean canPrint = config.GetCanPrint();
        _items = config.GetItemsConfig(canPrint);
        getCommand("setMaxStackSize").setExecutor(this);
        events = new Events();

        getServer().getPluginManager().registerEvents(events, this);

        boolean stackBuckets = true;
        List<Material> BucketsMaterials = config.GetGroupMaterials("Buckets");
        for (Material material : BucketsMaterials) {
            if (!_items.containsKey(material)) {
                stackBuckets = false;
                break;
            }
        }
        if (stackBuckets) {
            getServer().getPluginManager().registerEvents(new InventoryEvents(stackBuckets), this);
        }
    }

    @Override
    public void onDisable() {}
    public void TryStack(ItemStack item) {
        Material material = item.getType();
        if(!_items.containsKey(material)){return;}
        int maxStackSize = _items.get(material);
        if (item.getMaxStackSize() == maxStackSize){return;}
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setMaxStackSize(maxStackSize);
        item.setItemMeta(itemMeta);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        
        if(!command.getName().equalsIgnoreCase("setMaxStackSize")){
            return true;
        }
        if (!player.hasPermission("op"))
        {
            player.sendMessage("You don't have permission to use this command.");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage("Incorrect command usage. Use: /setItem Max <count>");
            return true;
        }
        try {
            int number = Integer.parseInt(args[0]);
            if (number < 1 || number > 100) {
                player.sendMessage("Number must be between 1 and 100.");
                return true;
            }

            _items = config.AddData(_items,player.getInventory().getItemInMainHand().getType(), number);
            boolean saved = config.Save(_items);
            if (saved){saveConfig();}

        } catch (NumberFormatException e) {
            player.sendMessage("An integer was expected at this position.");
            return true;
        }
        return true;
    }
}