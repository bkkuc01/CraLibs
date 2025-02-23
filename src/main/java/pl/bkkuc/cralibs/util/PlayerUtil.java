package pl.bkkuc.cralibs.util;

import com.cryptomorin.xseries.messages.ActionBar;
import com.cryptomorin.xseries.messages.Titles;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * Утилитный класс для работы с игроками в Minecraft.
 * Содержит методы для управления инвентарем и отправки сообщений.
 */
@SuppressWarnings("unused")
public final class PlayerUtil {

    /**
     * Исполнить разброс для игрока с переданной силой разброса.
     *
     * @param player Игрок
     * @param recoil Сила разброса
     */
    public static void recoil(Player player, double recoil) {
        if(!Validator.valid(player)) return;

        float currentPitch = player.getLocation().getPitch();
        float newPitch = (float) (currentPitch - recoil);

        try {
            player.setRotation(player.getLocation().getYaw(), newPitch);
        } catch (NoSuchMethodError exception) {
            throw new UnsupportedOperationException("Method Player#setRotation is not supported by core");
        }
    }

    /**
     * Передает предмет игроку. Если инвентарь игрока полон, предмет будет сброшен на землю.
     *
     * @param player игрок, которому нужно передать предмет.
     * @param item   предмет для передачи.
     */
    public static void giveItem(Player player, ItemStack item) {
        if(!Validator.valid(player, item) || item.getType() == Material.AIR) return;
        if(player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItemNaturally(player.getLocation(), item);
        }
        else {
            player.getInventory().addItem(item);
        }
    }

    /**
     * Передает несколько предметов игроку.
     *
     * @param player игрок, которому нужно передать предметы.
     * @param items  массив предметов для передачи.
     */
    public static void giveItem(Player player, ItemStack... items) {
        Arrays.stream(items).forEach(item -> giveItem(player, item));
    }

    /**
     * Удаляет у игрока определенное количество предметов определенного типа.
     *
     * @param player   игрок, у которого нужно удалить предметы.
     * @param material материал, который нужно удалить.
     * @param amount   количество предметов для удаления.
     *
     * @return количество удаленных предметов
     */
    public static int removeItem(Player player, Material material, int amount) {
        return removeItem(player, item -> item.getType() == material, amount);
    }

    /**
     * Удаляет у игрока предметы по предикату.
     *
     * @param player    игрок, у которого нужно удалить предметы.
     * @param predicate предикат, по которому нужно удалить предметы.
     * @param amount    количество предметов для удаления.
     *
     * @return количество удаленных предметов
     */
    public static int removeItem(Player player, Predicate<ItemStack> predicate, int amount) {
        return InventoryUtil.removeItem(player.getInventory(), predicate, amount);
    }

    /**
     * Удаляет у игрока все предметы по фильтру.
     *
     * @param player   игрок, у которого нужно удалить предметы.
     * @param filter   фильтрация.
     *
     * @return количество удаленных предметов
     */
    public static int removeItem(Player player, Predicate<ItemStack> filter) {
        return InventoryUtil.removeItem(player.getInventory(), filter);
    }

    /**
     * Отправляет игроку сообщение в чат.
     *
     * @param player  игрок, которому нужно отправить сообщение.
     * @param message сообщение для отправки.
     */
    public static void sendMessage(Player player, String message) {
        if(!Validator.valid(player, message) || message.isEmpty()) return;
        message = Util.safePAPI(player, message);
        player.sendMessage(ColorUtil.colorize(message));
    }

    /**
     * Отправляет игроку несколько сообщений в чат.
     *
     * @param player   игрок, которому нужно отправить сообщения.
     * @param messages список сообщений для отправки.
     */
    public static void sendMessage(Player player, List<String> messages) {
        if(!Validator.valid(player, messages) || messages.isEmpty()) return;

        messages = messages.stream().map(message -> Util.safePAPI(ColorUtil.colorize(message)) ).toList();

        String message = String.join("\n", messages);
        player.sendMessage(message);
    }

    /**
     * Отправляет игроку сообщение в ActionBar.
     *
     * @param player  игрок, которому нужно отправить сообщение.
     * @param message сообщение для отправки.
     */
    public static void sendActionBar(Player player, String message) {
        if(!Validator.valid(player, message) || message.isEmpty()) return;

        message = ColorUtil.colorize(Util.safePAPI(player, message));
        try {
            player.sendActionBar(message);
        } catch (Exception ignored) {
            ActionBar.sendActionBar(player, message);
        }
    }

    /**
     * Отправляет игроку заголовок и подзаголовок.
     *
     * @param player    игрок, которому нужно отправить заголовок и подзаголовок.
     * @param title     заголовок.
     * @param subtitle  подзаголовок.
     * @param fadeIn    время появления заголовка.
     * @param stay      время, в течение которого заголовок будет отображаться.
     * @param fadeOut   время исчезновения заголовка.
     */
    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        if(!Validator.valid(player) || (title == null && subtitle == null)) return;

        title = ColorUtil.colorize(Util.safePAPI(player, title));
        subtitle = ColorUtil.colorize(Util.safePAPI(player, subtitle));

        if(fadeIn == -1 && stay == -1 && fadeOut == -1) {
            try {
                player.sendTitle(title, subtitle);
            } catch (NoSuchMethodError e) {
                Titles.sendTitle(player, title, subtitle);
            }
        }
        else {
            try {
                player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
            } catch (NoSuchMethodError e) {
                Titles.sendTitle(player, fadeIn, stay, fadeOut, title, subtitle);
            }
        }
    }

    /**
     * Отправляет игроку заголовок и подзаголовок с дефолтными значениями для времени показа.
     *
     * @param player    игрок, которому нужно отправить заголовок и подзаголовок.
     * @param title     заголовок.
     * @param subtitle  подзаголовок.
     */
    public static void sendTitle(Player player, String title, String subtitle) {
        sendTitle(player, title, subtitle, -1, -1, -1);
    }
}
