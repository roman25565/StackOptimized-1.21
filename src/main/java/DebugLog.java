import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

//InventoryClickEvent
//InventoryDragEvent
//PlayerPickupItemEvent

public class DebugLog implements Listener {

    Dictionary<String, Player> players= new Hashtable<>();

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        players.put(player.getName(),player);
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent event) {
        players.remove(event.getPlayer().getEntityId());
    }

    private void UpdatePlayersData(Player player) {
        players.put(player.getName(),player);
    }

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent event) {
        event.getEventName();
        Player player = (Player) event.getWhoClicked().getInventory().getHolder();
        player.sendMessage("назва " + event.getEventName());

        UpdatePlayersData(player);
    }

    @EventHandler
    public void InventoryDragEvent(InventoryDragEvent event) {
        event.getEventName();
        Player player = (Player) event;
        player.sendMessage("назва " + event.getEventName());

        UpdatePlayersData(player);
    }

    @EventHandler
    public void PlayerPickupStack(PlayerPickupItemEvent event) {
        event.getEventName();
        Player player = event.getPlayer();
        Material BlockType = event.getItem().getItemStack().getType();
        int Count = event.getItem().getItemStack().getAmount();
        player.sendMessage("кількість піднятих речей " + event.getItem().getItemStack().getAmount());
        player.sendMessage("в слоті " + FindMaterialInInventory(player.getInventory(), BlockType,false));

        event.setCancelled(true);

        AddAddedItems(Count, event.getItem().getItemStack().getType(), player);
        event.getItem().remove();
    }

    private void DeleteAddedItems(int count, Material material, Player player){
        for (int i = 0; i < player.getInventory().getSize(); i++){
            player.sendMessage("слот " + i + "Type " + player.getInventory().getItem(i).getType());
        }
        for (int i = count; i >= 0; i--) {
            player.sendMessage("i" + i);
            int slotIndex = FindMaterialInInventory(player.getInventory(), material,false);
            if (slotIndex == -1) {
                player.sendMessage("Помилка незнайдено речей для видалення");
                return;
            }
            int countDeleted = player.getInventory().getItem(slotIndex).getAmount();
            player.sendMessage("видаляю речі зі слоту " + slotIndex);
            player.getInventory().getItem(slotIndex).setType(Material.AIR);
            i -= countDeleted + 1;
        }
    }

    private void AddAddedItems(int count, Material material, Player player){
        for (int i = count; i > 0; i--) {
            int slotIndex = FindMaterialInInventory(player.getInventory(), material,true);

            int addCount = i > 64 ? 64 : i;
            if (slotIndex == -1) {
                player.getInventory().addItem(new ItemStack(material,addCount));
            }else {
                int hasItemsInSlot = player.getInventory().getItem(slotIndex).getAmount();
                addCount = ((i + hasItemsInSlot) > 64) ? Math.abs(64 - hasItemsInSlot) : i;
                player.sendMessage("i " + i);
                player.sendMessage("hasItemsInSlot " + hasItemsInSlot);
                player.sendMessage("Math.abs(64 - hasItemsInSlot) " + Math.abs(64 - hasItemsInSlot));
                player.sendMessage("addCount " + addCount);
                player.getInventory().setItem(slotIndex,new ItemStack(material,hasItemsInSlot + addCount));
            }
            i -= addCount - 1;
        }
    }

    private int FindMaterialInInventory(Inventory inventory, Material material, boolean fromBeginningToEnd) {
        if (fromBeginningToEnd) {
            for (int i = 0; i <= inventory.getSize(); i++) {
                if (CheckSlot(inventory, material, i)) return i;
            }
        }
        else {
            for (int i = inventory.getSize(); i >= 0; i--) {
                if (CheckSlot(inventory, material, i)) return i;
            }
        }
        return -1;
    }

    private Boolean CheckSlot(Inventory inventory, Material material, int i) {
        if (inventory.getItem(i) == null || inventory.getItem(i) == null) {
            return false;
        }
        if (inventory.getItem(i).getType() == material && inventory.getItem(i).getAmount() < 64) {
            return true;
        }
        return false;
    }

}

