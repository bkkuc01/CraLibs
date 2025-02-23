package pl.bkkuc.cralibs.util;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class ActionExecutor {

    public enum ActionType {
        MESSAGE("[message]", new MessageCommand()),
        TITLE("[title]", new TitleCommand()),
        BROADCAST("[broadcast]", new BroadcastCommand()),
        SOUND("[sound]", new SoundCommand()),
        BROADCAST_SOUND("[broadcast-sound]", new BroadcastSoundCommand()),
        PLAYER_CHAT("[player]", new PlayerChatCommand()),
        CONSOLE("[console]", new ConsoleCommand()),
        ACTIONBAR("[actionbar]", new ActionBarCommand()),
        BROADCAST_TITLE("[broadcast-title]", new BroadcastTitleCommand()),
        CLOSE("[close]", new CloseCommand()),
        ;

        private final String identifier;
        private final ActionCommand command;

        ActionType(String identifier, ActionCommand command) {
            this.identifier = identifier;
            this.command = command;
        }

        public String getIdentifier() {
            return identifier;
        }

        public ActionCommand getCommand() {
            return command;
        }

        public static ActionType fromIdentifier(String identifier) {
            for (ActionType action : values()) {
                if (action.getIdentifier().equalsIgnoreCase(identifier)) {
                    return action;
                }
            }
            return null;
        }
    }

    /**
     * Выполняет действия из списка actions.
     *
     * @param player    Игрок, для которого выполняются действия.
     * @param actions   Список действий.
     * @param replacers Список плейсхолдеров.
     */
    public static void doActions(@Nullable Player player, List<String> actions, List<Replacer> replacers) {
        if (actions == null || actions.isEmpty()) return;

        List<Replacer> filteredReplacers = replacers.stream()
                .filter(Objects::nonNull)
                .toList();

        actions.stream()
                .filter(Objects::nonNull)
                .map(action -> prepareAction(player, action, filteredReplacers))
                .forEach(action -> executeAction(player, action));
    }

    private static String prepareAction(@Nullable Player player, String action, List<Replacer> replacers) {
        if (player != null) {
            action = action.replace("{player}", player.getName());
        }
        action = Util.safePAPI(player, action);

        for (Replacer replacer : replacers) {
            action = replacer.replace(action);
        }

        return action;
    }

    private static void executeAction(@Nullable Player player, String action) {
        final String[] params = action.split(" ");
        final String commandKey = params[0].toLowerCase();

        ActionType actionType = ActionType.fromIdentifier(commandKey);
        if (actionType == null) return;

        final ExecutionContext context = new ExecutionContext(player, params);
        actionType.getCommand().execute(context);
    }

    // Интерфейс для команды
    public interface ActionCommand {
        void execute(@NotNull ExecutionContext context);
    }

    // Команда для отправки сообщения игроку
    public static final class MessageCommand implements ActionCommand {
        @Override
        public void execute(@NotNull ExecutionContext context) {
            Player player = context.player();
            if (player != null) {
                PlayerUtil.sendMessage(player, ATS(context.params(), 1));
            }
        }
    }

    // Команда для отправки титула игроку или всем
    public static final class TitleCommand implements ActionCommand {
        @Override
        public void execute(@NotNull ExecutionContext context) {
            final String[] titleParts = ColorUtil.colorize(ATS(context.params(), 1)).split(";");
            if (context.player() != null) {
                context.player().sendTitle(titleParts[0], titleParts.length >= 2 ? titleParts[1] : "");
            } else {
                for (final Player online : Bukkit.getOnlinePlayers()) {
                    online.sendTitle(titleParts[0], titleParts.length >= 2 ? titleParts[1] : "");
                }
            }
        }
    }

    // Команда для трансляции сообщения
    public static final class BroadcastCommand implements ActionCommand {
        @Override
        public void execute(@NotNull ExecutionContext context) {
            final String message = ColorUtil.colorize(ATS(context.params(), 1));
            ChatUtil.broadcastMessage(message, context.player());
        }
    }

    // Команда для проигрывания звука для игрока
    public static final class SoundCommand implements ActionCommand {
        @Override
        public void execute(@NotNull ExecutionContext context) {
            final Player player = context.player();
            if (player != null) {
                try {
                    Sound sound = Sound.valueOf(context.params()[1].toUpperCase());
                    float volume = context.params().length >= 3 ? Float.parseFloat(context.params()[2]) : 1.0f;
                    float pitch = context.params().length >= 4 ? Float.parseFloat(context.params()[3]) : 1.0f;
                    player.playSound(player.getLocation(), sound, volume, pitch);
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
    }

    // Команда для трансляции звука всем игрокам
    public static final class BroadcastSoundCommand implements ActionCommand {
        @Override
        public void execute(@NotNull ExecutionContext context) {
            Sound sound;
            try {
                sound = Sound.valueOf(context.params()[1].toUpperCase());
            } catch (IllegalArgumentException e) {
                return;
            }

            final float volume = context.params().length >= 3 ? Float.parseFloat(context.params()[2]) : 1.0f;
            final float pitch = context.params().length >= 4 ? Float.parseFloat(context.params()[3]) : 1.0f;

            for (final Player online : Bukkit.getOnlinePlayers()) {
                online.playSound(online.getLocation(), sound, volume, pitch);
            }
        }
    }

    // Команда для отправки сообщения от лица игрока (чат)
    public static final class PlayerChatCommand implements ActionCommand {
        @Override
        public void execute(@NotNull ExecutionContext context) {
            Player player = context.player();
            if (player != null) {
                player.chat(ATS(context.params(), 1));
            }
        }
    }

    // Команда для выполнения команды от лица консоли
    public static final class ConsoleCommand implements ActionCommand {
        @Override
        public void execute(@NotNull ExecutionContext context) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ATS(context.params(), 1));
        }
    }

    // Команда для отправки сообщения в ActionBar игрока
    public static final class ActionBarCommand implements ActionCommand {
        @Override
        public void execute(@NotNull ExecutionContext context) {
            Player player = context.player();
            if (player != null) {
                PlayerUtil.sendActionBar(player, ATS(context.params(), 1));
            }
        }
    }

    // Команда для отправки титула всем игрокам
    public static final class BroadcastTitleCommand implements ActionCommand {
        @Override
        public void execute(@NotNull ExecutionContext context) {
            final String[] titleParts = ColorUtil.colorize(ATS(context.params(), 1)).split(";");
            for (final Player online : Bukkit.getOnlinePlayers()) {
                online.sendTitle(titleParts[0], titleParts.length >= 2 ? titleParts[1] : "");
            }
        }
    }

    public static final class CloseCommand implements ActionCommand {
        @Override
        public void execute(@NotNull ExecutionContext context) {
            Player player = context.player();
            if (player != null) {
                player.closeInventory();
            }
        }
    }

    public static String ATS(String[] array, int index) {
        return String.join(" ", Arrays.copyOfRange(array, index, array.length));
    }

    public record ExecutionContext(Player player, String[] params) {}
}
