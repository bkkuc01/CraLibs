package pl.bkkuc.cralibs.util;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.bkkuc.cralibs.item.ItemBuilder;
import pl.bkkuc.cralibs.nms.NBTEditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Утилитный класс для работы с инвентарем в Bukkit API.
 * Предоставляет методы для получения пустых слотов, случайного выбора слотов и создания инвентарей.
 */
@SuppressWarnings("unused")
public final class InventoryUtil {

    public static final Map<@NotNull Integer, Integer> ROWS_TO_SLOT_COUNT = Map.of
            (
                    1, 9,
                    2, 18,
                    3, 27,
                    4, 36,
                    5, 45,
                    6, 54
            );

    /**
     * Заполняет случайные пустые слоты инвентаря предметами из списка,
     * учитывая их вероятность выпадения.
     *
     * @param inventory     Инвентарь, который будет заполняться.
     * @param items         Список пар (ItemStack, Integer), где ItemStack — предмет,
     *                      а Integer — шанс выпадения (0-100).
     * @param limitedItems  Если true, предмет удаляется из списка после выпадения
     *                      (ограниченный запас). Если false, предмет может выпадать
     *                      несколько раз (неограниченный запас).
     */
    public static void inventoryRandomFill(
            @NotNull Inventory inventory,
            @NotNull List<Pair<ItemStack, Integer>> items,
            boolean limitedItems) {

        List<Integer> emptySlots = getEmptySlots(inventory);
        if (emptySlots.isEmpty() || items.isEmpty()) return;

        ThreadLocalRandom random = ThreadLocalRandom.current();
        List<Pair<ItemStack, Integer>> availableItems = new ArrayList<>(items);

        for (Integer slot : emptySlots) {
            if (availableItems.isEmpty()) break;

            Pair<ItemStack, Integer> entry = availableItems.get(random.nextInt(availableItems.size()));
            ItemStack item = entry.getLeft();
            int chance = entry.getRight();

            if (random.nextInt(100) < chance) {
                inventory.setItem(slot, item.clone());

                if (limitedItems) {
                    availableItems.remove(entry);
                }
            }
        }
    }

    /**
     * Десериализует инвентарь из конфигурации, добавляя в него предметы, указанные в секции "items".
     * Предметы могут быть размещены по указанным слотам, и если активирован параметр applyNBTTag,
     * к каждому предмету добавляется соответствующий NBT-тег.
     *
     * @param inventoryHolder Хранитель инвентаря (может быть, например, игрок или контейнер).
     * @param section Конфигурация, содержащая информацию об инвентаре, включая заголовок, количество строк и предметы.
     * @param applyNBTTag Флаг, указывающий, нужно ли применять NBT-теги к предметам.
     *                    Если true, каждому предмету будет добавлен NBT-тег с его идентификатором.
     * @return Десериализованный инвентарь, содержащий предметы в соответствии с конфигурацией.
     */
    public static @NotNull Inventory deserialize(@NotNull InventoryHolder inventoryHolder, @NotNull ConfigurationSection section, boolean applyNBTTag) {
        final String title = section.getString("title");
        final int rows = section.getInt("rows", section.getInt("size", 9) / 9);

        final Inventory inventory = createInventory(inventoryHolder, rows, title);

        final ConfigurationSection itemsSection = section.getConfigurationSection("items");
        if(itemsSection != null) {
            for(final String key : itemsSection.getKeys(false)) {
                final ConfigurationSection keySection = itemsSection.getConfigurationSection(key);
                if(keySection == null) continue;

                final ItemBuilder builder = ItemBuilder.deserialize(keySection);
                if(builder == null) continue;

                ItemStack item = builder.build();
                if(applyNBTTag) item = NBTEditor.set(item, key, "type");

                final List<Integer> slots = new ArrayList<>();

                final String slotString = keySection.getString("slot");
                if(slotString != null) {
                    slots.addAll(CraSlot.toSlots(slotString));
                }
                else {
                    final List<String> slotsList = keySection.getStringList("slots");
                    if(slotsList.isEmpty()) continue;

                    for(final String slotKey : slotsList) {
                        slots.addAll(CraSlot.toSlots(slotKey));
                    }
                }

                slots.removeIf(slot -> slot == null || slot < 0 || slot >= inventory.getSize());
                if(slots.isEmpty()) continue;

                for(final Integer slot : slots) {
                    inventory.setItem(slot, item);
                }
            }
        }

        return inventory;
    }

