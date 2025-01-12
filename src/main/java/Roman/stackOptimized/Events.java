package Roman.stackOptimized;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class Events implements Listener {

    @EventHandler
    public void PlayerPickup (EntityPickupItemEvent event){
        if (!(event.getEntity() instanceof Player)) { return; }
        ItemStack item = event.getItem().getItemStack();
        StackOptimized.Instance.TryStack(item);
    }

    @EventHandler
    public void EntityDeathEvent(EntityDeathEvent event){
        for (ItemStack itemStack : event.getDrops()) {
            StackOptimized.Instance.TryStack(itemStack);
        }
    }


//    @EventHandler //Not work :(
//    public void CraftItemEvent (CraftItemEvent event) {
//    }
//    @EventHandler //Not work :(
//    public void InventoryClickEvent (InventoryClickEvent event) {
//    }
}
