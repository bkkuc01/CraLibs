package pl.bkkuc.cralibs.menu;

import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum MenuItemClickType {
    LEFT_CLICK,
    RIGHT_CLICK,
    SHIFT_LEFT_CLICK,
    SHIFT_RIGHT_CLICK,
    MIDDLE_CLICK;

    public static @Nullable MenuItemClickType of(@NotNull ClickType clickType) {
        return switch (clickType) {
            case LEFT -> LEFT_CLICK;
            case RIGHT -> RIGHT_CLICK;
            case SHIFT_LEFT -> SHIFT_LEFT_CLICK;
            case SHIFT_RIGHT -> SHIFT_RIGHT_CLICK;
            case MIDDLE -> MIDDLE_CLICK;
            default -> null;
        };
    }
}
