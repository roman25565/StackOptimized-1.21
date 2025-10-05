package Roman.stackOptimized;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemsGui implements Listener {
	private final StackOptimized plugin;

	public ItemsGui(StackOptimized plugin) {
		this.plugin = plugin;
	}

	private final String title = ChatColor.DARK_GREEN + "StackOptimized Items";

	public void openFor(Player player) {
		Inventory inv = Bukkit.createInventory(null, 54, title);
		fill(inv, player);
		player.openInventory(inv);
	}

	private void fill(Inventory inv, Player player) {
		Map<Material, Integer> items = plugin.GetItems();
		var groups = plugin.GetGroups();
		List<GroupedItem> groupedItems = plugin.getStackConfig().getGroupedItems(items, groups);
		int slot = 0;

		for (GroupedItem groupedItem : groupedItems) {
			if (slot >= inv.getSize()) {
				break;
			}

			ItemStack display = new ItemStack(groupedItem.getMaterial());
			ItemMeta meta = display.getItemMeta();

			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.YELLOW + "Max Stack Size: " + ChatColor.AQUA
					+ (groupedItem.getStackSize() > 0 ? groupedItem.getStackSize() : "default"));
			lore.add("");
			lore.add(ChatColor.GRAY + "Left click: " + ChatColor.GREEN + "+1");
			lore.add(ChatColor.GRAY + "Right click: " + ChatColor.RED + "-1");
			lore.add(ChatColor.GRAY + "Shift Left click: " + ChatColor.GREEN + "+10");
			lore.add(ChatColor.GRAY + "Shift Right click: " + ChatColor.RED + "-10");
			lore.add(ChatColor.GRAY + "MiddleClick or Drop: " + ChatColor.DARK_RED + "To Remove");

			if (!"Individual".equals(groupedItem.getGroupName())) {
				meta.setDisplayName(ChatColor.GOLD + groupedItem.getDisplayName());
				lore.add(ChatColor.GRAY + "Items in group: " + ChatColor.WHITE + groupedItem.getItemCount());
				lore.add("");
				lore.add(ChatColor.DARK_GRAY + "Group contains:");

				var groupItems = plugin.getStackConfig().GetGroupMaterials(groupedItem.getGroupName());
				int maxItems = Math.min(groupItems.size(), 16);

				for (int i = 0; i < maxItems; i++) {
					String materialName = MaterialNameHelper.getFriendlyMaterialName(groupItems.get(i));
					lore.add(ChatColor.GRAY + "• " + ChatColor.WHITE + materialName);
				}

				if (groupItems.size() > 16) {
					lore.add(ChatColor.GRAY + "... and " + (groupItems.size() - 16) + " more");
				}
			}

			var itemCount = groupedItem.getStackSize() > 0 ? groupedItem.getStackSize() : 1;
			meta.setMaxStackSize(itemCount);
			meta.setLore(lore);
			display.setItemMeta(meta);
			display.setAmount(itemCount);
			inv.setItem(slot, display);
			slot++;
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		HumanEntity who = event.getWhoClicked();
		if (!(who instanceof Player)) {
			return;
		}
		if (event.getView().getTitle() == null) {
			return;
		}
		if (!event.getView().getTitle().equals(title)) {
			return;
		}

		event.setCancelled(true);

		Player player = (Player) who;

		if (!player.isOp()) {
			player.sendMessage(ChatColor.RED + "You must be OP to use this GUI!");
			return;
		}

		ItemStack clickedItem = event.getCurrentItem();

		if (clickedItem == null || clickedItem.getType() == Material.AIR) {
			return;
		}

		int slot = event.getSlot();
		int rawSlot = event.getRawSlot();

		if (rawSlot >= 54) {
			handlePlayerInventoryClick(player, clickedItem, event.getClick());
            refreshInventory(event.getInventory(), player);
			return;
		}

		List<GroupedItem> groupedItems = plugin.getStackConfig().getGroupedItems(plugin.GetItems(), plugin.GetGroups());

		if (slot >= groupedItems.size()) {
			return;
		}

		GroupedItem groupedItem = groupedItems.get(slot);

		switch (event.getClick()) {
			case LEFT:
				changeStackSize(groupedItem, player, clickedItem, 1);
				break;
			case RIGHT:
				changeStackSize(groupedItem, player, clickedItem, -1);
				break;
			case SHIFT_LEFT:
				changeStackSize(groupedItem, player, clickedItem, 10);
				break;
			case SHIFT_RIGHT:
				changeStackSize(groupedItem, player, clickedItem, -10);
				break;
			case MIDDLE:
			case DROP:
			case CONTROL_DROP:
				removeItemFromSystem(groupedItem, player, clickedItem);
				break;
			default:
				break;
		}

		refreshInventory(event.getInventory(), player);
	}

	private void refreshInventory(Inventory inv, Player player) {
		inv.clear();
		fill(inv, player);
	}

	private void handlePlayerInventoryClick(Player player, ItemStack clickedItem,
		org.bukkit.event.inventory.ClickType clickType) {
		Material material = clickedItem.getType();
		int stackSize = clickedItem.getMaxStackSize();
        plugin.updateItemStackSize(material, stackSize);
	}

	private void changeStackSize(GroupedItem groupedItem, Player player, ItemStack item, int amount) {
		int currentSize = groupedItem.getStackSize();
		int newSize = currentSize + amount;

		newSize = Math.max(1, Math.min(99, newSize));

		if (newSize == currentSize) {
			if (amount > 0) {
				player.sendMessage(ChatColor.RED + "Maximum stack size is 99!");
			} else {
				player.sendMessage(ChatColor.RED + "Minimum stack size is 1!");
			}
			return;
		}

		updateStackSize(groupedItem, player, newSize, Math.abs(amount));
	}

	private void updateStackSize(GroupedItem groupedItem, Player player, int newSize, int changeAmount) {
		String action = newSize > groupedItem.getStackSize() ? "Increased" : "Decreased";

		if ("Individual".equals(groupedItem.getGroupName())) {
			plugin.updateItemStackSize(groupedItem.getMaterial(), newSize);
			String message = String.format("%s %s stack size%s to %d",
					action,
					getTranslatedMaterialName(groupedItem.getMaterial()),
					changeAmount > 1 ? " by " + changeAmount : "",
					newSize);
			player.sendMessage(ChatColor.GREEN + message);
		} else {
			plugin.updateGroupStackSize(groupedItem.getGroupName(), newSize);
			String message = String.format("%s %s group stack size%s to %d",
					action,
					groupedItem.getGroupName(),
					changeAmount > 1 ? " by " + changeAmount : "",
					newSize);
			player.sendMessage(ChatColor.GREEN + message);
		}
	}

	private String getTranslatedMaterialName(Material material) {
		return MaterialNameHelper.getFriendlyMaterialName(material);
	}

	private void removeItemFromSystem(GroupedItem groupedItem, Player player, ItemStack clickedItem) {
		System.out.println("Removing " + groupedItem.getMaterial() + " from system");
		if ("Individual".equals(groupedItem.getGroupName())) {
			plugin.removeItemFromSystem(groupedItem.getMaterial());
		} else {
			plugin.removeGroupFromSystem(groupedItem.getGroupName());
		}
	}
}
