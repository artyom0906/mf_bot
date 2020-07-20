package com.multifandom.bot.telegram.mf_bot.command;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class CommandContext{

    private final Update event;
    private final List<String> args;

    public CommandContext(Update event, List<String> args) {
        this.event = event;
        this.args = args;
    }


    public Update getEvent() {
        return this.event;
    }

    public List<String> getArgs() {
        return args;
    }
}
