package pl.bkkuc.cralibs.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class CommandWrapper extends Command {

    private final CraCommand command;

    CommandWrapper(@NotNull String name, @NotNull CraCommand command) {
        super(name);
        this.command = command;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
        return command.onCommand(sender, this, commandLabel, args);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
        return command.onTabComplete(sender, this, alias, args);
    }
}
