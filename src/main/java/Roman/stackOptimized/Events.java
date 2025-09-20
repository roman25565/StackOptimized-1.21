package Roman.stackOptimized;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

public class Events implements Listener {

    @EventHandler
    public void PlayerPickup (EntityPickupItemEvent event){
        if (!(event.getEntity() instanceof Player)) { return; }
        ItemStack item = event.getItem().getItemStack();
        StackOptimized.Instance.TryStack(item);
    }

    // @EventHandler
    // public void EntityDeathEvent(EntityDeathEvent event){
    //     for (ItemStack itemStack : event.getDrops()) {
    //         StackOptimized.Instance.TryStack(itemStack);
    //     }
    // }

    // @EventHandler
    // public void BlockBreakEvent(BlockBreakEvent event) {
    //     for (ItemStack itemStack : event.getBlock().getDrops()) {
    //         StackOptimized.Instance.TryStack(itemStack);
    //     }
    // }

    // @EventHandler(priority = EventPriority.HIGHEST)
    // public void BlockDropItemEvent(BlockDropItemEvent event) {
    //     for (org.bukkit.entity.Item item : event.getItems()) {
    //         ItemStack itemStack = item.getItemStack();
    //         StackOptimized.Instance.TryStack(itemStack);
    //     }
    // }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void ItemSpawnEvent(ItemSpawnEvent event) {
        ItemStack itemStack = event.getEntity().getItemStack();
        StackOptimized.Instance.TryStack(itemStack);
    }


//    @EventHandler //Not work :(
//    public void CraftItemEvent (CraftItemEvent event) {
//    }
//    @EventHandler //Not work :(
//    public void InventoryClickEvent (InventoryClickEvent event) {
//    }
}
