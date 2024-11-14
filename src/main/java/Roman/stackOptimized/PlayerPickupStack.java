package Roman.stackOptimized;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class PlayerPickupStack implements Listener {
    Map<Material, Integer> _items = new HashMap<Material, Integer>();
    public void SetItems(Map<Material, Integer> items) {
        _items = items;
    }
    public PlayerPickupStack(Map<Material, Integer> items) {
        _items = items;
    }

    @EventHandler
    public void PlayerPickup (EntityPickupItemEvent event){
        if (!(event.getEntity() instanceof Player)) { return; }

        ItemStack item = event.getItem().getItemStack();
        Material material = item.getType();

        Player player = (Player) event.getEntity();
        player.sendMessage("Material: " +  material);

        if(!_items.containsKey(material)){return;}

        player.sendMessage("Material: " +  material);
        int maxStackSize = _items.get(material);
        if (item.getMaxStackSize() == maxStackSize){return;}

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setMaxStackSize(maxStackSize);

        item.setItemMeta(itemMeta);
    }
}
