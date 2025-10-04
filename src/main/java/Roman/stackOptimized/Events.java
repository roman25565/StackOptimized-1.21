package Roman.stackOptimized;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

public class Events implements Listener {
    private final StackOptimized plugin;

    public Events(StackOptimized plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void PlayerPickup(EntityPickupItemEvent event) {
        System.out.println("PlayerPickup");
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        ItemStack item = event.getItem().getItemStack();
        plugin.TryStack(item);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void ItemSpawnEvent(ItemSpawnEvent event) {
        ItemStack itemStack = event.getEntity().getItemStack();
        plugin.TryStack(itemStack);
    }
}
