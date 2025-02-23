package pl.bkkuc.cralibs.item;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XPotion;
import com.cryptomorin.xseries.profiles.builder.XSkull;
import com.cryptomorin.xseries.profiles.objects.Profileable;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.bkkuc.cralibs.util.ColorUtil;
import pl.bkkuc.cralibs.util.Util;
import pl.bkkuc.cralibs.util.Validator;

import java.util.*;

@SuppressWarnings({"unchecked", "unused"})
public class ItemBuilder implements Cloneable {

    private Material material;
    private int amount = 1;

    private @Nullable String skullOwner = null;

    private @Nullable String displayName = null;
    private @Nullable List<String> lore = new ArrayList<>();

    private @Nullable Integer customModelData = null;

    private Set<ItemFlag> flags = new HashSet<>();
    private Map<Enchantment, Integer> enchantments = new HashMap<>();
    private List<PotionEffect> potionEffects = new ArrayList<>();

    private @Nullable Color color = null;

    /**
     * Конструктор для создания ItemBuilder с материалом.
     *
     * @param material материал предмета
     */
    public ItemBuilder(@NotNull Material material) {
        this.material = material;
        Validator.notNullOrAir(material);
    }

    /**
     * Конструктор для создания ItemBuilder с материалом и количеством.
     *
     * @param material материал предмета
     * @param amount количество предметов
     */
    public ItemBuilder(@NotNull Material material, int amount) {
        this.material = material;
        Validator.notNullOrAir(material);
        this.amount = amount;
    }

    public ItemBuilder(@NotNull String string) {
        try {
            this.material = Material.valueOf(string.toUpperCase());
        } catch (IllegalArgumentException e) {
            final String[] split = string.split("#");
            if (split.length == 2) {
                this.material = Material.matchMaterial(split[0]);
                this.customModelData = Integer.parseInt(split[1]);
            }

            if(string.startsWith("head-") || string.startsWith("head:")) {
                try {
                    material = Material.PLAYER_HEAD;
                } catch (IllegalArgumentException exception) {
                    material = Material.LEGACY_SKULL;
                }
                try {
                    this.skullOwner = string.split(":")[1];
                } catch (Exception exception) {
                    this.skullOwner = string.split("-")[1];
                }
            }
        }
    }

    public ItemBuilder material(@NotNull String material) {
        try {
            try {
                final Material matchedMaterial = Material.matchMaterial(material);
                if(matchedMaterial != null) {
                    this.material = matchedMaterial;
                }
            } catch (IllegalArgumentException e) {
                final String[] split = material.split("#");
                if (split.length == 2) {
                    final Material matchedMaterial = Material.matchMaterial(split[0]);
                    if(matchedMaterial != null) {
                        this.material = matchedMaterial;
                    }
                    this.customModelData = Integer.parseInt(split[1]);
                }
            }
        } catch (Exception ignored) {}
        return this;
    }

    public ItemBuilder material(@NotNull Material material) {
        this.material = material;
        return this;
    }

    public Material material() {
        return this.material;
    }

    /**
     * Устанавливает CustomModelData
     *
     * @param customModelData новый CustomModelData
     * @return обновленный ItemBuilder
     */
    public ItemBuilder customModelData(@Nullable Integer customModelData) {
        this.customModelData = customModelData;
        return this;
    }

    public Integer customModelData() {
        return this.customModelData;
    }

    /**
     * Устанавливает количество предметов в стеке.
     *
     * @param amount количество предметов
     * @return обновленный ItemBuilder
     */
    public ItemBuilder amount(int amount) {
        if (amount <= 0) amount = 1;
        if (amount >= material.getMaxStackSize()) amount = material.getMaxStackSize();
        this.amount = amount;
        return this;
    }

    public int amount() {
        return this.amount;
    }

