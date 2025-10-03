package Roman.stackOptimized;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEntityEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryEvents implements Listener {
    private boolean _stackBuckets;
    private final StackOptimized plugin;
    public InventoryEvents(boolean stackBuckets, StackOptimized plugin) {
        this.plugin = plugin;
        _stackBuckets = stackBuckets;
    }

    @EventHandler
    public void BucketFillEvent(PlayerBucketFillEvent event) {
        if (!_stackBuckets) {return;}
        ItemStack item = event.getItemStack();
        plugin.TryStack(item);
    }

    @EventHandler
    public void PlayerBucketEntityEvent(PlayerBucketEntityEvent event) {
        if (!_stackBuckets) {return;}
        ItemStack itemStack = event.getEntityBucket();
        plugin.TryStack(itemStack);
    }

    @EventHandler
    public void BucketEmptyEvent(PlayerBucketEmptyEvent event) {
        if (!_stackBuckets) {return;}
        ItemStack item = event.getItemStack();
        plugin.TryStack(item);
    }
}
