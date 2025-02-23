package pl.bkkuc.cralibs.util;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public record Replacer(@NotNull String oldValue, @NotNull String newValue) {

    public @NotNull String replace(@NotNull String string) {
        return string.replace(oldValue, newValue);
    }

    public @NotNull List<@NotNull String> replace(@NotNull String... array) {
        return Arrays.stream(array).map(this::replace).toList();
    }

    public @NotNull List<@NotNull String> replace(@NotNull List<@NotNull String> list) {
        return list.stream().map(this::replace).toList();
    }
}
