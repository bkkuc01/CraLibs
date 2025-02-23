package pl.bkkuc.cralibs.manager;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.bkkuc.cralibs.command.CraCommand;
import pl.bkkuc.cralibs.command.CraCommandManager;

import java.util.ArrayList;
import java.util.List;

public final class CommandRegistry {

    private final List<@NotNull CraCommandManager> commandManagers = new ArrayList<>();

    /**
     * Регистрация командного менеджера.
     *
     * @param commandManager Командный менеджер.
     */
    public void registerCommandManager(@NotNull CraCommandManager commandManager) {
        if(isRegisteredCommandManager(commandManager)) return;
        commandManagers.add(commandManager);
    }

    /**
     * Разрегистрация всех команд менеджера, и исключение из списка.
     *
     * @param commandManager Командный менеджер.
     */
    public void unregisterCommandManager(@NotNull CraCommandManager commandManager) {
        if(!isRegisteredCommandManager(commandManager)) return;

        for(final CraCommand craCommand : commandManager.getCommands()) {
            commandManager.unregisterCommand(craCommand);
        }
        commandManagers.remove(commandManager);
    }

    /**
     * Зарегистрирован ли командный менеджер.
     *
     * @param commandManager Командный менеджер
     * @return {@code true} если зарегистрирован.
     */
    public boolean isRegisteredCommandManager(@NotNull CraCommandManager commandManager) {
        return commandManagers.contains(commandManager);
    }

    /**
     * Возвращает менеджер команды по {@link JavaPlugin}.
     *
     * @param plugin Плагин
     * @return менеджер команды. Может вернуть {@code null} если менеджер команды по JavaPlugin не найден.
     */
    public @Nullable CraCommandManager getCommandManager(@NotNull JavaPlugin plugin) {
        return commandManagers
                .stream()
                .filter(commandManager -> commandManager.getPlugin().equals(plugin))
                .findFirst()
                .orElse(null);
    }

    /**
     * Возвращает список всех командных менеджеров.
     *
     * @return список
     */
    public @NotNull List<@NotNull CraCommandManager> getCommandManagers() {
        return List.copyOf(commandManagers);
    }
}
