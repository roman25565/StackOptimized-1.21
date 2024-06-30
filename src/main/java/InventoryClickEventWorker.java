import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

import static org.bukkit.event.inventory.InventoryAction.*;

public class InventoryClickEventWorker implements Listener {
//    static final Map<Inventory, PagedGUI> GUIs = new HashMap<>();
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!isPlayer(event.getWhoClicked())) {
            return;
        }
        Player viewer = (Player) event.getWhoClicked();
//        PagedGUI gui = GUIs.get(event.getInventory());
//        if (gui == null) {
//            return;
//        }
        switch (event.getAction()) {
            // Disable unknown actions
            default:
            case UNKNOWN:
                // These events are cursed. There's no way to know where the items are moving to or from, just cancel.
            case COLLECT_TO_CURSOR:
            case MOVE_TO_OTHER_INVENTORY:
                event.setCancelled(true);
                return;
            // Leave these be as they aren't dangerous. Cloning a stack is an OP feature and clones to your cursor
            case NOTHING:
            case CLONE_STACK:
                return;
            // Allow the following in context to the player's inventory, but not when interacting with the GUI
            case PICKUP_ONE:
            case PICKUP_SOME:
            case PICKUP_HALF:
            case PICKUP_ALL:
            case PLACE_ONE:
            case PLACE_SOME:
            case PLACE_ALL:
            case SWAP_WITH_CURSOR:
            case DROP_ONE_SLOT:
            case DROP_ALL_SLOT:
            case DROP_ONE_CURSOR:
            case DROP_ALL_CURSOR:
            case HOTBAR_MOVE_AND_READD:
            case HOTBAR_SWAP: {
                event.setCancelled(true);
//                gui.clicked(event.getSlot(), viewer, event.getClick());
            }
        }
    }
    private static boolean isPlayer(final Entity entity) {
        if (entity == null) {
            return false;
        }
        if (entity.getType() != EntityType.PLAYER) {
            return false;
        }
        if (!(entity instanceof Player)) {
            return false;
        }
        return true;
    }
}
