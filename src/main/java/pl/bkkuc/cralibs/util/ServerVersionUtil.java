package pl.bkkuc.cralibs.util;

import org.bukkit.Bukkit;

/**
 * Утилитный класс для работы с версией сервера Minecraft.
 * Предоставляет метод для получения версии сервера в виде перечисления.
 */
public final class ServerVersionUtil {

    /**
     * Перечисление, представляющее версии сервера Minecraft.
     * Содержит версии от v1_8 до v1_21 и значение UNKNOWN для нераспознанных версий.
     */
    public enum ServerVersion {
        /** Версия сервера Minecraft 1.8 */
        v1_8,
        /** Версия сервера Minecraft 1.9 */
        v1_9,
        /** Версия сервера Minecraft 1.10 */
        v1_10,
        /** Версия сервера Minecraft 1.11 */
        v1_11,
        /** Версия сервера Minecraft 1.12 */
        v1_12,
        /** Версия сервера Minecraft 1.13 */
        v1_13,
        /** Версия сервера Minecraft 1.14 */
        v1_14,
        /** Версия сервера Minecraft 1.15 */
        v1_15,
        /** Версия сервера Minecraft 1.16 */
        v1_16,
        /** Версия сервера Minecraft 1.17 */
        v1_17,
        /** Версия сервера Minecraft 1.18 */
        v1_18,
        /** Версия сервера Minecraft 1.19 */
        v1_19,
        /** Версия сервера Minecraft 1.20 */
        v1_20,
        /** Версия сервера Minecraft 1.21 */
        v1_21,
        /** Неизвестная версия сервера */
        UNKNOWN;

        /**
         * Определяет версию сервера Minecraft, на котором работает данный плагин.
         * Извлекает версию из имени пакета сервера и преобразует её в соответствующее перечисление.
         * Если версия не может быть распознана, возвращает {@link ServerVersion#UNKNOWN}.
         *
         * @return {@link ServerVersion} версия сервера Minecraft
         */
        public static ServerVersion getVersion() {
            String version = Bukkit.getServer().getClass().getPackage().getName();
            String versionPattern = "org.bukkit.craftbukkit.(v[1-9]_[0-9])";

            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(versionPattern);
            java.util.regex.Matcher matcher = pattern.matcher(version);

            if (matcher.find()) {
                String versionStr = matcher.group(1);
                try {
                    return ServerVersion.valueOf(versionStr);
                } catch (IllegalArgumentException e) {
                    return UNKNOWN;
                }
            }
            return UNKNOWN;
        }
    }
}
