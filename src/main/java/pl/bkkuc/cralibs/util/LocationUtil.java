package pl.bkkuc.cralibs.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public final class LocationUtil {

    /**
     * Возвращает случайную локацию в указанном диапазоне.
     *
     * @param world  мир
     * @param minX   минимальный X
     * @param minZ   минимальный Z
     * @param maxX   максимальный X
     * @param maxZ   максимальный Z
     * @return случайную локацию
     */
    public static @NotNull Location getRandomLocation(World world, double minX, double minZ, double maxX, double maxZ) {
        Validator.notNull(world, "World");

        double x = RandomUtil.randomDouble(minX, maxX);
        double z = RandomUtil.randomDouble(minZ, maxZ);

        double y = world.getHighestBlockYAt((int) x, (int) z) + 1;

        return new Location(world, x, y, z);
    }

    /**
     * Возвращает случайную локацию в указанном диапазоне с фильтром.
     *
     * @param filter фильтр
     * @param attempts количество попыток
     * @param world  мир
     * @param minX   минимальный X
     * @param minZ   минимальный Z
     * @param maxX   максимальный X
     * @param maxZ   максимальный Z
     * @return случайную локацию или null если ничего не нашли
     */
    public static @Nullable Location getRandomLocation(Predicate<Location> filter, int attempts, World world, double minX, double minZ, double maxX, double maxZ) {
        if(filter == null) {
            return getRandomLocation(world, minX, minZ, maxX, maxZ);
        }
        else {
            while (attempts > 0) {
                Location location = getRandomLocation(world, minX, minZ, maxX, maxZ);
                if (filter.test(location)) {
                    return location;
                }
                attempts--;
            }
        }

        return null;
    }

    /**
     * Конвертирует переданную локацию в {@link String}.
     *
     * <p>
     *     Пример: world;56;77;12
     * </p>
     *
     * @param location Локация для конвертации
     * @param splitter Разделитель
     * @param includeFacing Включать ли Yaw и Pitch
     * @return конвертированную локацию.
     */
    public static @NotNull String convert(@NotNull Location location, @NotNull String splitter, boolean includeFacing) {
        if(splitter.isEmpty()) splitter = ";";

        return location.getWorld().getName() + splitter + location.getBlockX() + splitter + location.getBlockY() + splitter + location.getBlockZ() + (includeFacing ? splitter + location.getYaw() + splitter + location.getPitch() : "");
    }

    /**
     * Конвертирует глубоко переданную локацию в {@link String}.
     *
     * <p>
     *     Пример: world;56.500;77.000000;12.500
     * </p>
     *
     * @param location Локация для конвертации
     * @param splitter Разделитель
     * @param includeFacing Включать ли Yaw и Pitch
     * @return конвертированную локацию.
     */
    public static @NotNull String deepConvert(@NotNull Location location, @NotNull String splitter, boolean includeFacing) {
        if(splitter.isEmpty()) splitter = ";";

        return location.getWorld().getName() + splitter + location.getX() + splitter + location.getY() + splitter + location.getZ() + (includeFacing ? splitter + location.getYaw() + splitter + location.getPitch() : "");
    }

    /**
     * Де конвертирует строку обратно в {@link Location}.
     *
     * @param string Локация в виде строки
     * @param splitter Разделитель
     * @return Локация, извлеченная из строки
     *
     * @throws IllegalArgumentException Если неверный формат строки
     */
    public static @NotNull Location fromString(@NotNull String string, @NotNull String splitter) {
        if (splitter.isEmpty()) splitter = ";";

        String[] parts = string.split(splitter);

        if (parts.length < 4) {
            throw new IllegalArgumentException("Invalid location string format");
        }

        World world = Bukkit.getWorld(parts[0]);
        double x = Double.parseDouble(parts[1]);
        double y = Double.parseDouble(parts[2]);
        double z = Double.parseDouble(parts[3]);

        if (parts.length > 4) {
            float yaw = Float.parseFloat(parts[4]);
            float pitch = Float.parseFloat(parts[5]);
            return new Location(world, x, y, z, yaw, pitch);
        } else {
            return new Location(world, x, y, z);
        }
    }

    /**
     * Возвращает соседний блок в зависимости от указанного направления.
     *
     * @param block Блок, для которого нужно найти соседний блок.
     * @param face  Сторона блока (BlockFace), для которой нужно найти соседний блок.
     * @return Соседний блок, расположенный в указанном направлении.
     */
    public static @NotNull Block getAdjacentBlock(@NotNull Block block, @NotNull BlockFace face) {
        return block.getRelative(face);
    }

    /**
     * Возвращает центральную локацию указанной стороны блока.
     *
     * @param block Блок, для которого нужно найти центральную локацию стороны.
     * @param face  Сторона блока (BlockFace), для которой нужно найти центральную локацию.
     * @return Локация, представляющая центральную точку указанной стороны блока.
     */
    public static @NotNull Location getBlockFaceLocation(@NotNull Block block, @NotNull BlockFace face) {
        Location loc = block.getLocation().clone().toCenterLocation();

        switch (face) {
            case UP -> loc.add(0, 0.5, 0);
            case DOWN -> loc.add(0, -0.5, 0);
            case NORTH -> loc.add(0, 0, -0.5);
            case SOUTH -> loc.add(0, 0, 0.5);
            case WEST -> loc.add(-0.5, 0, 0);
            case EAST -> loc.add(0.5, 0, 0);
            case NORTH_WEST -> loc.add(-0.5, 0, -0.5);
            case NORTH_EAST -> loc.add(0.5, 0, -0.5);
            case SOUTH_WEST -> loc.add(-0.5, 0, 0.5);
            case SOUTH_EAST -> loc.add(0.5, 0, 0.5);
            default -> {}
        }

        return loc;
    }

    /**
     * Возвращает список игроков в заданном диапазоне.
     *
     * @param center Локация от куда нужно проверять
     * @param minRadius Начиная от
     * @param maxRadius Завершая до
     * @return список отфильтрованных игроков.
     */
    public static @NotNull List<Player> getPlayersInRadius(@NotNull Location center, double minRadius, double maxRadius) {
        return center.getNearbyPlayers(maxRadius).stream()
                .filter(player -> center.distance(player.getLocation()) >= minRadius)
                .toList();
    }

    /**
     * Возвращает список сущности в заданном диапазоне.
     *
     * @param center Локация от куда нужно проверять
     * @param minRadius Начиная от
     * @param maxRadius Завершая до
     * @return список отфильтрованных сущности.
     */
    public static @NotNull List<Entity> getEntitiesInRadius(@NotNull Location center, double minRadius, double maxRadius) {
        return center.getNearbyEntities(maxRadius, maxRadius, minRadius).stream()
                .filter(entity -> center.distance(entity.getLocation()) >= minRadius)
                .toList();
    }

    /**
     * Возвращает список живых сущности в заданном диапазоне.
     *
     * @param center Локация от куда нужно проверять
     * @param minRadius Начиная от
     * @param maxRadius Завершая до
     * @return список отфильтрованных живых сущности.
     */
    public static @NotNull List<LivingEntity> getLivingEntitiesInRadius(@NotNull Location center, double minRadius, double maxRadius) {
        return getEntitiesInRadius(center, minRadius, maxRadius).stream().filter(entity -> entity instanceof LivingEntity).map(entity -> (LivingEntity) entity).toList();
    }
}
