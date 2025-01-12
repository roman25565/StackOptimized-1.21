package Roman.stackOptimized;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEntityEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryEvents implements Listener {
    private boolean _stackBuckets;
    public InventoryEvents(boolean stackBuckets) {
        _stackBuckets = stackBuckets;
    }

    @EventHandler
    public void BucketFillEvent(PlayerBucketFillEvent event) {//Best Work
        if (!_stackBuckets) {return;}
        ItemStack item = event.getItemStack();
        StackOptimized.Instance.TryStack(item);

    }

    @EventHandler
    public void PlayerBucketEntityEvent(PlayerBucketEntityEvent event) {
        if (!_stackBuckets) {return;}
        ItemStack itemStack = event.getEntityBucket();
        StackOptimized.Instance.TryStack(itemStack);
    }

//    @EventHandler //Not work :(
//    public void BucketEmptyEvent(PlayerBucketEmptyEvent event) {
//    }
}
