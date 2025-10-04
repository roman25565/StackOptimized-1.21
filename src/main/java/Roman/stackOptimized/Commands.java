package Roman.stackOptimized;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    private final StackOptimized plugin;
    private final ItemsGui itemsGui;

    public Commands(StackOptimized plugin, ItemsGui itemsGui) {
        this.plugin = plugin;
        this.itemsGui = itemsGui;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;

        if (!player.hasPermission("op")) {
            player.sendMessage("You don't have permission to use this command.");
            return true;
        }

        if (command.getName().equalsIgnoreCase("name")) {
            var item = player.getInventory().getItemInMainHand();
            net.md_5.bungee.api.chat.TranslatableComponent component = new net.md_5.bungee.api.chat.TranslatableComponent(
                    item.getType().getTranslationKey());

            player.spigot().sendMessage(component);
            player.sendMessage("Mat" + item + " Item name: " + item.getItemMeta().getItemName());
            return true;
        }

        if (command.getName().equalsIgnoreCase("stackgui")) {
            itemsGui.openFor(player);
            return true;
        }

        if (command.getName().equalsIgnoreCase("resetstacks")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("all")) {
                int totalResetCount = plugin.resetAllItemsOnServer();
                player.sendMessage(
                        "Reset " + totalResetCount + " items to default stack sizes across the entire server.");
            } else {
                int resetCount = plugin.resetAllItemStacks(player);
                player.sendMessage("Reset " + resetCount + " items to default stack sizes in your inventory.");
                player.sendMessage("Use '/resetstacks all' to reset all items on the server.");
            }
            return true;
        }
        if (command.getName().equalsIgnoreCase("setMaxStackSize")) {
            if (args.length != 1) {
                sender.sendMessage("Incorrect command usage. Use: /setItem Max <count>");
                return true;
            }

            try {
                int number = Integer.parseInt(args[0]);
                if (number < 1 || number > 99) {
                    player.sendMessage("Number must be between 1 and 99.");
                    return true;
                }
                plugin.updateItemStackSize(player.getInventory().getItemInMainHand().getType(), number);
                player.sendMessage("Item added successfully.");
            } catch (NumberFormatException e) {
                player.sendMessage("An integer was expected at this position.");
            }

        }
        return true;
    }
}
