package pl.bkkuc.cralibs.menu;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.bkkuc.cralibs.CraLibs;

import java.util.ArrayList;
import java.util.List;

public final class MenuRegistry {

    private final List<MenuManager> menuManagers = new ArrayList<>();

    public void registerMenuManager(@NotNull MenuManager menuManager) {
        if(menuManagers.contains(menuManager)) return;

        CraLibs.getInstance().getLogger().info("Registering menu manager: " + menuManager.getPlugin().getName() + "...");
        menuManagers.add(menuManager);
    }

    public void unregisterMenuManager(@NotNull MenuManager menuManager) {
        if(!menuManagers.contains(menuManager)) return;

        CraLibs.getInstance().getLogger().info("Unregistering menu manager: " + menuManager.getPlugin().getName() + "...");

        for(final Menu menu: menuManager.getMenus()) {
            menuManager.removeMenu(menu);
        }
        menuManagers.remove(menuManager);
    }

    public @Nullable MenuManager getMenuManager(@NotNull Menu menu) {
        return menuManagers.stream()
                .filter(menuManager -> menuManager.getMenus().contains(menu))
                .findFirst()
                .orElse(null);
    }


    public @Nullable MenuManager getMenuManager(@NotNull JavaPlugin plugin) {
        return menuManagers.stream()
                .filter(menuManager -> menuManager.getPlugin().equals(plugin))
                .findFirst()
                .orElse(null);
    }

    public @NotNull List<MenuManager> getMenuManagers() {
        return List.copyOf(menuManagers);
    }
}
