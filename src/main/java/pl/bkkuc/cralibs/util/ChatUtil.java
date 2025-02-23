package pl.bkkuc.cralibs.util;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Утилитарный класс для отправки сообщений игрокам и трансляции сообщений всем игрокам.
 */
@SuppressWarnings("unused")
public final class ChatUtil {

    /**
     * Отправляет сообщение всем онлайн игрокам.
     *
     * @param message Сообщение для отправки.
     */
    private static void sendToPlayers(String message) {
        if (message == null || message.isEmpty()) return;

        message = Util.safePAPI(null, message);
        message = ColorUtil.colorize(message);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(message);
        }

        Bukkit.getLogger().info("[Broadcast] " + message);
    }

    /**
     * Отправляет сообщение указанному отправителю.
     *
     * @param sender  Отправитель команды.
     * @param message Сообщение для отправки.
     */
    public static void sendMessage(CommandSender sender, String message) {
        if (sender == null || message == null || message.isEmpty()) return;

        message = Util.hasPlaceholderAPI() ? PlaceholderAPI.setPlaceholders(sender instanceof Player player ? player : null, message) : message;

        sender.sendMessage(ColorUtil.colorize(message));
    }

    /**
     * Отправляет несколько сообщений одному отправителю.
     *
     * @param sender   Отправитель команды.
     * @param messages Сообщения для отправки.
     */
    public static void sendMessage(CommandSender sender, String... messages) {
        if (sender == null || messages == null) return;

        String message = String.join("\n", messages);
        sendMessage(sender, message);
    }

    /**
     * Отправляет несколько сообщений одному отправителю.
     *
     * @param sender   Отправитель команды.
     * @param messages Сообщения для отправки.
     */
    public static void sendMessage(CommandSender sender, List<String> messages) {
        if (sender == null || messages == null) return;

        String message = String.join("\n", messages);
        sendMessage(sender, message);
    }

    /**
     * Транслирует сообщение всем онлайн игрокам.
     *
     * @param message           Сообщение для отправки.
     * @param applyPlaceholder  Применить плейсхолдеры к сообщению.
     */
    public static void broadcastMessage(String message, boolean applyPlaceholder) {
        if (message == null || message.isEmpty()) return;

        message = Util.hasPlaceholderAPI() && applyPlaceholder ? PlaceholderAPI.setPlaceholders(null, message) : message;
        sendToPlayers(message);
    }

    /**
     * Транслирует сообщение всем онлайн игрокам.
     *
     * @param message Сообщение для отправки.
     * @param placeholderPlayer Применить плейсхолдеры к сообщению.
     */
    public static void broadcastMessage(String message, Player placeholderPlayer) {
        if (message == null || message.isEmpty()) return;

        sendToPlayers(Util.safePAPI(placeholderPlayer, message));
    }

    /**
     * Транслирует сообщение всем онлайн игрокам.
     *
     * @param message Сообщение для отправки.
     */
    public static void broadcastMessage(String message) {
        broadcastMessage(message, true);
    }

    /**
     * Транслирует несколько сообщений всем онлайн игрокам.
     *
     * @param messages Сообщения для отправки.
     */
    public static void broadcastMessage(String... messages) {
        if (messages == null || messages.length == 0) return;

        String message = String.join("\n", messages);
        sendToPlayers(message);
    }

    /**
     * Транслирует несколько сообщений всем онлайн игрокам.
     *
     * @param messages Сообщения для отправки.
     */
    public static void broadcastMessage(List<String> messages) {
        if (messages == null || messages.isEmpty()) return;

        String message = String.join("\n", messages);
        sendToPlayers(message);
    }
}
