package pl.bkkuc.cralibs.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import pl.bkkuc.cralibs.CraLibs;

public final class MenuListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(final @NotNull InventoryClickEvent e) {
        final Inventory inventory = e.getClickedInventory();
        if(inventory == null || !(e.getWhoClicked().getOpenInventory().getTopInventory().equals(inventory)) || !(inventory.getHolder() instanceof final Menu menu)) return;
        if(CraLibs.getInstance().getMenuRegistry().getMenuManagers().stream().noneMatch(menuManager -> menuManager.getMenus().contains(menu))) return;

        final Player player       = (Player) e.getWhoClicked();
        final int slot            = e.getSlot();
        final ClickType clickType = e.getClick();

        e.setCancelled(true);
        menu.click(player, slot, MenuItemClickType.of(clickType));
    }
}
