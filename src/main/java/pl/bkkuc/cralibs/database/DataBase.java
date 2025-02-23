package pl.bkkuc.cralibs.database;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Интерфейс DataBase для работы с базами данных.
 */
public interface DataBase {

    /**
     * Получает соединение с базой данных.
     *
     * @return соединение с базой данных
     * @throws SQLException если не удалось установить соединение
     */
    Connection getConnection() throws SQLException;

    /**
     * Инициализирует базу данных с использованием предоставленных SQL-запросов.
     *
     * @param queries SQL-запросы для инициализации
     * @throws SQLException если произошла ошибка при выполнении запросов
     */
    void initDataBase(@NotNull String... queries) throws SQLException;

    /**
     * Выполняет синхронный SQL-запрос для обновления данных в базе.
     * Используется для операций, которые изменяют данные, такие как INSERT, UPDATE или DELETE.
     *
     * @param query  SQL-запрос для выполнения
     * @param params параметры, которые будут переданы в подготовленный запрос
     * @throws SQLException если запрос не удался
     */
    default void executeUpdate(@NotNull String query, Object... params) throws SQLException {
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            statement.executeUpdate();
        }
    }

    /**
     * Выполняет синхронный SQL-запрос для получения данных из базы.
     * Результат передается в обработчик, который вызывается после завершения запроса.
     *
     * @param query         SQL-запрос для выполнения
     * @param resultHandler обработчик результата (ResultSet)
     * @param params        параметры, которые будут переданы в подготовленный запрос
     * @throws SQLException если запрос не удался
     */
    default void executeQuery(@NotNull String query, @NotNull Consumer<ResultSet> resultHandler, Object... params) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                resultHandler.accept(resultSet);
            }
        }
    }

    /**
     * Выполняет асинхронный SQL-запрос для обновления данных в базе.
     * Используется для операций, которые изменяют данные, такие как INSERT, UPDATE или DELETE.
     * Метод выполняется в отдельном потоке, чтобы избежать блокировки основного потока приложения.
     *
     * @param query  SQL-запрос для выполнения
     * @param params параметры, которые будут переданы в подготовленный запрос
     */
    default void executeAsyncUpdate(@NotNull String query, Object... params) {
        CompletableFuture.runAsync(() -> {
            try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i]);
                }
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Выполняет асинхронный SQL-запрос для получения данных из базы.
     * Результат передается в обработчик, который вызывается после завершения запроса.
     *
     * @param query     SQL-запрос для выполнения
     * @param resultHandler обработчик результата (ResultSet)
     * @param params    параметры, которые будут переданы в подготовленный запрос
     */
    default void executeAsyncQuery(@NotNull String query, @NotNull Consumer<ResultSet> resultHandler, Object... params) {
        CompletableFuture.runAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i]);
                }

                try (ResultSet resultSet = statement.executeQuery()) {
                    resultHandler.accept(resultSet);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Закрывает соединение с базой данных.
     */
    void close();

    /**
     * Возвращает тип базы данных.
     *
     * @return тип базы данных
     */
    @NotNull
    DataBaseType getDataBaseType();

    /**
     * Возвращает имя базы данных.
     *
     * @return имя базы данных или null, если имя не установлено
     */
    @Nullable
    String getDataBaseName();
}
