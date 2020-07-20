package com.multifandom.bot.telegram.mf_bot.command;

import com.multifandom.bot.telegram.mf_bot.commands.StartCommand;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();

    public CommandManager() {
        addCommand(new StartCommand());
    }

    private void addCommand(ICommand cmd) {
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));

        if (nameFound) {
            throw new IllegalArgumentException("A command with this name is already present");
        }
        commands.add(cmd);
    }

    @Nullable
    private ICommand getCommand(String search) {
        String searchLower = search.toLowerCase();
        for (ICommand cmd : commands) {
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                return cmd;
            }
        }

        return null;
    }

    public void handle(Update event) {
        if (event.hasMessage()) {
            execute_command(event.getMessage().getText(), event);
        } else if (event.hasCallbackQuery()) {
            execute_command(event.getCallbackQuery().getData(), event);
        }
    }
    private void execute_command(String command, Update event){
        String[] split = command
                .replaceFirst("(?i)" + Pattern.quote("/"), "").split("\\s");
        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);

        if (cmd != null) {
            List<String> args = Arrays.asList(split).subList(1, split.length);

            CommandContext ctx = new CommandContext(event, args);

            cmd.handle(ctx);
        }
    }
}
