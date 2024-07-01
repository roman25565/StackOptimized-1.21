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
//    }
//}
