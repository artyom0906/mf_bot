package com.multifandom.bot.telegram.mf_bot.main;

import com.multifandom.bot.telegram.mf_bot.command.CommandManager;
import com.multifandom.bot.telegram.mf_bot.utils.Config;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {
    private static Bot INSTANCE;

    public static Bot getINSTANCE() {
        if (INSTANCE == null){
            INSTANCE = new Bot();
        }
        return INSTANCE;
    }
    CommandManager manager = new CommandManager();

    @Override
    public void onUpdateReceived(Update update) {
        manager.handle(update);
    }

    @Override
    public String getBotUsername() {
        return null;
    }

    @Override
    public String getBotToken() {
        return Config.get("TOKEN");
    }

    public void sendMessage(String text, Message message) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText(text);
        execute(sendMessage);
    }
}
