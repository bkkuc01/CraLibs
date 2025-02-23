package pl.bkkuc.cralibs.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * Класс ItemSerializer предоставляет методы для сериализации и десериализации {@link ItemStack} в байты и строку {@link Base64}.
 */
public class ItemSerializer {

    /**
     * Преобразует {@link ItemStack} в массив байтов.
     *
     * @param itemStack {@link ItemStack} для преобразования
     * @return массив байтов, представляющий {@link ItemStack}
     * @throws IOException если произошла ошибка ввода-вывода
     */
    public static byte[] toBytes(ItemStack itemStack) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream objectOutputStream = new BukkitObjectOutputStream(outputStream)
        ) {
            objectOutputStream.writeObject(itemStack);
            return outputStream.toByteArray();
        }
    }

    /**
     * Преобразует {@link ItemStack} в строку {@link Base64}.
     *
     * @param itemStack {@link ItemStack} для преобразования
     * @return строка {@link Base64}, представляющая {@link ItemStack}
     * @throws IOException если произошла ошибка ввода-вывода
     */
    public static String toBase64(ItemStack itemStack) throws IOException {
        return Base64.getEncoder().encodeToString(toBytes(itemStack));
    }

    /**
     * Преобразует массив байтов в {@link ItemStack}.
     *
     * @param input массив байтов для преобразования
     * @return {@link ItemStack}, восстановленный из массива байтов
     * @throws IOException если произошла ошибка ввода-вывода
     * @throws ClassNotFoundException если класс ItemStack не найден
     */
    public static ItemStack fromBytes(byte[] input) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
             BukkitObjectInputStream objectInputStream = new BukkitObjectInputStream(inputStream)
        ) {
            return (ItemStack) objectInputStream.readObject();
        }
    }

    /**
     * Преобразует строку {@link Base64} в {@link ItemStack}.
     *
     * @param input строка {@link Base64} для преобразования
     * @return {@link ItemStack}, восстановленный из строки Base64
     * @throws IOException если произошла ошибка ввода-вывода
     * @throws ClassNotFoundException если класс ItemStack не найден
     */
    public static ItemStack fromBase64(String input) throws IOException, ClassNotFoundException {
        return fromBytes(Base64.getDecoder().decode(input));
    }
}
