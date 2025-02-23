package pl.bkkuc.cralibs.util;

import com.cryptomorin.xseries.XPotion;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Вспомогательный класс для работы с командами и плагинами.
 */
@SuppressWarnings("unused")
public final class Util {

    /**
     * Преобразует список строк в одну строку, где каждая строка разделена символом новой строки.
     **
     * @param list список строк, который не может быть null
     * @return строка, содержащая элементы списка, разделенные новой строкой (или пустая строка для пустого списка)
     */
    public static @NotNull String toString(@NotNull List<String> list) {
        list = list.stream().map(s -> s == null ? " " : s).toList();
        return String.join("\n", list);
    }

    /**
     * Получение отфильтрованного стека вызовов для указанного пакета.
     *
     * @param e           Исключение для анализа
     * @param packageName Пакет для фильтрации стека вызовов
     * @return Отфильтрованный стек вызовов в виде строки
     * <p>
     * Пример использования:
     * <p>
     * Если исключение `e` содержит следующий стек вызовов:
     * <p>
     * ```
     * at com.example.otherpackage.OtherClass.method(OtherClass.java:10)
     * at pl.bkkuc.cralibs.MyClass.myMethod(MyClass.java:25)
     * at pl.bkkuc.cralibs.AnotherClass.anotherMethod(AnotherClass.java:30)
     * at java.base/java.lang.Thread.run(Thread.java:832)
     * ```
     * <p>
     * И `packageName` равно `"pl.bkkuc.cralibs"`, то результатом будет:
     * <p>
     * ```
     * at pl.bkkuc.cralibs.MyClass.myMethod(MyClass.java:25)
     * at pl.bkkuc.cralibs.AnotherClass.anotherMethod(AnotherClass.java:30)
     * ```
     * <p>
     * Этот метод фильтрует стек вызовов, чтобы включать только те строки, которые содержат указанный пакет.
     */
    public static @NotNull String getFilteredStackTrace(final @NotNull Exception e, final @NotNull String packageName) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String stackTrace = sw.toString();

        return Arrays.stream(stackTrace.split("\n"))
                .filter(line -> line.contains(packageName))
                .collect(Collectors.joining("\n"));
    }

    /**
     * Создает PotionEffect из Map<String, Object>.
     * <p>
     * Пример, как будет выглядеть Map:
     * ```yaml
     * type: SPEED
     * duration: 600
     * amplifier: 1
     * ```
     *
     * @param data карта с данными эффекта зелья
     * @return созданный PotionEffect
     */
    public static @Nullable PotionEffect fromMap(final @NotNull Map<String, Object> data) {
        String typeName = (String) data.get("type");
        if(typeName == null) return null;

        int duration = (int) data.get("duration");
        int amplifier = (int) data.get("amplifier");

        XPotion xPotion = XPotion.matchXPotion(typeName.toUpperCase()).orElse(null);
        if(xPotion == null) return null;

        PotionEffectType type = xPotion.getPotionEffectType();
        if (type == null) return null;

        return new PotionEffect(type, duration, amplifier);
    }


    /**
     * Удаляет команду из командной карты Bukkit.
     *
     * @param commandName Имя команды для удаления.
     */
    public static void removeCommand(String commandName) {
        if(commandName == null || commandName.isEmpty()) return;

        CommandMap commandMap = Bukkit.getCommandMap();
        Command command = commandMap.getKnownCommands().get(commandName);
        if (command != null) {
            List<String> aliases = command.getAliases();
            command.unregister(commandMap);
            command.setAliases(new ArrayList<>());
            commandMap.getKnownCommands().remove(commandName);

            for (String alias : aliases) {
                removeCommand(alias);
            }
        }
    }

    /**
     * Проверяет, установлен ли плагин PlaceholderAPI.
     *
     * @return {@code true}, если PlaceholderAPI установлен; {@code false} в противном случае.
     */
    public static boolean hasPlaceholderAPI() {
        try {
            Class.forName("me.clip.placeholderapi.PlaceholderAPI");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Безопасно применяет PlaceholderAPI в предоставленной строке.
     * Возвращает null, если входная строка равна null.
     *
     * @param input Входная строка, содержащая плейсхолдеры.
     * @return Обработанная строка с разрешенными плейсхолдерами или null, если input равен null.
     */
    public static @Nullable String safePAPI(String input) {
        if (input == null) return null;
        return hasPlaceholderAPI() ? PlaceholderAPI.setPlaceholders(null, input) : input;
    }

    /**
     * Безопасно применяет PlaceholderAPI в предоставленном тексте для определенного игрока.
     * Возвращает null, если текст равен null или пуст.
     *
     * @param player Игрок, для которого нужно разрешить плейсхолдеры.
     * @param text   Текст, содержащий плейсхолдеры для разрешения.
     * @return Обработанная строка с разрешенными плейсхолдерами или исходный текст, если PlaceholderAPI недоступен или если text равен null или пуст.
     */
    public static @Nullable String safePAPI(Player player, String text) {
        if (text == null || text.isEmpty()) return null;
        return hasPlaceholderAPI() ? PlaceholderAPI.setPlaceholders(player, text) : text;
    }
}