    /**
     * Устанавливает отображаемое имя предмета.
     *
     * @param displayName отображаемое имя
     * @return обновленный ItemBuilder
     */
    public ItemBuilder displayName(@Nullable String displayName) {
        this.displayName = displayName;
        return this;
    }

    public @Nullable String displayName() {
        return this.displayName;
    }

    /**
     * Устанавливает описание предмета.
     *
     * @param lore описание (список строк)
     * @return обновленный ItemBuilder
     */
    public ItemBuilder lore(@Nullable List<String> lore) {
        this.lore = lore != null ? new ArrayList<>(lore) : null;
        return this;
    }

    public @Nullable List<String> lore() {
        return lore;
    }

    /**
     * Добавляет строку к описанию предмета.
     *
     * @param lore строка описания
     * @return обновленный ItemBuilder
     */
    public ItemBuilder addLore(@Nullable String lore) {
        if (this.lore == null) this.lore = new ArrayList<>();
        this.lore.add(lore);
        return this;
    }

    public ItemBuilder flags(@Nullable Collection<ItemFlag> flags) {
        this.flags = flags != null ? new HashSet<>(flags) : null;
        return this;
    }

    public Set<ItemFlag> flags() {
        return flags;
    }

    /**
     * Добавляет флаг предмета.
     *
     * @param flags флаги предмета
     * @return обновленный ItemBuilder
     */
    public ItemBuilder addFlag(@NotNull ItemFlag... flags) {
        if (this.flags == null) this.flags = new HashSet<>();
        Collections.addAll(this.flags, flags);
        return this;
    }


    /**
     * Добавляет зачарование предмета.
     *
     * @param enchantment зачарование
     * @param level уровень зачарования
     * @return обновленный ItemBuilder
     */
    public ItemBuilder addEnchantment(@NotNull Enchantment enchantment, int level) {
        if (enchantments == null) enchantments = new HashMap<>();
        enchantments.put(enchantment, level);
        return this;
    }

    public ItemBuilder enchantments(@Nullable Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments != null ? new HashMap<>(enchantments) : null;
        return this;
    }

    public @NotNull Map<Enchantment, Integer> enchantments() {
        return enchantments;
    }

    /**
     * Устанавливает владельца головы (если материал головы).
     *
     * @param skullOwner владелец головы
     * @return обновленный ItemBuilder
     */
    public ItemBuilder skullOwner(@Nullable String skullOwner) {
        this.skullOwner = skullOwner;
        return this;
    }

    public @Nullable String skullOwner() {
        return this.skullOwner;
    }

    /**
     * Добавляет эффект зелья к предмету (если материал зелье).
     *
     * @param effect эффект зелья
     * @return обновленный ItemBuilder
     */
    public ItemBuilder addPotionEffect(@NotNull PotionEffect effect) {
        if(potionEffects == null) potionEffects = new ArrayList<>();
        potionEffects.removeIf(potionEffect -> potionEffect.getType() == effect.getType());
        potionEffects.add(effect);
        return this;
    }

    public ItemBuilder potionEffects(@Nullable List<PotionEffect> potionEffects) {
        this.potionEffects = potionEffects != null ? new ArrayList<>(potionEffects) : null;
        return this;
    }

    public List<PotionEffect> potionEffects() {
        return potionEffects;
    }

    /**
     * Устанавливает цвет предмета (если материал может быть окрашен).
     *
     * @param color цвет
     * @return обновленный ItemBuilder
     */
    public ItemBuilder color(@Nullable Color color) {
        this.color = color;
        return this;
    }

    public @Nullable Color color() {
        return this.color;
    }

