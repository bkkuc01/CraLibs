package pl.bkkuc.cralibs.database.databases;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.bkkuc.cralibs.database.DataBase;
import pl.bkkuc.cralibs.database.DataBaseType;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SqLite implements DataBase {

    private final HikariDataSource dataSource;
    private final String dataBaseName;

    public SqLite(JavaPlugin javaPlugin, String dataBaseName){
        if(dataBaseName == null){
            dataBaseName = "database";
        }
        this.dataBaseName = dataBaseName;

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:sqlite:" + javaPlugin.getDataFolder().getAbsolutePath() + "/" + dataBaseName + ".db");

        config.setMaximumPoolSize(10);
        config.setPoolName(dataBaseName + "-pool");

        this.dataSource = new HikariDataSource(config);
    }

    @Override
    public void initDataBase(@NotNull String... queries) {
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();

            for(String query: queries) {
                statement.executeUpdate(query);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Could not initialize database schema!");
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public @NotNull DataBaseType getDataBaseType() {
        return DataBaseType.SQLITE;
    }

    @Override
    public void close() {
        dataSource.close();
    }

    @Override
    public @Nullable String getDataBaseName() {
        return dataBaseName;
    }
}
