//import org.bukkit.Material;
//import org.bukkit.entity.Player;
//
//public class ussles {
//    private void DeleteAddedItems(int count, Material material, Player player){
//        for (int i = 0; i < player.getInventory().getSize(); i++){
//            player.sendMessage("слот " + i + "Type " + player.getInventory().getItem(i).getType());
//        }
//        for (int i = count; i >= 0; i--) {
//            player.sendMessage("i" + i);
//            int slotIndex = FindMaterialInInventory(player.getInventory(), material,false);
//            if (slotIndex == -1) {
//                player.sendMessage("Помилка незнайдено речей для видалення");
//                return;
//            }
//            int countDeleted = player.getInventory().getItem(slotIndex).getAmount();
//            player.sendMessage("видаляю речі зі слоту " + slotIndex);
//            player.getInventory().getItem(slotIndex).setType(Material.AIR);
//            i -= countDeleted + 1;
//        }
//private void PlaceSome(InventoryClickEvent event, Player player) {
//        event.setCancelled(true);
//        ItemStack itemOnCursor = player.getItemOnCursor();
//        int needToRemuve = 64 - event.getCurrentItem().getAmount();
//        int needToAdd = 64;
//        if (itemOnCursor.getAmount() < needToRemuve){
//            needToRemuve = itemOnCursor.getAmount();
//            needToAdd = needToRemuve;
//        }
//
//        int rawSlot = event.getRawSlot();
//        Inventory inventory = event.getInventory();
//
//        if (inventory.getSize() <= rawSlot) {// до інвентарю гравця з відкритим інвентарем сундука
//            int slotIndex = ConversionFromChessSlotIndexToPlayerInventorySlotIndex(rawSlot, inventory.getSize(), event.getSlotType());
//            player.getInventory().setItem(slotIndex, new ItemStack(player.getItemOnCursor().getType(), needToAdd ));
//        }
//        else {
//            int amount = itemOnCursor.getAmount();
//            ItemStack itemToAdd = new ItemStack(player.getItemOnCursor().getType(), amount);
//            inventory.setItem(rawSlot, itemToAdd);
//            inventory.getItem(rawSlot).setAmount(amount);
//            player.updateInventory();
//            player.sendMessage("StackSize" + inventory.getItem(rawSlot));
//        }
//
//        player.setItemOnCursor(new ItemStack(player.getItemOnCursor().getType(), needToRemuve));
//    }
//    }
//}
