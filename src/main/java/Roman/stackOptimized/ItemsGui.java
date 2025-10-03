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

import net.md_5.bungee.api.chat.TranslatableComponent;

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
		List<Config.GroupedItem> groupedItems = plugin.getStackConfig().getGroupedItems(items);
        System.out.println(ChatColor.GREEN + "Loaded " + groupedItems.size() + " items");
		int slot = 0;
		
		for (Config.GroupedItem groupedItem : groupedItems) {
			if (slot >= inv.getSize()) { break; }
			
			ItemStack display = new ItemStack(groupedItem.getRepresentative());
			ItemMeta meta = display.getItemMeta();

			// Set lore
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.YELLOW + "Max Stack Size: " + ChatColor.AQUA + groupedItem.getStackSize());
			lore.add("");
			lore.add(ChatColor.GRAY + "Left click: " + ChatColor.GREEN + "+1");
			lore.add(ChatColor.GRAY + "Right click: " + ChatColor.RED + "-1");

			if (!"Individual".equals(groupedItem.getGroupName())) {
			    meta.setDisplayName(ChatColor.GOLD + groupedItem.getDisplayName());
				lore.add(ChatColor.GRAY + "Items in group: " + ChatColor.WHITE + groupedItem.getItemCount());
				lore.add("");
				lore.add(ChatColor.DARK_GRAY + "Group contains:");
				
				// Add list of items in the group (max 16 items to avoid too long lore)
				var groupItems = plugin.getStackConfig().GetGroupMaterials(groupedItem.getGroupName());
				int maxItems = Math.min(groupItems.size(), 16);
				
				for (int i = 0; i < maxItems; i++) {
					var component = new TranslatableComponent(groupItems.get(i).getTranslationKey()).toPlainText();
					lore.add(ChatColor.GRAY + "• " + ChatColor.WHITE + component);
				}
				
				if (groupItems.size() > 16) {
					lore.add(ChatColor.GRAY + "... and " + (groupItems.size() - 16) + " more");
				}
			}

            meta.setMaxStackSize(groupedItem.getStackSize());
			meta.setLore(lore);
			display.setItemMeta(meta);
            display.setAmount(groupedItem.getStackSize());
			inv.setItem(slot, display);
			slot++;
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		HumanEntity who = event.getWhoClicked();
		if (!(who instanceof Player)) { return; }
		if (event.getView().getTitle() == null) { return; }
		if (!event.getView().getTitle().equals(title)) { return; }
		
		event.setCancelled(true);
		
		Player player = (Player) who;
		ItemStack clickedItem = event.getCurrentItem();
		
		if (clickedItem == null || clickedItem.getType() == Material.AIR) { return; }

		// Get the grouped item from the clicked slot
		int slot = event.getSlot();
		List<Config.GroupedItem> groupedItems = plugin.getStackConfig().getGroupedItems(plugin.GetItems());
		
		if (slot >= groupedItems.size()) { return; }
		
		Config.GroupedItem groupedItem = groupedItems.get(slot);

        System.out.println(ChatColor.GREEN + "Loaded22 " + groupedItems.size() + " items");
		// Handle left click (increase) and right click (decrease)
		if (event.getClick().isLeftClick()) {
			increaseStackSize(groupedItem, player, clickedItem);
		} else if (event.getClick().isRightClick()) {
			decreaseStackSize(groupedItem, player, clickedItem);
		}

        refreshInventory(event.getInventory(), player);
	}
    private void refreshInventory(Inventory inv, Player player) {
        inv.clear(); // Очищаємо інвентар
        fill(inv, player); // Заповнюємо заново з новими даними
    }

    private void UpdateAmount(ItemStack item, int newSize){
//        var amount = item.getAmount();
//        item.getItemMeta().setMaxStackSize(newSize);
//        item.setAmount(amount);
    }
	
	private void increaseStackSize(Config.GroupedItem groupedItem, Player player, ItemStack item) {
		int currentSize = groupedItem.getStackSize();
		if (currentSize >= 100) {
			player.sendMessage(ChatColor.RED + "Maximum stack size is 100!");
			return;
		}
		
		int newSize = currentSize + 1;
        UpdateAmount(item, newSize);
		if ("Individual".equals(groupedItem.getGroupName())) {
			plugin.updateItemStackSize(groupedItem.getRepresentative(), newSize);
			player.sendMessage(ChatColor.GREEN + "Increased " + getTranslatedMaterialName(groupedItem.getRepresentative()) + 
				" stack size to " + newSize);
		} else {
			plugin.updateGroupStackSize(groupedItem.getGroupName(), newSize);
			player.sendMessage(ChatColor.GREEN + "Increased " + groupedItem.getGroupName() + 
				" group stack size to " + newSize);
		}
	}
	
	private void decreaseStackSize(Config.GroupedItem groupedItem, Player player, ItemStack item) {
		int currentSize = groupedItem.getStackSize();
		if (currentSize <= 1) {
			player.sendMessage(ChatColor.RED + "Minimum stack size is 1!");
			return;
		}
		
		int newSize = currentSize - 1;
        UpdateAmount(item, newSize);
		if ("Individual".equals(groupedItem.getGroupName())) {
			plugin.updateItemStackSize(groupedItem.getRepresentative(), newSize);
			player.sendMessage(ChatColor.GREEN + "Decreased " + getTranslatedMaterialName(groupedItem.getRepresentative()) + 
				" stack size to " + newSize);
		} else {
			plugin.updateGroupStackSize(groupedItem.getGroupName(), newSize);
			player.sendMessage(ChatColor.GREEN + "Decreased " + groupedItem.getGroupName() + 
				" group stack size to " + newSize);
		}
	}
	
	private String getTranslatedMaterialName(Material material) {
		try {
			net.md_5.bungee.api.chat.TranslatableComponent component =
				new net.md_5.bungee.api.chat.TranslatableComponent(material.getTranslationKey());
			return component.toPlainText();
		} catch (Exception e) {
			System.out.println("TranslatableComponent failed for " + material.toString() + " " + e.getMessage());
			// Fallback to Config method if TranslatableComponent fails
			return Config.getFriendlyMaterialName(material);
		}
	}
}



