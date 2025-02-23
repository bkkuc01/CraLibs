package pl.bkkuc.cralibs.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.bkkuc.cralibs.util.ChatUtil;

import java.util.List;

/**
 * Абстрактный класс для реализации команд в Minecraft плагинах.
 * Этот класс реализует интерфейсы CommandExecutor и TabCompleter,
 * что позволяет обрабатывать команды и автозаполнение для них.
 * <p>
 * Классы, расширяющие CraCommand, должны предоставить конкретную реализацию
 * методов execute и tabExecute.
 */
@SuppressWarnings("unused")
public abstract class CraCommand implements CommandExecutor, TabCompleter {

    private final String name;
    private String permission, permissionMessage, usage;
    private final int minArgs;
    private final String[] aliases;

    /**
     * Конструктор, который извлекает метаинформацию из аннотации {@link CommandMeta}.
     * Содержит информацию о названии команды, правах доступа, минимальном количестве аргументов и т.д.
     */
    public CraCommand() {
        CommandMeta command;
        try {
            command = getClass().getAnnotation(CommandMeta.class);
        } catch (NullPointerException e) {
            throw new ExceptionInInitializerError("Класс должен быть аннотирован с помощью @CommandMeta");
        }

        this.name = command.name();
        this.permission = command.permission();
        this.permissionMessage = command.permissionMessage();
        this.usage = command.usage();
        this.minArgs = command.minArgs();
        this.aliases = command.aliases();
    }

    /**
     * Метод, который должен быть реализован в подклассе для выполнения команды.
     *
     * @param sender Отправитель команды
     * @param args Аргументы команды
     */
    public abstract void execute(@NotNull CommandSender sender, @NotNull String[] args);

    /**
     * Метод для обработки автозаполнения команд. Может быть переопределен в подклассе.
     *
     * @param sender Отправитель команды
     * @param args Аргументы команды
     * @return Список предложений для автозаполнения
     */
    public abstract @Nullable List<String> tabExecute(@NotNull CommandSender sender, @NotNull String[] args);

    /**
     * Метод, вызываемый при выполнении команды.
     * Проверяет права доступа, количество аргументов и вызывает метод execute,
     * если условия выполнены.
     *
     * @param sender Отправитель команды
     * @param c Объект команды
     * @param label Метка команды
     * @param args Аргументы команды
     * @return true, если команда была обработана, false — если нет
     */
    @Override
    public final boolean onCommand(@NotNull CommandSender sender, @NotNull Command c, @NotNull String label, @NotNull String[] args) {

        if (!permission.isEmpty()) {
            if (!sender.hasPermission(permission)) {
                if (permissionMessage.isEmpty()) return true;
                sendMessage(sender, permissionMessage);
                return true;
            }
        }

        if (args.length < minArgs) {
            if (usage.isEmpty()) return true;
            sendMessage(sender, usage);
            return true;
        }

        execute(sender, args);
        return true;
    }

    /**
     * Метод для автозаполнения команды.
     * Проверяет аргументы и возвращает подходящие варианты автозаполнения.
     *
     * @param sender Отправитель команды
     * @param command Объект команды
     * @param label Метка команды
     * @param args Аргументы команды
     * @return Список предложений для автозаполнения
     */
    @Override
    public final @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!permission.isEmpty() && !sender.hasPermission(permission)) {
            return List.of();
        }

        List<String> tab = tabExecute(sender, args);
        if (tab == null) return List.of();

        return tab.stream().filter(s -> s != null && s.toLowerCase().contains(args[args.length - 1].toLowerCase())).toList();
    }

    /**
     * Получает список всех онлайн-игроков.
     *
     * @return Список имен всех онлайн-игроков.
     */
    protected final List<String> getOnlinePlayers() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
    }

    /**
     * Получает список всех онлайн-игроков, кроме отправителя команды.
     *
     * @param sender Отправитель команды
     * @return Список имен всех онлайн-игроков, кроме отправителя команды.
     */
    protected final List<String> getOnlinePlayersWithOut(@NotNull CommandSender sender) {
        return Bukkit.getOnlinePlayers().stream().filter(player -> !player.equals(sender)).map(Player::getName).toList();
    }

    /**
     * Проверяет, является ли отправитель команды и указанный игрок одним и тем же лицом.
     *
     * @param sender Отправитель команды
     * @param player Игрок для проверки
     * @return true, если отправитель и игрок одинаковы, false — в противном случае
     */
    protected final boolean same(@NotNull CommandSender sender, @Nullable Player player) {
        return sender.equals(player);
    }

    /**
     * Проверяет, является ли указанный игрок допустимой целью для команды.
     *
     * @param sender Отправитель команды
     * @param target Целевой игрок для проверки
     * @return true, если игрок допустим как цель для команды, false — в противном случае
     */
    protected final boolean testTarget(@NotNull CommandSender sender, @Nullable Player target) {
        return target != null && target.isOnline() && !sender.equals(target);
    }

    /**
     * Проверяет, является ли указанный игрок допустимой целью для команды.
     * Этот метод не требует отправителя команды и предполагает, что проверка происходит
     * в контексте, где отправитель команды не является обязательным.
     *
     * @param target Целевой игрок для проверки
     * @return true, если игрок допустим как цель для команды, false — в противном случае
     */
    protected final boolean testTarget(@Nullable Player target) {
        return target != null && target.isOnline();
    }

    /**
     * Отправляет сообщение отправителю команды.
     *
     * @param sender Отправитель команды
     * @param message Сообщение для отправки
     */
    protected final void sendMessage(@NotNull CommandSender sender, @NotNull String message) {
        ChatUtil.sendMessage(sender, message);
    }

    // Геттеры для получения информации о команде
    public final String getName() {
        return name;
    }

    public final String getPermission() {
        return permission;
    }

    public final String getPermissionMessage() {
        return permissionMessage;
    }

    public final String getUsage() {
        return usage;
    }

    public final int getMinArgs() {
        return minArgs;
    }

    public final String[] getAliases() {
        return aliases;
    }

    public final void setPermission(String permission) {
        this.permission = permission;
    }

    public final void setPermissionMessage(String permissionMessage) {
        this.permissionMessage = permissionMessage;
    }

    public final void setUsage(String usage) {
        this.usage = usage;
    }
}
