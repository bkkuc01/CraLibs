package pl.bkkuc.cralibs.database;

/**
 * Перечисление DataBaseType для указания типа базы данных.
 */
public enum DataBaseType {
    MYSQL("MySQL"),
    SQLITE("SqLite"),
    MARIADB("MariaBD"),
    POSTGRESQL("Postgresql");

    private final String name;

    /**
     * Конструктор для инициализации типа базы данных.
     *
     * @param name имя типа базы данных
     */
    DataBaseType(String name) {
        this.name = name;
    }

    /**
     * Возвращает имя типа базы данных.
     *
     * @return имя типа базы данных
     */
    public String getName() {
        return name;
    }
}
