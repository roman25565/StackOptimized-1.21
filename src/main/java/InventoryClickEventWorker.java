import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class InventoryClickEventWorker implements Listener {
//    static final Map<Inventory, PagedGUI> GUIs = new HashMap<>();
//    @EventHandler
//    public void eb(Event event) {
//        if (_player != null && _player instanceof Player) return;
//        _player.sendMessage(event.getEventName());
//    }

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

        Player player = (Player) event.getWhoClicked();
                player.sendMessage(event.getAction().name());
        switch (event.getAction()) {
            case PLACE_ALL: //Усі елементи на курсорі переміщуються до клацаного слота.
                PlaceAll(event,player);
                break;
            case COLLECT_TO_CURSOR: //В інвентарі шукаються однакові матеріали, і вони ставляться на курсор до Material.getMaxStackSize().
                COLLECT_TO_CURSOR(event,player);
                break;
            case MOVE_TO_OTHER_INVENTORY://важливо Предмет переміщується до протилежного інвентарю, якщо знайдено місце.
                MoveToOtherInventory(event,player);
                break;
            case CLONE_STACK: //На курсор поміщається стопка максимального розміру елемента, на який ви клацнули.
            case SWAP_WITH_CURSOR: // Вибраний елемент і курсор міняються.
            case HOTBAR_MOVE_AND_READD: //Натиснутий  предмет переміщується на гарячу панель, а предмет, який зараз там, знову додається до інвентарю гравця.
            case HOTBAR_SWAP: // Клацнуте слот і вибраний слот гарячої панелі поміняються місцями.
            case NOTHING: // через нього може бути логіка  PLACE_SOME
            case PICKUP_SOME://баг можна стакати овер 64

                //норм працюють взятя предметів в курсор
            case PICKUP_ONE:
            case PICKUP_HALF:
            case PICKUP_ALL:
            case PLACE_SOME://Деякі елементи з курсору переміщуються до клацаного слота (зазвичай до максимального розміру стека). / треба буде робити для предметів які по дефолту стакаюця більше 1
                break;
                //неотрібні
            case PLACE_ONE://Окремий елемент з курсору переміщується до клацаного слота. //невикликаєця для спец блоків коли намагаєшся добавити 1 елемент до слоту з айтемом
            case DROP_ONE_SLOT:
            case DROP_ALL_SLOT:
            case DROP_ONE_CURSOR:
            case DROP_ALL_CURSOR:
            case UNKNOWN://нетреба клік правою або лівою кнопкою миші по пустому слоту з пустою мишкою
                //
        }
    }

    private void PlaceAll(InventoryClickEvent event, Player player){
        player.sendMessage("event.getInventory().getType()" + event.getInventory().getType());
        if (player.getItemOnCursor().getType() == Material.AIR) {//необхіно для нормального викидання предметів
            return;
        }
        player.sendMessage("2");
        event.setCancelled(true);
        int rawSlot = event.getRawSlot();
        Inventory inventory = event.getInventory();
        int InventorySize = inventory.getSize();

        player.sendMessage("rawSlot: " + rawSlot);
        player.sendMessage("InventorySize" + InventorySize);

        if (inventory.getType() == InventoryType.CRAFTING){// без сундука
            int slotIndex = ConversionFromCraftingTypeToPlayerInventory(rawSlot);
            player.getInventory().setItem(slotIndex, player.getItemOnCursor());
        }
        else if (inventory.getSize() <= rawSlot){// до інвентарю гравця з відкритим інвентарем сундука
            int slotIndex = ConversionFromChessSlotIndexToPlayerInventorySlotIndex(rawSlot, inventory.getSize(), event.getSlotType());
            player.getInventory().setItem(slotIndex, player.getItemOnCursor());
        }
        else {
            inventory.setMaxStackSize(64);
            player.sendMessage("MaxStackSize" + inventory.getMaxStackSize());
            ItemStack cursorStack = player.getItemOnCursor();
            int amount = cursorStack.getAmount(); // Get the stack size

// Create a new ItemStack with the same type and set the amount
            ItemStack itemToAdd = new ItemStack(cursorStack.getType(), amount);

// Add the item to the inventory
            inventory.setItem(rawSlot, itemToAdd);
            inventory.getItem(rawSlot).setAmount(amount);
            player.updateInventory();
            player.sendMessage("StackSize" + inventory.getItem(rawSlot));
        }

        player.setItemOnCursor(null);
    }

    private void COLLECT_TO_CURSOR(InventoryClickEvent event, Player player) {
        event.setCancelled(true);

    }

    private void MoveToOtherInventory(InventoryClickEvent event, Player player){
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        int count = item.getAmount();
        Material material = item.getType();
        Inventory inventory = event.getInventory();

        for (int i = count; i > 0; i--) {
            int slotIndex = DebugLog.FindMaterialInInventory(inventory, material, player);

            int addCount = i > 64 ? 64 : i;
            if (slotIndex == -1) {
                int index = FindFreeSlot(inventory);
                inventory.setItem(index, new ItemStack(material, addCount));
                inventory.getItem(index).setAmount(addCount);
                player.updateInventory();
            }else {

                int hasItemsInSlot = inventory.getItem(slotIndex).getAmount();
                addCount = ((i + hasItemsInSlot) > 64) ? Math.abs(64 - hasItemsInSlot) : i;
                inventory.setItem(slotIndex,new ItemStack(material,hasItemsInSlot + addCount));
                inventory.getItem(slotIndex).setAmount(hasItemsInSlot + addCount);
            }
            i -= addCount - 1;
        }

        event.setCurrentItem(null);
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

    private int ConversionFromChessSlotIndexToPlayerInventorySlotIndex(int index, int InventorySize, InventoryType.SlotType slotType) {
        int add = 0;
        if (InventorySize == 54) {
            add = 27;
        }
        int slotIndex = index - (18 + add);

        if (slotType == InventoryType.SlotType.QUICKBAR){
            slotIndex = index - (54 + add);
        }
        return slotIndex;
    }

    private int ConversionFromCraftingTypeToPlayerInventory(int index){
        if (index >= 36) {
            return index - 36;
        }
        return index;
    }

    private int FindFreeSlot(Inventory inventory){
        if (inventory.isEmpty()) {return 0;}
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                return i;
            }
        }
        return -1;
    }
}
