package pl.bkkuc.cralibs.database;

import org.jetbrains.annotations.NotNull;

/**
 * Интерфейс Connectable для предоставления информации о подключении к базе данных.
 */
public interface Connectable {

    /**
     * Возвращает хост базы данных.
     *
     * @return хост базы данных
     */
    @NotNull String getHost();

    /**
     * Возвращает порт базы данных.
     *
     * @return порт базы данных
     */
    @NotNull String getPort();

    /**
     * Возвращает имя пользователя для подключения к базе данных.
     *
     * @return имя пользователя
     */
    @NotNull String getUserName();

    /**
     * Возвращает пароль для подключения к базе данных.
     *
     * @return пароль
     */
    @NotNull String getPassword();
}