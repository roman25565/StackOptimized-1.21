package kirca.romanstack;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.block.PistonMoveReaction.MOVE;
import static org.bukkit.event.inventory.InventoryAction.PICKUP_ALL;
import static org.bukkit.event.inventory.InventoryAction.PICKUP_ONE;

public final class RomanStack extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("камінь").setExecutor(this);

        getServer().getPluginManager().registerEvents(new DebugLog(), this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("камінь")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                // Створити стек каменю з 60 одиниць
                ItemStack stoneStack = new ItemStack(Material.WATER_BUCKET, 64);

                // Додати стек до інвентарю гравця
                player.getInventory().addItem(stoneStack);

                // Надіслати повідомлення в чат гравцю
                player.sendMessage("Вам видано 60 каменю!");

                return true;
            } else {
                sender.sendMessage("Ця команда доступна лише для гравців!");
                return false;
            }
        }

        return false;
    }

}