    /**
     * Безопасно помещает предмет в инвентарь.
     *
     * @param inventory инвентарь
     * @param slots     список слотов
     * @param item      предмет
     * @return количество предметов который не вместилось.
     */
    public static int safetyPut(@NotNull Inventory inventory, @NotNull List<Integer> slots, @NotNull ItemStack item) {
        int remainingAmount = item.getAmount();

        for (Integer slot : slots) {
            if (slot == null || remainingAmount <= 0) break;

            if (slot < 0 || slot >= inventory.getSize()) continue;

            ItemStack currentItem = inventory.getItem(slot);

            if (currentItem == null) {
                int amountToAdd = Math.min(remainingAmount, item.getMaxStackSize()); // Изменение: Введена переменная amountToAdd
                inventory.setItem(slot, new ItemStack(item.getType(), amountToAdd));
                remainingAmount -= amountToAdd;
            } else if (currentItem.isSimilar(item)) {
                int space = currentItem.getMaxStackSize() - currentItem.getAmount();
                int toAdd = Math.min(remainingAmount, space);
                currentItem.setAmount(currentItem.getAmount() + toAdd);
                inventory.setItem(slot, currentItem);
                remainingAmount -= toAdd;
            }
        }

        return remainingAmount;
    }

    /**
     * Безопасно помещает несколько предметов в инвентарь.
     *
     * @param inventory инвентарь
     * @param slots     слоты куда нужно поместить
     * @param items     список предметов
     * @return предмет с количеством которые не вместилось
     */
    public static @NotNull Map<ItemStack, Integer> safetyPutAll(@NotNull Inventory inventory, @NotNull List<Integer> slots, @NotNull List<ItemStack> items) {
        Map<ItemStack, Integer> result = new HashMap<>();

        for (ItemStack item : items) {
            int remainingAmount = safetyPut(inventory, slots, item);
            if (remainingAmount > 0) {
                result.put(item, remainingAmount);
            }
        }

        return result;
    }

    /**
     * Удаляет у инвентаря определенное количество предметов определенного типа.
     *
     * @param inventory  инвентарь, у которого нужно удалить предметы.
     * @param material   материал, который нужно удалить.
     * @param amount     количество предметов для удаления.
     *
     * @return количество удаленных предметов
     */
    public static int removeItem(Inventory inventory, Material material, int amount) {
        return removeItem(inventory, item -> item.getType() == material, amount);
    }

    /**
     * Удаляет у инвентаря предметы по предикату.
     *
     * @param inventory инвентарь, у которого нужно удалить предметы.
     * @param predicate предикат, по которому нужно удалить предметы.
     * @param amount    количество предметов для удаления.
     *
     * @return количество удаленных предметов
     */
    public static int removeItem(Inventory inventory, Predicate<ItemStack> predicate, int amount) {
        if (!Validator.valid(inventory, predicate) || amount <= 0) return 0;

        ItemStack[] contents = inventory.getContents();
        int toRemove = amount;

        int removed = 0;
        for (int i = 0; i < contents.length; i++) {
            ItemStack currentItem = contents[i];
            if(currentItem == null || currentItem.getType() == Material.AIR) continue;

            boolean success = predicate.test(currentItem);

            if (success) {
                int itemAmount = currentItem.getAmount();

                if (itemAmount <= toRemove) {
                    toRemove -= itemAmount;
                    removed += itemAmount;
                    inventory.setItem(i, null);
                } else {
                    currentItem.setAmount(itemAmount - toRemove);
                    inventory.setItem(i, currentItem);
                    return removed + toRemove;
                }
            }
        }

        return removed;
    }

    /**
     * Удаляет у инвентаря все предметы по фильтру.
     *
     * @param inventory  инвентарь, у которого нужно удалить предметы.
     * @param filter     фильтрация.
     *
     * @return количество удаленных предметов
     */
    public static int removeItem(Inventory inventory, Predicate<ItemStack> filter) {
        if (!Validator.valid(inventory, filter)) return 0;

        ItemStack[] contents = inventory.getContents();

        int removed = 0;
        for (int i = 0; i < contents.length; i++) {
            ItemStack currentItem = contents[i];
            if (currentItem == null || currentItem.getType() == Material.AIR || !filter.test(currentItem)) continue;

            removed += currentItem.getAmount();
            inventory.setItem(i, null);
        }

        return removed;
    }

    /**
     * Возвращает слот по его координатам.
     *
     * @param x горизонтальный сдвиг слота (1-9)
     * @param y вертикальный сдвиг слота (1-6)
     * @return индекс слота, или null, если координаты выходят за пределы допустимого диапазона
     */
    public static @Nullable Integer getSlotByXY(int x, int y) {
        if (x < 1 || x > 9 || y < 1 || y > 6) {
            return null;
        }
        return (y - 1) * 9 + (x - 1);
    }

