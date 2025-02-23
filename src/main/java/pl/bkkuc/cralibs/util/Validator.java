package pl.bkkuc.cralibs.util;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * Утилитарный класс для проверки различных условий.
 */
@SuppressWarnings("unused")
public final class Validator {

    private static final String NULL_MESSAGE = "%s cannot be null";
    private static final String EMPTY_MESSAGE = "%s cannot be empty";
    private static final String NULL_AND_EMPTY_MESSAGE = "%s cannot be empty or null";

    /**
     * Класс
     */
    public Validator() {}

    /**
     * Проверяет, что все переданные объекты не равны null.
     *
     * @param o объекты для проверки.
     * @return true, если ни один объект не равен null; false в противном случае.
     */
    public static boolean valid(Object... o) {
        return Arrays.stream(o).noneMatch(Objects::isNull);
    }

    /**
     * Проверяет, что объект не равен null.
     *
     * @param object Проверяемый объект.
     * @param name   Имя объекта для сообщения об ошибке.
     * @throws IllegalArgumentException если объект равен null.
     */
    public static void notNull(Object object, @NotNull String name) {
        if (object == null) throw new IllegalArgumentException(String.format(NULL_MESSAGE, name));
    }

    /**
     * Проверяет, что строка не равна null.
     *
     * @param string Проверяемая строка.
     * @param name   Имя строки для сообщения об ошибке.
     * @throws IllegalArgumentException если строка равна null.
     */
    public static void notEmpty(String string, @NotNull String name) {
        if (string == null) throw new IllegalArgumentException(String.format(EMPTY_MESSAGE, name));
    }

    /**
     * Проверяет, что строка не равна null и не пустая.
     *
     * @param string Проверяемая строка.
     * @param name   Имя строки для сообщения об ошибке.
     * @throws IllegalArgumentException если строка равна null или пустая.
     */
    public static void notNullOrEmpty(String string, String name) {
        if (string == null || string.isEmpty()) throw new IllegalArgumentException(String.format(NULL_AND_EMPTY_MESSAGE, name));
    }

    /**
     * Проверяет, что материал не равен null и не является воздухом (Material.AIR).
     *
     * @param material Проверяемый материал.
     * @throws IllegalArgumentException если материал равен null или является воздухом.
     */
    public static void notNullOrAir(Material material) {
        notNull(material, "Material");
        if (material.name().endsWith("_AIR")) throw new IllegalArgumentException(String.format(NULL_AND_EMPTY_MESSAGE, material.name()));
    }
}
