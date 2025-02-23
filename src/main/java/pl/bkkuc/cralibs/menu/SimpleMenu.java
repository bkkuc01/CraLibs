package pl.bkkuc.cralibs.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.bkkuc.cralibs.util.InventoryUtil;

import java.util.HashMap;
import java.util.Map;

public abstract class SimpleMenu implements Menu {

    private final Inventory inventory;
    private final Rows rows;

    private final Map<Integer, MenuItem> items = new HashMap<>();

    public SimpleMenu(@NotNull Rows rows, @Nullable String title) {
        this.inventory = InventoryUtil.createInventory(this, rows.getRows(), title);
        this.rows = rows;
    }

    @Override
    public final void click(@NotNull Player player, int slot, @Nullable MenuItemClickType clickType) {
        MenuItem menuItem = items.get(slot);
        if(menuItem == null) return;

        menuItem.click(player, clickType);
    }

    @Override
    public final void setItem(int slot, @Nullable MenuItem menuItem) {
        if (menuItem == null) {
            items.remove(slot);
            inventory.setItem(slot, new ItemStack(Material.AIR));
            return;
        }

        items.put(slot, menuItem);
        inventory.setItem(slot, menuItem.getItemStack());
    }


    @Override
    public abstract void initItems();

    @Override
    public final @NotNull Inventory getInventory() {
        return inventory;
    }

    public final @NotNull Rows getRows() {
        return rows;
    }

    public final @NotNull Map<Integer, MenuItem> getItems() {
        return Map.copyOf(items);
    }
}
