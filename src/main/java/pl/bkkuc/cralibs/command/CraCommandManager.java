package pl.bkkuc.cralibs.command;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.bkkuc.cralibs.CraLibs;
import pl.bkkuc.cralibs.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * Менеджер команд для управления регистрацией и снятием команд плагина.
 *
 * <p>Пример использования данного класса:</p>
 *
 * <pre>{@code
 * // Представим что это ваш главный класс
 * public class Main extends JavaPlugin {
 *
 *     private CraCommandManager commandManager;
 *
 *     @Override
 *     public void onEnable() {
 *         // other code...
 *
 *         this.commandManager = new CraCommandManager(this);
 *         this.commandManager.registerCommand(new MyCommand());
 *     }
 * }
 * }</pre>
 */

@SuppressWarnings("unused")
public final class CraCommandManager {

    private final @NotNull JavaPlugin plugin;
    private final @NotNull List<CraCommand> commands;

    /**
     * Конструктор для создания менеджера команд.
     *
     * @param plugin Плагин, для которого создается менеджер команд.
     */
    public CraCommandManager(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        this.commands = new ArrayList<>();

        CraLibs.getInstance().getCommandRegistry().registerCommandManager(this);
    }

    /**
     * Регистрирует команду и добавляет ее в список команд.
     *
     * @param craCommand Команда для регистрации.
     */
    public void registerCommand(@NotNull CraCommand craCommand) {
        if(commands.contains(craCommand)) return;
        commands.add(craCommand);

        Util.removeCommand(craCommand.getName());

        CommandWrapper commandWrapper = new CommandWrapper(craCommand.getName(), craCommand);
        commandWrapper.setAliases(Arrays.stream(craCommand.getAliases()).toList());

        String name = plugin.getDescription().getName();

//        try {
//            name = plugin.getPluginMeta().getName();
//        } catch (NoSuchMethodError e) {
//            name = plugin.getDescription().getName();
//        }

        Bukkit.getCommandMap().register(name, commandWrapper);

        plugin.getLogger().info("[CommandManager] Loaded command: " + craCommand.getName());
    }

    /**
     * Снимает регистрацию команды и удаляет ее из списка команд.
     *
     * @param command Команда для снятия регистрации.
     */
    public void unregisterCommand(@NotNull CraCommand command) {
        if(!this.commands.remove(command)) return;

        Util.removeCommand(command.getName());
        plugin.getLogger().info("[CommandManager] Unloaded command: " + command.getName());
    }

    /**
     * Получает команду по имени или алиасу.
     *
     * @param nameOrAlias Имя или алиас команды.
     * @return Команда или {@code null}, если команда не найдена.
     */
    public @Nullable CraCommand getCommand(@NotNull String nameOrAlias) {
        return commands
                .stream()
                .filter(craCommand -> craCommand.getName().equalsIgnoreCase(nameOrAlias) || Arrays.stream(craCommand.getAliases()).toList().contains(nameOrAlias))
                .findFirst()
                .orElse(null);
    }

    /**
     * Возвращает команду, которая удовлетворяет заданному предикату.
     *
     * @param predicate условие, которому должна удовлетворять команда.
     *                  Не может быть null.
     * @return команду, которая удовлетворяет заданному предикату, или null, если такая команда не найдена.
     */
    public @Nullable CraCommand getCommand(@NotNull Predicate<CraCommand> predicate) {
        return commands
                .stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }

    /**
     * Получает копию списка всех зарегистрированных команд.
     *
     * @return Копия списка всех команд.
     */
    public @NotNull List<CraCommand> getCommands() {
        return List.copyOf(commands);
    }

    /**
     * Получает плагин, для которого был создан менеджер команд.
     *
     * @return Плагин.
     */
    public @NotNull JavaPlugin getPlugin() {
        return plugin;
    }
}
