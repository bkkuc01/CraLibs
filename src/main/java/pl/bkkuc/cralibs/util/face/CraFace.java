package pl.bkkuc.cralibs.util.face;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Утилитный класс-перечисление для работы с направлениями
 *
 * @see CraDiagonalFace
 * @author bkkuc
 */
public enum CraFace {

    NORTH(180.0f),
    EAST (-90.0f),
    SOUTH(0.0f),
    WEST (90.0f);

    private final float yaw;

    CraFace(float yaw) {
        this.yaw = yaw;
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
     * Возвращает угол поворота по оси Y (yaw) для данного направления.
     *
     * @return конвертированный угол поворота по оси Y (yaw).
     */
    public float toYaw() {
        return getYaw();
    }

    /**
     * Возвращает yaw
     *
     * @return yaw.
     */
    public float getYaw() {
        return this.yaw;
    }

    /**
     * Возвращает следующее направление по часовой стрелке.
     *
     * @return Следующее направление по часовой стрелке.
     */
    public @NotNull CraFace getNextFace() {
        return switch (this) {
            case NORTH -> EAST;
            case EAST  -> SOUTH;
            case SOUTH -> WEST;
            case WEST  -> NORTH;
        };
    }

    /**
     * Возвращает предыдущее направление против часовой стрелки.
     *
     * @return Предыдущее направление против часовой стрелки.
     */
    public @NotNull CraFace getPreviousFace() {
        return switch (this) {
            case NORTH -> WEST;
            case EAST  -> NORTH;
            case SOUTH -> EAST;
            case WEST  -> SOUTH;
        };
    }

    /**
     * Возвращает противоположное направление.
     *
     * @return Противоположное направление.
     */
    public @NotNull CraFace getOppositeFace() {
        return switch (this) {
            case NORTH -> SOUTH;
            case EAST  -> WEST;
            case SOUTH -> NORTH;
            case WEST  -> EAST;
        };
    }

    /**
     * Определяет направление {@link CraFace} на основе угла поворота по оси Y (yaw).
     *
     * @param yaw Угол поворота по оси Y, значение которого обычно варьируется от -180 до 180 градусов.
     *            Значение 0 указывает на юг, положительные значения указывают на запад,
     *            а отрицательные — на восток.
     *            Например, yaw = 90 соответствует направлению на запад.
     * @return Направление {@link CraFace}, соответствующее углу поворота.
     *         Возвращает {@link CraFace#SOUTH} для угла в диапазоне от -45 до 45 градусов,
     *         {@link CraFace#WEST} для угла от 45 до 135 градусов,
     *         {@link CraFace#NORTH} для угла от 135 до 180 и от -180 до -135 градусов,
     *         и {@link CraFace#EAST} для угла от -135 до -45 градусов.
     */
    public static @NotNull CraFace fromYaw(float yaw) {
        if (yaw >= -45 && yaw < 45) {
            return SOUTH;
        } else if (yaw >= 45 && yaw < 135) {
            return WEST;
        } else if (yaw >= 135 || yaw < -135) {
            return NORTH;
        } else {
            return EAST;
        }
    }

    /**
     * Преобразует направление {@link Location} в соответствующее направление {@link CraFace}.
     *
     * @param location Локация в мире Minecraft, содержащая информацию об угле поворота (yaw).
     * @return Соответствующее направление {@link CraFace}, определяемое на основе угла поворота (yaw) локации.
     */
    public static @NotNull CraFace fromLocation(@NotNull Location location) {
        return fromYaw(location.getYaw());
    }

    /**
     * Преобразует направление {@link Entity} в соответствующее направление {@link CraFace}.
     *
     * @param entity Сущность в мире Minecraft, для которой определяется направление.
     *               Метод использует угол поворота сущности по оси Y.
     * @return Направление {@link CraFace}, соответствующее текущему углу поворота сущности.
     */
    public static @NotNull CraFace fromEntity(@NotNull Entity entity) {
        return fromYaw(entity.getLocation().getYaw());
    }

    /**
     * Преобразует направление {@link BlockFace} в соответствующее направление {@link CraFace}.
     *
     * @param blockFace Направление блока {@link BlockFace}, которое нужно преобразовать.
     *                  Метод поддерживает только основные направления: NORTH, EAST, SOUTH, WEST.
     * @return Соответствующее направление {@link CraFace}, или {@code null}, если направление не поддерживается.
     */
    public static @Nullable CraFace fromBlockFace(@NotNull BlockFace blockFace) {
        return fromBlockFace(blockFace, null);
    }

    /**
     * Преобразует направление {@link BlockFace} в соответствующее направление {@link CraFace}.
     *
     * @param blockFace Направление блока {@link BlockFace}, которое нужно преобразовать.
     *                  Метод поддерживает только основные направления: NORTH, EAST, SOUTH, WEST.
     * @return Соответствующее направление {@link CraFace}, или {@code defaultFace}, если направление не поддерживается.
     */
    public static @Nullable CraFace fromBlockFace(@NotNull BlockFace blockFace, @Nullable CraFace defaultValue) {
        return switch (blockFace) {
            case NORTH -> NORTH;
            case EAST -> EAST;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            default -> defaultValue;
        };
    }

    /**
     * Преобразует направление {@link CraFace} в соответствующее направление {@link BlockFace}.
     *
     * @return Соответствующее направление {@link BlockFace}.
     */
    public @NotNull BlockFace toBlockFace() {
        return switch (this) {
            case NORTH -> BlockFace.NORTH;
            case EAST  -> BlockFace.EAST;
            case SOUTH -> BlockFace.SOUTH;
            case WEST  -> BlockFace.WEST;
        };
    }
}
