package Roman.stackOptimized;

import org.bukkit.Material;

import java.util.*;

public class GroupedItemManager {

    public List<GroupedItem> getGroupedItems(Map<Material, Integer> items, Map<ItemGroup, Integer> groups) {
        List<GroupedItem> groupedItems = new ArrayList<>();

        addGroupItems(groups, groupedItems);
        addIndividualItems(items, groups, groupedItems);

        return groupedItems;
    }

    private void addGroupItems(Map<ItemGroup, Integer> groups, List<GroupedItem> groupedItems) {
        for (Map.Entry<ItemGroup, Integer> groupEntry : groups.entrySet()) {
            ItemGroup group = groupEntry.getKey();
            Integer groupStackSize = groupEntry.getValue();

            List<Material> groupMaterials = group.getMaterials();
            Material representative = groupMaterials.get(0);

            groupedItems.add(new GroupedItem(
                    group.getConfigName(),
                    representative,
                    groupStackSize,
                    groupMaterials.size()));

        }
    }

    private void addIndividualItems(Map<Material, Integer> items, Map<ItemGroup, Integer> groups,
            List<GroupedItem> groupedItems) {
        Map<Material, Integer> individualItems = getIndividualItems(items, groups);

        for (Map.Entry<Material, Integer> entry : individualItems.entrySet()) {
            groupedItems.add(new GroupedItem("Individual", entry.getKey(), entry.getValue(), 1));
        }
    }

    private Map<Material, Integer> getIndividualItems(Map<Material, Integer> items, Map<ItemGroup, Integer> groups) {
        Map<Material, Integer> individualItems = new HashMap<>(items);

        for (Map.Entry<ItemGroup, Integer> groupEntry : groups.entrySet()) {
            ItemGroup group = groupEntry.getKey();
            var value = groupEntry.getValue();
            List<Material> groupMaterials = group.getMaterials();
            for (Material material : groupMaterials) {
                if(!individualItems.containsKey(material)) continue;
                var itemValue = individualItems.get(material);
                if (value != itemValue) continue;
                individualItems.remove(material);
            }
        }

        return individualItems;
    }

    public List<Material> getGroupMaterials(String groupName) {
        ItemGroup group = ItemGroup.fromConfigName(groupName);
        return group != null ? group.getMaterials() : new ArrayList<>();
    }
}
