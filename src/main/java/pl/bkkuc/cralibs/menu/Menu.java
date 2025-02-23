package pl.bkkuc.cralibs.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Menu extends InventoryHolder {

    // if null it's any click
    void click(@NotNull Player player, int slot, @Nullable MenuItemClickType clickType);

    void setItem(int slot, @Nullable MenuItem menuItem);

    void initItems();

    default void open(@Nullable Player player) {
        if(player == null) return;

        if(getOwner() != null && !player.equals(getOwner())) return;

        initItems();
        player.openInventory(getInventory());
    }

    @Nullable Player getOwner();
}
