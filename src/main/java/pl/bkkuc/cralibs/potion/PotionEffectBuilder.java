package pl.bkkuc.cralibs.potion;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public final class PotionEffectBuilder {

    private final List<@NotNull PotionEffect> effects = new ArrayList<>();

    /**
     * Добавить эффект в список.
     *
     * @param effectType        тип эффекта
     * @param durationInSeconds длительность в секундах
     * @param amplifier         сила
     * @param particles         показывать частицы
     */
    public @NotNull PotionEffectBuilder addPotionEffect(@NotNull PotionEffectType effectType, int durationInSeconds, int amplifier, boolean particles) {
        final PotionEffect effect = new PotionEffect(effectType, durationInSeconds * 20, amplifier, false, particles);
        effects.add(effect);
        return this;
    }

    public void apply(@NotNull LivingEntity livingEntity) {
        livingEntity.addPotionEffects(effects);
    }

    /**
     * Возвращает список эффектов, удовлетворяющих предикату
     *
     * @param predicate предикат
     * @return список
     */
    public @NotNull List<@NotNull PotionEffect> getEffects(@NotNull Predicate<@NotNull PotionEffect> predicate) {
        return effects
                .stream()
                .filter(predicate)
                .toList();
    }

    public @NotNull List<@NotNull Map<@NotNull String, @NotNull Object>> serialize() {
        final List<@NotNull Map<@NotNull String, @NotNull Object>> list = new ArrayList<>();

        for (PotionEffect effect : effects) {
            final Map<@NotNull String, @NotNull Object> map = new HashMap<>(effect.serialize());
            list.add(map);
        }

        return list;
    }

    public static @NotNull PotionEffectBuilder fromMap(@NotNull List<@NotNull Map<@NotNull String, @NotNull Object>> list) {
        final PotionEffectBuilder builder = new PotionEffectBuilder();

        for (final Map<@NotNull String, @NotNull Object> map : list) {
            PotionEffect potionEffect;
            try {
                potionEffect = new PotionEffect(map);
            } catch (Exception e) {
                continue;
            }

            builder.getEffects().add(potionEffect);
        }

        return builder;
    }

    public List<@NotNull PotionEffect> getEffects() {
        return effects;
    }
}
