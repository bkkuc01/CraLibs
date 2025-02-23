package pl.bkkuc.cralibs.command;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandMeta {

    /**
     * Возвращает имя команды.
     *
     * @return Имя команды.
     */
    @NotNull String name();

    /**
     * Возвращает описание команды.
     *
     * @return Описание команды.
     */
    String[] aliases() default {};

    /**
     * Права доступа к команде.
     *
     * @return Права доступа к команде.
     */
    String permission() default "";

    /**
     * Возвращает сообщение о недостаточных правах.
     *
     * @return Сообщение о недостаточных правах.
     */
    String permissionMessage() default "";

    /**
     * Минимальное количество аргументов для команды.
     *
     * @return Минимальное количество аргументов для команды.
     */
    int minArgs() default 0;

    /**
     * Возвращает сообщение о недостаточном количестве аргументов.
     *
     * @return Сообщение о недостаточном количестве аргументов.
     */
    String usage() default "/%s";
}
