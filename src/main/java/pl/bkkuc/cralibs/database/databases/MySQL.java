package pl.bkkuc.cralibs.database.databases;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.bkkuc.cralibs.database.Connectable;
import pl.bkkuc.cralibs.database.DataBase;
import pl.bkkuc.cralibs.database.DataBaseType;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL implements DataBase, Connectable {

    private final HikariDataSource dataSource;

    @NotNull
    private final String host,port,username,password,databaseName;

    public MySQL(@NotNull String host, @NotNull String port, @NotNull String username, @NotNull String password, @NotNull String dataBaseName) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.databaseName = dataBaseName;

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + dataBaseName);
        config.setUsername(username);
        config.setPassword(password);

        config.setMaximumPoolSize(10);
        config.setPoolName(dataBaseName + "-pool");

        dataSource = new HikariDataSource(config);
    }

    @Override
    public void initDataBase(@NotNull String... queries) {
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();

            for(String query: queries) {
                statement.executeUpdate(query);
            }
        } catch (SQLException e){
            e.printStackTrace();
            Bukkit.getLogger().severe("Could not initialize database schema!");
        }
    }

    @Override
    public void close() {
        dataSource.close();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public @NotNull DataBaseType getDataBaseType() {
        return DataBaseType.MYSQL;
    }

    @Override
    public @Nullable String getDataBaseName() {
        return databaseName;
    }

    @Override
    public @NotNull String getHost() {
        return host;
    }

    @Override
    public @NotNull String getPort() {
        return port;
    }

    @Override
    public @NotNull String getUserName() {
        return username;
    }

    @Override
    public @NotNull String getPassword() {
        return password;
    }
}
