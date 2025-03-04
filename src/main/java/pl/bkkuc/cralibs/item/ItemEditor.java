package pl.bkkuc.cralibs.item;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Color;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.bkkuc.cralibs.util.ColorUtil;
import pl.bkkuc.cralibs.util.Util;
import pl.bkkuc.cralibs.util.Validator;

import java.util.ArrayList;
import java.util.List;

public class ItemEditor {

    private final ItemStack item;
    private final ItemMeta meta;

    public ItemEditor(@NotNull ItemStack item) {
        this.item = item;
        Validator.notNullOrAir(item.getType());
        meta = item.getItemMeta();
        Validator.notNull(meta, "Meta");
    }

    /**
     * Устанавливает CustomModelData.
     *
     * @param customModelData новый модель дата.
     */
    public ItemEditor setCustomModelData(@Nullable Integer customModelData) {
        meta.setCustomModelData(customModelData);
        return this;
    }

    /**
     * Устанавливает описание предмета.
     *
     * @param lore описание (список строк)
     */
    public ItemEditor setLore(@NotNull List<String> lore) {
        lore = lore.stream().map(str -> {
                str = Util.safePAPI(str);
                return ColorUtil.colorize(str);
        }).toList();
        meta.setLore(lore);
        return this;
    }

    /**
     * Добавляет строку к описанию предмета.
     *
     * @param lore строка описания
     */
    public ItemEditor addLore(@NotNull String lore) {
        List<String> loreList = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
        lore = Util.safePAPI(ColorUtil.colorize(lore));
        loreList.add(lore);
        meta.setLore(loreList);
        return this;
    }

    /**
     * Удаляет строку из описания предмета.
     *
     * @param lore строка описания
     */
    public ItemEditor removeLore(@NotNull String lore) {
        List<String> loreList = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
        loreList.remove(lore);
        meta.setLore(loreList);
        return this;
    }

    /**
     * Добавляет флаг предмета.
     *
     * @param flag флаг предмета
     */
    public ItemEditor addFlag(@NotNull ItemFlag flag) {
        meta.addItemFlags(flag);
        return this;
    }

    /**
     * Удаляет флаг предмета.
     *
     * @param flag флаг предмета
     */
    public ItemEditor removeFlag(@NotNull ItemFlag flag) {
        meta.removeItemFlags(flag);
        return this;
    }

    /**
     * Добавляет зачарование предмета.
     *
     * @param enchantment зачарование
     * @param level уровень зачарования
     */
    public ItemEditor addEnchantment(@NotNull Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
        return this;
    }

    /**
     * Удаляет зачарование предмета.
     *
     * @param enchantment зачарование
     */
    public ItemEditor removeEnchantment(@NotNull Enchantment enchantment) {
        meta.removeEnchant(enchantment);
        return this;
    }

    /**
     * Добавляет эффект зелья к предмету (если материал зелье).
     *
     * @param effect эффект зелья
     */
    public ItemEditor addPotionEffect(@NotNull PotionEffect effect) {
        if (meta instanceof PotionMeta potionMeta) {
            potionMeta.addCustomEffect(effect, true);
        }
        return this;
    }

    /**
     * Удаляет эффект зелья с предмета (если материал зелье).
     *
     * @param effectType тип эффекта
     */
    public ItemEditor removePotionEffect(@NotNull PotionEffectType effectType) {
        if (meta instanceof PotionMeta potionMeta) {
            potionMeta.removeCustomEffect(effectType);
        }
        return this;
    }

    /**
     * Устанавливает цвет предмета (если материал может быть окрашен).
     *
     * @param color цвет
     */
    public ItemEditor setColor(@NotNull Color color) {
        if (meta instanceof LeatherArmorMeta armorMeta) {
            armorMeta.setColor(color);
        } else if (meta instanceof PotionMeta potionMeta) {
            potionMeta.setColor(color);
        }
        return this;
    }

    /**
     * Устанавливает отображаемое имя предмета.
     *
     * @param displayName отображаемое имя
     */
    public ItemEditor setDisplayName(@NotNull String displayName) {
        displayName = Util.hasPlaceholderAPI() ? PlaceholderAPI.setPlaceholders(null, displayName) : displayName;
        meta.setDisplayName(ColorUtil.colorize(displayName));
        return this;
    }

    public @NotNull String getDisplayName() {
        return meta.getDisplayName();
    }

    public @Nullable List<String> getLore() {
        return meta.getLore();
    }

    public @Nullable Integer getCustomModelData() {
        return meta.hasCustomModelData() ? meta.getCustomModelData() : null;
    }

    /**
     * Применяет изменения метаданных к предмету.
     */
    public void apply() {
        item.setItemMeta(meta);
    }
}