    /**
     * Возвращает слот по его координатам от полученной строки.
     *
     * @param string строка с координатами
     * @return индекс слота, или null, если координаты выходят за пределы допустимого диапазона или получена некорректная строка
     */
    public static @Nullable Integer getSlotByXY(String string) {
        if(string == null || string.isEmpty()) return null;
        String[] split = string.split(";");
        if(split.length != 2) return null;

        int x, y;
        try {
            x = Integer.parseInt(split[0]);
            y = Integer.parseInt(split[1]);
        } catch (NumberFormatException e) {
            return null;
        }

        return getSlotByXY(x, y);
    }

    /**
     * Возвращает список индексов слотов в диапазоне.
     *
     * @param start начальный индекс слота (включительно)
     * @param end   конечный индекс слота (включительно)
     * @return Список индексов слотов
     */
    public static @NotNull List<Integer> getSlots(int start, int end) {
        return IntStream.rangeClosed(start, end)
                .boxed()
                .toList();
    }

    /**
     * Возвращает список индексов слотов в диапазоне по полученной строке.
     *
     * @param string строка с диапазоном
     * @return Список индексов слотов
     */
    public static @NotNull List<Integer> getSlots(String string) {
        if(string == null || string.isEmpty()) return List.of();
        String[] split = string.split(";");
        if(split.length != 2) return List.of();

        int start, end;
        try {
            start = Integer.parseInt(split[0]);
            end = Integer.parseInt(split[1]);
        } catch (NumberFormatException e) {
            return List.of();
        }

        return getSlots(start, end);
    }

    /**
     * Получает список индексов пустых слотов в инвентаре.
     *
     * @param inventory Инвентарь, для которого нужно получить пустые слоты.
     * @return Список индексов пустых слотов.
     */
    public static @NotNull List<Integer> getEmptySlots(@NotNull Inventory inventory) {
        return IntStream.range(0, inventory.getSize())
                .filter(slot -> inventory.getItem(slot) == null || inventory.getItem(slot).getType() == Material.AIR)
                .boxed()
                .collect(Collectors.toList());
    }

    /**
     * Получает список всех индексов слотов в инвентаре.
     *
     * @param inventory Инвентарь, для которого нужно получить все слоты.
     * @return Список индексов всех слотов.
     */
    public static @NotNull List<Integer> getSlots(@NotNull Inventory inventory) {
        return IntStream.range(0, inventory.getSize())
                .boxed()
                .collect(Collectors.toList());
    }

    /**
     * Получает случайный индекс слота из всех слотов инвентаря.
     *
     * @param inventory Инвентарь, из которого нужно выбрать случайный слот.
     * @return Случайный индекс слота или null, если инвентарь пуст.
     */
    public static @Nullable Integer getRandomSlot(@NotNull Inventory inventory) {
        List<Integer> slots = getSlots(inventory);
        return slots.isEmpty() ? null : slots.get(ThreadLocalRandom.current().nextInt(slots.size()));
    }

    /**
     * Получает случайный индекс пустого слота.
     *
     * @param inventory Инвентарь, из которого нужно выбрать случайный пустой слот.
     * @return Случайный индекс пустого слота или null, если нет пустых слотов.
     */
    public static @Nullable Integer getRandomEmptySlot(@NotNull Inventory inventory) {
        List<Integer> slots = getEmptySlots(inventory);
        return slots.isEmpty() ? null : slots.get(ThreadLocalRandom.current().nextInt(slots.size()));
    }

    /**
     * Создает новый инвентарь с указанным количеством строк и заголовком.
     *
     * @param holder Хранитель инвентаря.
     * @param rows Количество строк в инвентаре (от 1 до 6).
     * @param title Заголовок инвентаря.
     * @return Созданный инвентарь.
     */
    @SuppressWarnings("deprecation")
    public static @NotNull Inventory createInventory(@Nullable InventoryHolder holder, int rows, String title) {
        title = Util.safePAPI(title);
        if (title == null) title = "";

        return Bukkit.createInventory(holder, getSizeByRows(rows), ColorUtil.colorize(title));
    }

    /**
     * Получает размер инвентаря по количеству строк.
     *
     * @param rows Количество строк (от 1 до 6).
     * @return Размер инвентаря (количество слотов).
     */
    public static int getSizeByRows(int rows) {
        rows = Math.max(1, Math.min(rows, 6));
        return rows * 9;
    }

    /**
     * Получает количество строк инвентаря по его размеру.
     *
     * @param size Размер (9, 18, 27, 36, 45, 54).
     * @return Количество строк инвентаря.
     */
    public static int getRowsBySize(int size) {
        int rows = size / 9;
        return Math.max(1, Math.min(rows, 6));
    }
}
