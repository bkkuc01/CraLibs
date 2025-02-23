package pl.bkkuc.cralibs.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public final class MenuItem {

    private final ItemStack itemStack;
    private final @Nullable MenuItemClickType clickType;
    private final @Nullable Consumer<Player> action;

    public MenuItem(@NotNull ItemStack item, @Nullable MenuItemClickType clickType, @Nullable Consumer<Player> action) {
        this.itemStack = item;
        this.clickType = clickType;
        this.action = action;
    }

    public MenuItem(@NotNull ItemStack item, @Nullable MenuItemClickType clickType) {
        this(item, clickType, null);
    }

    public MenuItem(@NotNull ItemStack item, @Nullable Consumer<Player> action) {
        this(item, null, action);
    }

    public void click(@NotNull Player player) {
        click(player, null);
    }

    public void click(@NotNull Player player, @Nullable MenuItemClickType clickType) {
        if (this.clickType == null || this.clickType == clickType) {
            if (action != null) {
                action.accept(player);
            }
        }
    }


    public @NotNull ItemStack getItemStack() {
        return itemStack;
    }

    public @Nullable MenuItemClickType getClickType() {
        return clickType;
    }

    public @Nullable Consumer<Player> getAction() {
        return action;
    }
}