    /**
     * Создает предмет с указанными свойствами.
     *
     * @return созданный предмет
     */
    public @NotNull ItemStack build() {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();

        if (displayName != null) {
            displayName = ColorUtil.colorize(displayName);
            meta.setDisplayName(Util.hasPlaceholderAPI() ? PlaceholderAPI.setPlaceholders(null, displayName) : displayName);
        }

        if (lore != null && !lore.isEmpty()) {
            meta.setLore(lore.stream().map(str -> {
                str = (Util.hasPlaceholderAPI()) ? PlaceholderAPI.setPlaceholders(null, str) : str;
                str = ColorUtil.colorize(str);
                return str;
            }).toList());
        }

        if(customModelData != null) meta.setCustomModelData(customModelData);

        if (!enchantments.isEmpty()) {
            enchantments.forEach((enchantment, level) -> meta.addEnchant(enchantment, level, true));
        }

        if (!flags.isEmpty()) {
            flags.forEach(meta::addItemFlags);
        }

        item.setItemMeta(meta);

        if (meta instanceof LeatherArmorMeta armorMeta) {
            if (this.color != null) armorMeta.setColor(color);
            item.setItemMeta(armorMeta);
        }

        if (meta instanceof PotionMeta pm) {
            if (color != null) pm.setColor(color);
            if (!potionEffects.isEmpty()) {
                potionEffects.forEach(potionEffect -> pm.addCustomEffect(potionEffect, true));
            }
            item.setItemMeta(pm);
        }

        if (material == Material.PLAYER_HEAD && skullOwner != null) {
            item = XSkull.of(item).profile(Profileable.detect(skullOwner)).apply();
        }

        return item;
    }

