package Roman.stackOptimized;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class StackOptimized extends JavaPlugin {

    Config config;
    PlayerPickupStack playerPickupStack;
    Map<Material, Integer> _items = new HashMap<>();
    @Override
    public void onEnable() {
        saveDefaultConfig();

        config = new Config(this);
        boolean canPrint = config.GetCanPrint();
        _items = config.GetItemsConfig(canPrint);
        getCommand("setMaxStackSize").setExecutor(this);
        playerPickupStack = new PlayerPickupStack(_items);
        getServer().getPluginManager().registerEvents(playerPickupStack, this);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        if(!command.getName().equalsIgnoreCase("setMaxStackSize")){
            return true;
        }


        Player player = (Player) sender;
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
            playerPickupStack.SetItems(_items);

        } catch (NumberFormatException e) {
            player.sendMessage("An integer was expected at this position.");
            return true;
        }


        return true;
    }
}