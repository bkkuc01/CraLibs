package pl.bkkuc.cralibs.menu;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class MenuManager {

    private final JavaPlugin plugin;
    private final List<Menu> menus = new ArrayList<>();

    public MenuManager(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void addMenu(@NotNull Menu menu) {
        if(menus.contains(menu)) return;

        menus.add(menu);
        if(menu.getOwner() == null) {
            menu.initItems();
        }
    }

    public void removeMenu(@NotNull Menu menu) {
        if(!menus.contains(menu)) return;

        menus.remove(menu);
        Bukkit.getOnlinePlayers().forEach(player -> {
            if(player.getOpenInventory().getTopInventory().equals(menu.getInventory())) {
                player.closeInventory();
            }
        });
    }

    public @NotNull JavaPlugin getPlugin() {
        return plugin;
    }

    public @NotNull List<@NotNull Menu> getMenus() {
        return List.copyOf(menus);
    }
}
