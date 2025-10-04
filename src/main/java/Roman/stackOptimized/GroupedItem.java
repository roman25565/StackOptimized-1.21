package Roman.stackOptimized;

import org.bukkit.Material;

public class GroupedItem {
    private final String groupName;
    private final Material material;
    private final int stackSize;
    private final int itemCount;

    public GroupedItem(String groupName, Material representative, int stackSize, int itemCount) {
        this.groupName = groupName;
        this.material = representative;
        this.stackSize = stackSize;
        this.itemCount = itemCount;
    }

    public String getGroupName() {
        return groupName;
    }

    public Material getMaterial() {
        return material;
    }

    public int getStackSize() {
        return stackSize;
    }

    public int getItemCount() {
        return itemCount;
    }

    public String getDisplayName() {
        if ("Individual".equals(groupName)) {
            return MaterialNameHelper.getFriendlyMaterialName(material);
        } else {
            return groupName + " (" + itemCount + " items)";
        }
    }
}
