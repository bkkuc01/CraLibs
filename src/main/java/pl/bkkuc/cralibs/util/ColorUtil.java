package pl.bkkuc.cralibs.util;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ColorUtil {

    private static final Pattern HEX_PATTERN = Pattern.compile("#[a-fA-F0-9]{6}");

    /**
     * Преобразует строку с HEX-кодами цветов в формат, поддерживаемый Minecraft.
     * @param message строка, содержащая HEX-коды цветов.
     * @return строка с цветами в формате Minecraft.
     */
    public static String colorize(String message) {
        if (message == null || message.isEmpty()) return message;

        Matcher matcher = HEX_PATTERN.matcher(message);
        while (matcher.find()) {
            String hexCode = matcher.group();
            String replaceSharp = hexCode.replace('#', '&');

            StringBuilder builder = new StringBuilder("&x");
            for (char c : replaceSharp.substring(1).toCharArray()) {
                builder.append("&").append(c);
            }

            message = message.replace(hexCode, builder.toString());
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }
//    public static String colorize(String message) {
//        if(message == null || message.isEmpty()) return message;
//
//        Matcher matcher = HEX_PATTERN.matcher(message);
//        while (matcher.find()) {
//            String hexCode = matcher.group();
//            String replaceSharp = hexCode.replace('#', 'x');
//
//            StringBuilder builder = new StringBuilder();
//            for (char c : replaceSharp.toCharArray()) builder.append("&").append(c);
//
//            message = message.replace(hexCode, builder.toString());
//        }
//        return ChatColor.translateAlternateColorCodes('&', message.replaceAll("(?i)&x&([A-F0-9])&([A-F0-9])&([A-F0-9])&([A-F0-9])&([A-F0-9])&([A-F0-9])", "#$1$2$3$4$5$6"));
//    }

    /**
     * Преобразует список строк с HEX-кодами цветов в формат, поддерживаемый Minecraft.
     * @param texts список строк, содержащих HEX-коды цветов.
     * @return список строк с цветами в формате Minecraft.
     */
    public static List<String> colorize(List<String> texts) {
        if (texts == null || texts.isEmpty()) return new ArrayList<>();
        return texts.stream().map(ColorUtil::colorize).toList();
    }

    /**
     * Преобразует HEX-код цвета в объект Bukkit Color.
     * @param hex HEX-код цвета (например, "#FF5733" или "FF5733").
     * @return Объект Color, соответствующий HEX-коду.
     */
    public static @Nullable Color hexToColor(String hex) {
        if (hex == null || hex.isEmpty()) return null;

        if (hex.startsWith("#")) hex = hex.substring(1);
        if (hex.length() != 6) throw new IllegalArgumentException("Некорректный HEX-код цвета. Должен содержать 6 символов.");

        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);

        return Color.fromRGB(r, g, b);
    }

    /**
     * Преобразует объект Bukkit Color в HEX-код.
     * @param color Объект Color.
     * @return HEX-код цвета (например, "#FF5733").
     */
    public static @Nullable String colorToHex(Color color) {
        if (color == null) return null;

        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        return String.format("#%02X%02X%02X", r, g, b);
    }

    /**
     * Парсит объект цвета.
     *
     * @param color Объект цвета.
     * @return Объект Color.
     */
    public static @Nullable Color parseColor(Object color) {
        if (color == null) return null;
        if (color instanceof final Integer intColor) return Color.fromRGB(intColor);

        if (color instanceof String colorStr) {

            colorStr = colorStr.trim();

            if (colorStr.startsWith("#")) {
                try {
                    return Color.fromRGB(
                            Integer.valueOf(colorStr.substring(1, 3), 16),
                            Integer.valueOf(colorStr.substring(3, 5), 16),
                            Integer.valueOf(colorStr.substring(5, 7), 16)
                    );
                } catch (NumberFormatException ignored) {}
            }

            String[] rgbParts = colorStr.split(",");
            if (rgbParts.length == 3) {
                try {
                    int r = Integer.parseInt(rgbParts[0].trim());
                    int g = Integer.parseInt(rgbParts[1].trim());
                    int b = Integer.parseInt(rgbParts[2].trim());
                    return Color.fromRGB(r, g, b);
                } catch (NumberFormatException ignored) {}
            }

            try {
                int hexColor = Integer.parseInt(colorStr, 16);
                return Color.fromRGB((hexColor >> 16) & 0xFF, (hexColor >> 8) & 0xFF, hexColor & 0xFF);
            } catch (NumberFormatException ignored) {}
        }

        return null;
    }
}
