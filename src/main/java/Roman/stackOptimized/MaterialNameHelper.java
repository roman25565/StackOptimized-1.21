package Roman.stackOptimized;

import org.bukkit.Material;

public class MaterialNameHelper {
    public static String getFriendlyMaterialName(Material material) {
        try {
            net.md_5.bungee.api.chat.TranslatableComponent component = new net.md_5.bungee.api.chat.TranslatableComponent(
                    material.getTranslationKey());
            return component.toPlainText();
        } catch (Exception e) {
            System.out.println("TranslatableComponent failed for " + material.toString() + " " + e.getMessage());
            return convertMaterialName(material.toString());
        }
    }

    private static String convertMaterialName(String materialName) {
        String name = materialName.toLowerCase().replace("_", " ");
        String[] words = name.split(" ");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (result.length() > 0) {
                result.append(" ");
            }
            result.append(word.substring(0, 1).toUpperCase()).append(word.substring(1));
        }

        return result.toString();
    }
}
