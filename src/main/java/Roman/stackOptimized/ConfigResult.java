package Roman.stackOptimized;

import org.bukkit.Material;

import java.util.Map;

public class ConfigResult {
    private final Map<Material, Integer> materials;
    private final Map<ItemGroup, Integer> groups;

    public ConfigResult(Map<Material, Integer> materials, Map<ItemGroup, Integer> groups) {
        this.materials = materials;
        this.groups = groups;
    }

    public Map<Material, Integer> getMaterials() {
        return materials;
    }

    public Map<ItemGroup, Integer> getGroups() {
        return groups;
    }
}
