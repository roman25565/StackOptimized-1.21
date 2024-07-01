import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;


public class InventoryClickEventWorker implements Listener {
//    static final Map<Inventory, PagedGUI> GUIs = new HashMap<>();
    @EventHandler
    public void eb(Event event) {
        if (_player != null && _player instanceof Player) return;
        _player.sendMessage(event.getEventName());
    }
    private Player _player;

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
        _player = player;
                player.sendMessage(event.getAction().name());
        switch (event.getAction()) {
            case PLACE_ONE://Окремий елемент з курсору переміщується до клацаного слота. //невикликаєця для спец блоків коли намагаєшся добавити 1 елемент до слоту з айтемом
                break;
            case PLACE_SOME://Деякі з елементів у слоті, на який ви клацнули, переміщуються до курсору.
            case PLACE_ALL: //Усі елементи на курсорі переміщуються до клацаного слота.
                PLACE(event,player);
                break;
            case COLLECT_TO_CURSOR: //В інвентарі шукаються однакові матеріали, і вони ставляться на курсор до Material.getMaxStackSize().
                COLLECT_TO_CURSOR(event,player);
                break;
            case MOVE_TO_OTHER_INVENTORY://важливо Предмет переміщується до протилежного інвентарю, якщо знайдено місце.
            case CLONE_STACK: //На курсор поміщається стопка максимального розміру елемента, на який ви клацнули.
            case SWAP_WITH_CURSOR: // Вибраний елемент і курсор міняються. s
            case HOTBAR_MOVE_AND_READD: //Натиснутий  предмет переміщується на гарячу панель, а предмет, який зараз там, знову додається до інвентарю гравця.
            case HOTBAR_SWAP: // Клацнуте слот і вибраний слот гарячої панелі поміняються місцями.


                //норм працюють взятя предметів в курсор
            case PICKUP_ONE:
            case PICKUP_SOME://баг
            case PICKUP_HALF:
            case PICKUP_ALL:
                //неотрібні
            case DROP_ONE_SLOT:
            case DROP_ALL_SLOT:
            case DROP_ONE_CURSOR:
            case DROP_ALL_CURSOR:
            case NOTHING:
            case UNKNOWN://нетреба клік правою або лівою кнопкою миші по пустому слоту з пустою мишкою
                //
        }
    }

    private void PLACE(InventoryClickEvent event, Player player) {
//        event.setCancelled(true);
//        ItemStack itemOnCursor = player.getItemOnCursor();
//        player.sendMessage("itemOnCursor" + itemOnCursor.getAmount());
//        int index = event.getSlot();
//        player.sendMessage("index" + index);
//        event.getInventory().setItem(index,new ItemStack(itemOnCursor.getType(), itemOnCursor.getAmount()));
//        player.sendMessage("Amount" + event.getInventory().getItem(index).getAmount());
//        event.getInventory().getItem(index).setAmount(itemOnCursor.getAmount());
//        event.getInventory().getItem(index).setAmount(itemOnCursor.getAmount());
////        event.setCurrentItem(itemOnCursor);
//        player.getItemOnCursor().setAmount(0);
    }

    private void COLLECT_TO_CURSOR(InventoryClickEvent event, Player player) {
//        event.setCancelled(true);
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
