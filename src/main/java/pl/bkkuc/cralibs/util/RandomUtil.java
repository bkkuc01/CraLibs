package pl.bkkuc.cralibs.util;

import java.util.Arrays;
import java.util.Random;

public final class RandomUtil {

    public static final char[] ENGLISH_LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    public static final char[] RUSSIAN_LETTERS = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ".toCharArray();

    public static final char[] SYMBOLS = "!@#$%^&*()_+-=[]{}|;:'\",.<>?/".toCharArray();
    public static final char[] NUMBERS = "0123456789".toCharArray();

    private static final Random RANDOM = new Random();

    /**
     * Генерирует случайный пароль заданной длины.
     *
     * @param length длина пароля
     * @return сгенерированный пароль
     */
    public static String generateRandomPassword(int length) {
        String charSet = Arrays.toString(ENGLISH_LETTERS) + Arrays.toString(NUMBERS) + Arrays.toString(SYMBOLS);
        return generateRandomString(charSet, length);
    }

    /**
     * Генерирует случайный код заданной длины.
     *
     * @param length длина кода
     * @return сгенерированный код
     */
    public static int generateRandomCode(int length) {
        return Integer.parseInt(generateRandomString(Arrays.toString(NUMBERS), length));
    }

    /**
     * Генерирует случайную строку из заданного набора символов.
     *
     * @param charSet набор символов
     * @param length длина строки
     * @return сгенерированная строка
     */
    private static String generateRandomString(String charSet, int length) {
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(charSet.length());
            stringBuilder.append(charSet.charAt(randomIndex));
        }
        return stringBuilder.toString();
    }

    /**
     * Получить случайное число в диапазоне.
     *
     * @param min минимальное значение (включительно)
     * @param max максимальное значение (исключительно)
     * @return случайное целое число в заданном диапазоне.
     */
    public static int randomInt(int min, int max) {
        return RANDOM.nextInt(max - min) + min;
    }

    /**
     * Получить случайное число в диапазоне.
     *
     * @param min минимальное значение (включительно)
     * @param max максимальное значение (исключительно)
     * @return случайное число с плавающей запятой в заданном диапазоне.
     */
    public static double randomDouble(double min, double max) {
        return min + (max - min) * RANDOM.nextDouble();
    }
}
