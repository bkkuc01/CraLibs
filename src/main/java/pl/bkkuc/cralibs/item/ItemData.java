package pl.bkkuc.cralibs.item;

import com.jeff_media.morepersistentdatatypes.DataType;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.bkkuc.cralibs.util.Validator;

/**
 * Класс ItemData предоставляет методы для работы с метаданными {@link ItemStack}.
 * Пример использования данного класса:
 *
 * <pre>{@code
 *
 *  ItemData data = new ItemData(item);
 *  if(data.hasTag("example_tag")) {
 *      Bukkit.getLogger().warn("has tag!");
 *  }
 * }</pre>
 *
 * <span style="color:red">Данный класс предназначен для версии Minecraft Java Edition начиная с 1.14 и выше!</span><br>
 */
@SuppressWarnings("unused")
public class ItemData {

    private final ItemStack item;
    private final ItemMeta meta;
    private final PersistentDataContainer container;

    /**
     * Конструктор, который инициализирует {@link ItemData} из {@link ItemStack}.
     *
     * @param item {@link ItemStack} для инициализации.
     */
    public ItemData(@NotNull ItemStack item) {
        Validator.notNullOrAir(item.getType());
        this.item = item;
        if(item.getItemMeta() != null) {
            meta = item.getItemMeta();
        }
        else {
            meta = Bukkit.getItemFactory().getItemMeta(item.getType());
            item.setItemMeta(meta);
        }
        this.container = meta.getPersistentDataContainer();
    }

    /**
     * Проверяет наличие тега указанного типа.
     *
     * @param tag Название тега.
     * @param type Тип данных тега.
     * @param <T> Исходный тип данных.
     * @param <Z> Тип данных контейнера.
     * @return {@code true}, если тег существует и соответствует указанному типу, иначе {@code false}.
     */
    public <T, Z> boolean hasTag(@NotNull String tag, @NotNull PersistentDataType<T, Z> type) {
        if (tag.isEmpty()) return false;
        try {
            NamespacedKey key = key(tag);
            return container.has(key, type);
        } catch (NullPointerException ignored) {
            return false;
        }
    }

    public @Nullable String getString(@NotNull String tag) {
        return get(tag, PersistentDataType.STRING);
    }

    public @Nullable Integer getInt(@NotNull String tag) {
        return get(tag, PersistentDataType.INTEGER);
    }

    public @Nullable Integer getInteger(@NotNull String tag) {
        return getInt(tag);
    }

    public @Nullable Boolean getBoolean(@NotNull String tag) {
        return get(tag, DataType.BOOLEAN);
    }

    public @Nullable Double getDouble(@NotNull String tag) {
        return get(tag, PersistentDataType.DOUBLE);
    }

    public @Nullable Float getFloat(@NotNull String tag) {
        return get(tag, PersistentDataType.FLOAT);
    }

    public @Nullable Long getLong(@NotNull String tag) {
        return get(tag, PersistentDataType.LONG);
    }

    public @Nullable Byte getByte(@NotNull String tag) {
        return get(tag, PersistentDataType.BYTE);
    }

    public @Nullable Short getShort(@NotNull String tag) {
        return get(tag, PersistentDataType.SHORT);
    }

    public @Nullable ItemStack getItemStack(@NotNull String tag) {
        return get(tag, DataType.ITEM_STACK);
    }

    public @Nullable ItemStack[] getItemStacks(@NotNull String tag) {
        return get(tag, DataType.ITEM_STACK_ARRAY);
    }

    private @Nullable <T, Z> T get(@NotNull String tag, @NotNull PersistentDataType<Z, T> type) {
        if (tag.isEmpty() || container == null || !hasTag(tag, type)) return null;
        return container.get(key(tag), type);
    }

    public void setString(@NotNull String tag, @NotNull String value) {
        if (tag.isEmpty() || container == null) return;
        container.set(key(tag), PersistentDataType.STRING, value);
        item.setItemMeta(meta);
    }

    public void setInteger(@NotNull String tag, int value) {
        if (tag.isEmpty() || container == null) return;
        container.set(key(tag), PersistentDataType.INTEGER, value);
        item.setItemMeta(meta);
    }

    public void setInt(@NotNull String tag, int value) {
        setInteger(tag, value);
    }

    public void setBoolean(@NotNull String tag, boolean value) {
        if (tag.isEmpty() || container == null) return;
        container.set(key(tag), DataType.BOOLEAN, value);
        item.setItemMeta(meta);
    }

    public void setDouble(@NotNull String tag, double value) {
        if (tag.isEmpty() || container == null) return;
        container.set(key(tag), PersistentDataType.DOUBLE, value);
        item.setItemMeta(meta);
    }

    public void setFloat(@NotNull String tag, float value) {
        if (tag.isEmpty() || container == null) return;
        container.set(key(tag), PersistentDataType.FLOAT, value);
        item.setItemMeta(meta);
    }

    public void setLong(@NotNull String tag, long value) {
        if (tag.isEmpty() || container == null) return;
        container.set(key(tag), PersistentDataType.LONG, value);
        item.setItemMeta(meta);
    }

    public void setByte(@NotNull String tag, byte value) {
        if (tag.isEmpty() || container == null) return;
        container.set(key(tag), PersistentDataType.BYTE, value);
        item.setItemMeta(meta);
    }

    public void setShort(@NotNull String tag, short value) {
        if (tag.isEmpty() || container == null) return;
        container.set(key(tag), PersistentDataType.SHORT, value);
        item.setItemMeta(meta);
    }

    public void setItemStack(@NotNull String tag, @NotNull ItemStack itemStack) {
        if (tag.isEmpty() || container == null) return;
        container.set(key(tag), DataType.ITEM_STACK, itemStack);
        item.setItemMeta(meta);
    }

    public void setItemStacks(@NotNull String tag, @NotNull ItemStack[] itemStacks) {
        if (tag.isEmpty() || container == null) return;
        container.set(key(tag), DataType.ITEM_STACK_ARRAY, itemStacks);
        item.setItemMeta(meta);
    }

    public <T,Z> void set(@NotNull String tag, PersistentDataType<T, Z> type, Z value) {
        if(tag.isEmpty() || container == null || value == null) return;
        container.set(key(tag), type, value);
        item.setItemMeta(meta);
    }

    public void remove(@NotNull String tag){
        if (tag.isEmpty() || container == null) return;
        container.remove(key(tag));
        item.setItemMeta(meta);
    }

    private NamespacedKey key(@NotNull String tag) {
        return NamespacedKey.fromString(tag);
    }

    public @NotNull ItemStack getItem() {
        return item;
    }
}
