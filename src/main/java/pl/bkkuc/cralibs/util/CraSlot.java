package pl.bkkuc.cralibs.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public enum CraSlot {
    X_Y,      // x,y
    FROM_TO,  // from-to
    SLOT;     // slot

    private static final Pattern X_Y_PATTERN = Pattern.compile("^\\d+,\\d+$");
    private static final Pattern FROM_TO_PATTERN = Pattern.compile("^\\d+-\\d+$");
    private static final Pattern SLOT_PATTERN = Pattern.compile("^\\d+$");

    public static @Nullable CraSlot fromString(String string) {
        if (string == null) return null;

        if (X_Y_PATTERN.matcher(string).matches()) {
            return X_Y;
        }

        if (FROM_TO_PATTERN.matcher(string).matches()) {
            return FROM_TO;
        }

        if (SLOT_PATTERN.matcher(string).matches()) {
            return SLOT;
        }

        return null;
    }

    public static @NotNull List<Integer> toSlots(String string) {
        if (string == null) return List.of();

        if (X_Y_PATTERN.matcher(string).matches()) {
            String[] parts = string.split(",");
            if (parts.length != 2) return List.of();

            try {
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);

                Integer slot = InventoryUtil.getSlotByXY(x, y);
                if (slot == null) return List.of();

                return List.of(slot);
            } catch (NumberFormatException e) {
                return List.of();
            }
        }

        if (FROM_TO_PATTERN.matcher(string).matches()) {
            String[] split = string.split("-");
            if (split.length != 2) return List.of();

            try {
                int from = Integer.parseInt(split[0]);
                int to = Integer.parseInt(split[1]);

                if (from > to) return List.of();

                return IntStream.rangeClosed(from, to)
                        .boxed()
                        .toList();
            } catch (NumberFormatException e) {
                return List.of();
            }
        }

        if (SLOT_PATTERN.matcher(string).matches()) {
            try {
                int slot = Integer.parseInt(string);
                return List.of(slot);
            } catch (NumberFormatException e) {
                return List.of();
            }
        }

        return List.of();
    }
}
