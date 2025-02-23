package pl.bkkuc.cralibs.file;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("unused")
public final class CraFile {

    private final JavaPlugin plugin;
    private final FileConfiguration configuration;

    private final File file;
    private final String name;
    private final String folderName;
    private final String path;

    public CraFile(@NotNull JavaPlugin plugin, @NotNull String name, @Nullable String folderName) {
        this.plugin = plugin;
        this.name = name;
        this.folderName = folderName;
        this.path = plugin.getDataFolder() + (folderName == null ? "" : "/" + folderName);

        File dataFolder = new File(plugin.getDataFolder().getPath());
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        this.file = new File(path, name);
        if (!file.exists()) {
            try {
                plugin.saveResource(folderName != null ? folderName + "/" + name : name, false);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Could not save " + name + " to " + folderName);
            }
        }

        this.configuration = YamlConfiguration.loadConfiguration(file);
    }

    public CraFile(@NotNull JavaPlugin plugin, @NotNull String name) {
        this(plugin, name, null);
    }

    /**
     * Получает имя файла.
     *
     * @return Имя файла.
     */
    public @NotNull String getName() {
        return name;
    }

    /**
     * Получает имя папки, содержащей файл, если применимо.
     *
     * @return Имя папки или null, если файл не находится в конкретной папке.
     */
    public @Nullable String getFolderName() {
        return folderName;
    }

    /**
     * Возвращает конфигурацию, связанную с этим файлом.
     *
     * @return Экземпляр FileConfiguration.
     */
    public @NotNull FileConfiguration getConfig() {
        return configuration;
    }

    /**
     * Сохраняет текущую конфигурацию в файл.
     */
    public void saveConfig() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Возвращает плагин, связанный с этим файлом.
     *
     * @return Экземпляр JavaPlugin.
     */
    public @NotNull JavaPlugin getPlugin() {
        return plugin;
    }

    /**
     * Возвращает путь к файлу.
     *
     * @return Путь к файлу.
     */
    public @NotNull String getPath() {
        return path;
    }

    /**
     * Возвращает файл.
     *
     * @return Файл.
     */
    public @NotNull File getFile() {
        return file;
    }
}
