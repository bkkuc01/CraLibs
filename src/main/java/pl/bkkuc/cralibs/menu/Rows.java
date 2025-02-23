package pl.bkkuc.cralibs.menu;

import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public enum Rows {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6);

    private final int rows;
    private final int size;

    Rows(int rows) {
        this.rows = rows;
        this.size = rows * 9;
    }

    public static @Nullable Rows of(int rows) {
        return Stream.of(values())
                .filter(row -> row.rows == rows)
                .findFirst()
                .orElse(null);
    }

    public static @Nullable Rows of(String rows) {

        int row;
        try {
            row = Integer.parseInt(rows);
        } catch (NumberFormatException e) {
            return null;
        }

        return of(row);
    }

    public static @Nullable Rows ofBySize(int size) {
        return Stream.of(values())
                .filter(row -> row.size == size)
                .findFirst()
                .orElse(null);
    }

    public static @NotNull Rows of(@NotNull Inventory inventory) {
        return of(inventory.getSize() / 9);
    }

    public int getRows() {
        return rows;
    }

    public int getSize() {
        return size;
    }
}
