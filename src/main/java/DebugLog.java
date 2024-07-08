import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

//InventoryClickEvent
//InventoryDragEvent

public class DebugLog implements Listener {

    Dictionary<String, Player> players= new Hashtable<>();

    @EventHandler
    public void PlayerPickupStack(PlayerPickupItemEvent event) {
        event.getEventName();
        Player player = event.getPlayer();
        Material BlockType = event.getItem().getItemStack().getType();
        int Count = event.getItem().getItemStack().getAmount();
//        player.sendMessage("кількість піднятих речей " + event.getItem().getItemStack().getAmount());
//        player.sendMessage("в слоті " + FindMaterialInInventory(player.getInventory(), BlockType,false));

        event.setCancelled(true);

        AddItemsToInventory(Count, event.getItem().getItemStack().getType(), player.getInventory(),player);
        event.getItem().remove();
    }

    public static void AddItemsToInventory(int count, Material material, Inventory inventory,Player player){
        for (int i = count; i > 0; i--) {
            int slotIndex = FindMaterialInInventory(inventory, material, player);

            int addCount = i > 64 ? 64 : i;
            if (slotIndex == -1) {
                inventory.addItem(new ItemStack(material,addCount));
            }else {
                int hasItemsInSlot = inventory.getItem(slotIndex).getAmount();
                addCount = ((i + hasItemsInSlot) > 64) ? Math.abs(64 - hasItemsInSlot) : i;
                player.sendMessage("i " + i);
                player.sendMessage("hasItemsInSlot " + hasItemsInSlot);
                player.sendMessage("Math.abs(64 - hasItemsInSlot) " + Math.abs(64 - hasItemsInSlot));
                player.sendMessage("addCount " + addCount);
                inventory.setItem(slotIndex,new ItemStack(material,hasItemsInSlot + addCount));
            }
            i -= addCount - 1;
        }
    }

    //шукає елемент в наданому інвентарі вертає -1 якщо жодного не знайдено якщо знайдено повертає індекс слоту в якому менше 64 елементів шуканого типу
    public static int FindMaterialInInventory(Inventory inventory, Material material, Player player) {
        player.sendMessage("material " + material);
        for (int i = 0; i < inventory.getSize(); i++) {
            player.sendMessage("i " + i);
            if (CheckSlot(inventory, material, i)) return i;
        }
        return -1;
    }

    private static Boolean CheckSlot(Inventory inventory, Material material, int i) {
        if (inventory.isEmpty() || inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR) {
            return false;
        }
        if (inventory.getItem(i).getType() == material && inventory.getItem(i).getAmount() < 64) {
            return true;
        }
        return false;
    }

}