    /**
     * Клонирует текущий объект ItemBuilder.
     *
     * @return клон объекта ItemBuilder
     */
    @Override
    public ItemBuilder clone() {
        try {
            return (ItemBuilder) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**
     * Сериализует объект ItemBuilder в Map<String, Object>.
     * <p>
     * Пример, как будет выглядеть сериализованный объект в конфигурационном файле (YAML):
     * <p>
     * ```yaml
     * material: DIAMOND_SWORD  # Название материала, например, DIAMOND_SWORD
     * amount: 1                # Количество предметов в стеке
     * skullOwner: null         # Владелец головы, если применимо, например, "Steve" или null
     * displayName: '§6Epic Sword'  # Отображаемое имя предмета с цветом (например, §6 для золотого цвета)
     * lore:                    # Описание предмета в виде списка строк
     *   - 'Уникальный меч'
     * customModelData: null    # Значение CustomModelData, если установлено (например, 123 или null)
     * flags:                   # Флаги предмета, которые будут скрыты (например, HIDE_ENCHANTS)
     *   - HIDE_ENCHANTS
     * enchantments:            # Зачарования предмета в формате "название: уровень"
     *   DAMAGE_ALL: 5
     * potionEffects:           # Эффекты зелья, если применимо
     *   - type: SPEED
     *     duration: 600
     *     amplifier: 1
     * color: null              # Цвет предмета в формате RGB, если применимо (например, 16711680 для красного или null)
     * ```
     *
     * @param itemBuilder объект ItemBuilder для сериализации
     * @return карта с сериализованными данными
     */
    public static Map<String, Object> serialize(@NotNull ItemBuilder itemBuilder) {
        Map<String, Object> serialized = new HashMap<>();
        serialized.put("material", itemBuilder.material.name());
        serialized.put("amount", itemBuilder.amount);
        serialized.put("skullOwner", itemBuilder.skullOwner);
        serialized.put("displayName", itemBuilder.displayName);
        serialized.put("lore", itemBuilder.lore != null ? itemBuilder.lore : new ArrayList<>());
        serialized.put("customModelData", itemBuilder.customModelData);
        serialized.put("flags", itemBuilder.flags.stream().map(ItemFlag::name).toList());

        List<Map<String, Object>> enchantments = new ArrayList<>();

        itemBuilder.enchantments.forEach((enchantment, level) -> {
            enchantments.add(Map.of("enchantment", enchantment.getName(), "level", level));
        });

        List<Map<String, Object>> potionEffects = itemBuilder.potionEffects.stream()
                .map(PotionEffect::serialize)
                .toList();
        serialized.put("potionEffects", potionEffects);

        serialized.put("color", itemBuilder.color != null ? itemBuilder.color.asRGB() : null);
        return serialized;
    }

    /**
     * Сериализует объект ItemBuilder в ConfigurationSection.
     *
     * @param itemBuilder объект ItemBuilder для сериализации
     * @param section ConfigurationSection для сохранения данных
     */
    public static void serialize(@NotNull ItemBuilder itemBuilder, @NotNull ConfigurationSection section) {
        section.set("material", itemBuilder.material.name());
        section.set("amount", itemBuilder.amount);
        section.set("skullOwner", itemBuilder.skullOwner);
        section.set("name", itemBuilder.displayName);
        section.set("lore", itemBuilder.lore != null ? itemBuilder.lore : new ArrayList<>());
        section.set("customModelData", itemBuilder.customModelData);
        section.set("flags", itemBuilder.flags.stream().map(ItemFlag::name).toList());

        Map<String, Integer> enchantments = new HashMap<>();
        itemBuilder.enchantments.forEach((enchantment, level) -> enchantments.put(enchantment.getName(), level));
        section.set("enchantments", enchantments);

        List<Map<String, Object>> potionEffects = itemBuilder.potionEffects.stream()
                .map(PotionEffect::serialize)
                .toList();
        section.set("potionEffects", potionEffects);

        section.set("color", itemBuilder.color != null ? itemBuilder.color.asRGB() : null);
    }

    /**
     * Десериализует карту данных в объект ItemBuilder.
     *
     * @param data карта данных
     * @return десериализованный объект ItemBuilder
     */
    public static @Nullable ItemBuilder deserialize(@NotNull Map<String, Object> data) {

        ItemBuilder builder;
        try {
            builder = new ItemBuilder(String.valueOf(data.get("material")));
        } catch (final Exception e) {
            return null;
        }

        int amount = (int) data.getOrDefault("amount", 1);
        builder.amount(amount);

        if(builder.skullOwner == null) {
            builder.skullOwner = (String) data.getOrDefault("skullOwner", data.getOrDefault("skull-owner", data.get("head")));
        }
        builder.displayName = (String) data.getOrDefault("displayName", data.getOrDefault("name", data.get("display-name")));
        builder.lore = (List<String>) data.get("lore");

        if(builder.customModelData == null) {
            builder.customModelData = (Integer) data.getOrDefault("customModelData", data.getOrDefault("custom-model-data", data.get("model-data")));
        }

        List<String> flagNames = (List<String>) data.get("flags");
        if (flagNames != null) {
            for (String flagName : flagNames) {
                builder.flags.add(ItemFlag.valueOf(flagName));
            }
        }

        List<Map<String, Object>> enchantmentsData = (List<Map<String, Object>>) data.getOrDefault("enchantments", data.get("enchants"));
        if(enchantmentsData != null) {
            for (Map<String, Object> enchantmentData : enchantmentsData) {
                String enchantmentName = (String) enchantmentData.getOrDefault("enchantment", enchantmentData.getOrDefault("enchant", enchantmentData.get("name")));
                if(enchantmentName == null) continue;
                enchantmentName = enchantmentName.toUpperCase();

                XEnchantment.matchXEnchantment(enchantmentName).ifPresent(xEnchantment -> {
                    builder.enchantments.put(xEnchantment.getEnchant(), (int) enchantmentData.getOrDefault("level", enchantmentData.getOrDefault("lvl", 1)));
                });
            }
        }

        List<Map<String, Object>> potionEffectsData = (List<Map<String, Object>>) data.getOrDefault("potionEffects", data.getOrDefault("potion-effects", data.get("effects")));
        if (potionEffectsData != null) {
            for (Map<String, Object> effectData : potionEffectsData) {
                PotionEffect potionEffect = Util.fromMap(effectData);
                if(potionEffect == null) continue;
                builder.potionEffects.add(Util.fromMap(effectData));
            }
        }

        builder.color = ColorUtil.parseColor(data.get("color"));

        return builder;
    }

    /**
     * Десериализует ConfigurationSection в объект ItemBuilder.
     *
     * @param section ConfigurationSection с данными
     * @return десериализованный объект ItemBuilder
     */
    public static @Nullable ItemBuilder deserialize(@NotNull ConfigurationSection section) {
        ItemBuilder itemBuilder;
        try {
            itemBuilder = new ItemBuilder(section.getString("material", "STONE"));
        } catch (IllegalArgumentException e){
            Bukkit.getLogger().warning("[CraLibs] Section " + section.getName() + " material is not found.");
            return null;
        }

        int amount = section.getInt("amount", 1);
        itemBuilder.amount(amount);

        if(itemBuilder.skullOwner == null) {
            itemBuilder.skullOwner = section.getString("skullOwner", section.getString("head", section.getString("skull-owner", section.getString("skull"))));
        }
        itemBuilder.displayName = section.getString("displayName", section.getString("name", section.getString("display-name", section.getString("title", section.getString("displayname")))));
        itemBuilder.lore = section.getStringList("lore");

        if(itemBuilder.customModelData == null) {
            itemBuilder.customModelData = section.contains("customModelData") || section.contains("custom-model-data") || section.contains("custommodeldata") || section.contains("model-data") ? section.getInt("customModelData", section.getInt("custom-model-data", section.getInt("custommodeldata", section.getInt("model-data")))) : null;
        }

        List<String> flagNames = section.getStringList("flags");
        for (String flagName : flagNames) {
            itemBuilder.flags.add(ItemFlag.valueOf(flagName));
        }

        ConfigurationSection enchantmentsSection = section.getConfigurationSection("enchantments");
        if(enchantmentsSection != null) {
            for(final String enchantmentName: enchantmentsSection.getKeys(false)) {
                XEnchantment.matchXEnchantment(enchantmentName.toUpperCase()).ifPresent(xEnchantment -> itemBuilder.enchantments.put(xEnchantment.getEnchant(), enchantmentsSection.getInt(enchantmentName, 1)));
            }
        }

        List<String> potionEffects = section.getStringList("potionEffects");
        if(potionEffects.isEmpty()) potionEffects = section.getStringList("effects");
        if(potionEffects.isEmpty()) potionEffects = section.getStringList("potion-effects");

        for(final String potionEffectParam: potionEffects) {
            final String[] split = potionEffectParam.split(";");
            if(split.length != 3) continue;

            int duration = Integer.parseInt(split[1]);
            int amplifier = Integer.parseInt(split[2]);

            XPotion.matchXPotion(split[0].toUpperCase()).ifPresent(xPotion -> itemBuilder.potionEffects.add(new PotionEffect(xPotion.getPotionEffectType(), duration * 20, amplifier)));
        }

        Color color = ColorUtil.parseColor(section.get("color"));
        if(color != null) {
            itemBuilder.color = color;
        }

        return itemBuilder;
    }

    public static @NotNull ItemBuilder of(@NotNull ItemStack item) {
        return new ItemBuilder
                (item.getType(), item.getAmount())
                .customModelData(item.getItemMeta().getCustomModelData())
                .flags(item.getItemMeta().getItemFlags())
                .enchantments(item.getEnchantments())
                .skullOwner(XSkull.of(item).getProfileValue())
                .displayName(item.getItemMeta().getDisplayName())
                .lore(item.getItemMeta().getLore())
                .color(item.getItemMeta() instanceof final LeatherArmorMeta leatherArmorMeta ? leatherArmorMeta.getColor() : item.getItemMeta() instanceof final PotionMeta potionMeta ? potionMeta.getColor() : null)
                .potionEffects(item.getItemMeta() instanceof final PotionMeta potionMeta ? potionMeta.getCustomEffects() : new ArrayList<>())
                ;
    }
}
