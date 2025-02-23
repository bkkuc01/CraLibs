package pl.bkkuc.cralibs.util.face;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Утилитный класс-перечисление для работы с направлениями
 *
 * @see CraFace
 *
 * @author bkkuc
 */
public enum CraDiagonalFace {
    NORTH_EAST(135.0f),
    NORTH_WEST(-135.0f),
    SOUTH_EAST(45.0f),
    SOUTH_WEST(-45.0f);

    private final float yaw;

    CraDiagonalFace(float yaw) {
        this.yaw = yaw;
    }

    /**
     * Возвращает угол поворота по оси Y (yaw) для данного направления.
     *
     * @return yaw
     */
    public float getYaw() {
        return yaw;
    }

    /**
     * Конвертирует угол поворота по оси Y (yaw)
     *
     * @return yaw
     */
    public float toYaw() {
        return getYaw();
    }

    /**
     * Устанавливает угол поворота по оси Y (yaw) для переданной локации.
     *
     * @param location Локация.
     */
    public void setFace(@NotNull Location location) {
        location.setYaw(this.toYaw());
    }

    /**
     * Устанавливает угол поворота по оси Y (yaw) для переданной сущности.
     *
     * @param entity Сущность.
     */
    public void setFace(@NotNull Entity entity) {
        entity.setRotation(this.toYaw(), 0);
    }

    /**
     * Возвращает следующее диагональное направление по часовой стрелке.
     *
     * @return Следующее диагональное направление по часовой стрелке.
     */
    public @NotNull CraDiagonalFace getNextFace() {
        return switch (this) {
            case NORTH_EAST -> SOUTH_EAST;
            case SOUTH_EAST -> SOUTH_WEST;
            case SOUTH_WEST -> NORTH_WEST;
            case NORTH_WEST -> NORTH_EAST;
        };
    }

    /**
     * Возвращает предыдущее диагональное направление против часовой стрелки.
     *
     * @return Предыдущее диагональное направление против часовой стрелки.
     */
    public @NotNull CraDiagonalFace getPreviousFace() {
        return switch (this) {
            case NORTH_EAST -> NORTH_WEST;
            case NORTH_WEST -> SOUTH_WEST;
            case SOUTH_WEST -> SOUTH_EAST;
            case SOUTH_EAST -> NORTH_EAST;
        };
    }

    /**
     * Возвращает противоположное диагональное направление.
     *
     * @return Противоположное диагональное направление.
     */
    public @NotNull CraDiagonalFace getOppositeFace() {
        return switch (this) {
            case NORTH_EAST -> SOUTH_WEST;
            case SOUTH_EAST -> NORTH_WEST;
            case SOUTH_WEST -> NORTH_EAST;
            case NORTH_WEST -> SOUTH_EAST;
        };
    }

    /**
     * Определяет диагональное направление {@link CraDiagonalFace} на основе угла поворота по оси Y (yaw).
     *
     * @param yaw Угол поворота по оси Y, значение которого обычно варьируется от -180 до 180 градусов.
     *            Значение 0 указывает на юг, положительные значения указывают на запад,
     *            а отрицательные — на восток.
     *            Например, yaw = 90 соответствует направлению на запад.
     * @return Направление {@link CraDiagonalFace}, соответствующее углу поворота.
     *         Возвращает {@link CraDiagonalFace#SOUTH_EAST} для угла в диапазоне от -45 до 45 градусов,
     *         {@link CraDiagonalFace#SOUTH_WEST} для угла от 45 до 135 градусов,
     *         {@link CraDiagonalFace#NORTH_WEST} для угла от 135 до 180 и от -180 до -135 градусов,
     *         и {@link CraDiagonalFace#NORTH_EAST} для угла от -135 до -45 градусов.
     */
    public static @NotNull CraDiagonalFace fromYaw(float yaw) {
        if(yaw >= -90 && yaw <= 0) {
            return SOUTH_EAST;
        }
        else if(yaw >= 0 && yaw <= 90) {
            return SOUTH_WEST;
        }
        else if(yaw >= 90 && yaw <= 180) {
            return NORTH_WEST;
        }
        else /*if(yaw >= -180 && yaw < -90)*/ {
            return NORTH_EAST;
        }
    }

    /**
     * Преобразует направление {@link BlockFace} в соответствующее направление {@link CraDiagonalFace}.
     *
     * @param blockFace Направление блока {@link BlockFace}, которое нужно преобразовать.
     * @return Соответствующее направление {@link CraDiagonalFace}, или {@code null}, если направление не поддерживается.
     */
    public static @Nullable CraDiagonalFace fromBlockFace(@NotNull BlockFace blockFace) {
        return fromBlockFace(blockFace, null);
    }

    /**
     * Преобразует направление {@link BlockFace} в соответствующее направление {@link CraDiagonalFace}.
     *
     * @param blockFace Направление блока {@link BlockFace}, которое нужно преобразовать.
     * @return Соответствующее направление {@link CraDiagonalFace}, или {@code defaultFace}, если направление не поддерживается.
     */
    public static @Nullable CraDiagonalFace fromBlockFace(@NotNull BlockFace blockFace, @Nullable CraDiagonalFace defaultValue) {
        return switch (blockFace) {
            case NORTH_EAST -> NORTH_EAST;
            case NORTH_WEST -> NORTH_WEST;
            case SOUTH_EAST -> SOUTH_EAST;
            case SOUTH_WEST -> SOUTH_WEST;
            default -> defaultValue;
        };
    }

    /**
     * Преобразует направление {@link CraDiagonalFace} в соответствующее направление {@link BlockFace}.
     *
     * @return Соответствующее направление {@link BlockFace}.
     */
    public @NotNull BlockFace toBlockFace() {
        return switch (this) {
            case NORTH_EAST -> BlockFace.NORTH_EAST;
            case NORTH_WEST -> BlockFace.NORTH_WEST;
            case SOUTH_EAST -> BlockFace.SOUTH_EAST;
            case SOUTH_WEST -> BlockFace.SOUTH_WEST;
        };
    }
}