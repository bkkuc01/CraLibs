package pl.bkkuc.cralibs;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pl.bkkuc.cralibs.command.CraCommandManager;
import pl.bkkuc.cralibs.manager.CommandRegistry;
import pl.bkkuc.cralibs.menu.MenuListener;
import pl.bkkuc.cralibs.menu.MenuManager;
import pl.bkkuc.cralibs.menu.MenuRegistry;

public final class CraLibs extends JavaPlugin implements Listener {

    private static CraLibs instance;

    private CommandRegistry commandRegistry;
    private MenuRegistry menuRegistry;

    @Override
    public void onEnable() {
        instance = this;

        this.commandRegistry = new CommandRegistry();
        this.menuRegistry = new MenuRegistry();

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
    }

    @Override
    public void onDisable() {
        if(this.commandRegistry != null) {
            getLogger().warning("Unregistering Command Managers and Commands...");

            commandRegistry.getCommandManagers().forEach(commandRegistry::unregisterCommandManager);
        }

        if(this.menuRegistry != null) {
            getLogger().warning("Unregistering menus...");

            menuRegistry.getMenuManagers().forEach(menuRegistry::unregisterMenuManager);
        }
    }

    @EventHandler
    private void onPluginDisable(@NotNull PluginDisableEvent e){
        CraCommandManager commandManager = commandRegistry.getCommandManager((JavaPlugin) e.getPlugin());
        if(commandManager != null) {
            commandRegistry.unregisterCommandManager(commandManager);
        }

        MenuManager menuManager = menuRegistry.getMenuManager((JavaPlugin) e.getPlugin());
        if(menuManager != null) {
            menuRegistry.unregisterMenuManager(menuManager);
        }
    }

    public static CraLibs getInstance() {
        return instance;
    }

    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

    public MenuRegistry getMenuRegistry() {
        return menuRegistry;
    }
}
