package com.multifandom.bot.telegram.mf_bot.commands;

import com.multifandom.bot.telegram.mf_bot.command.CommandContext;
import com.multifandom.bot.telegram.mf_bot.command.ICommand;
import com.multifandom.bot.telegram.mf_bot.main.Bot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class StartCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx){
        Update event = ctx.getEvent();
        if(ctx.getArgs().isEmpty()) {
            Message message = event.getMessage();
            User author = message.getFrom();
            String language = author.getLanguageCode();
            ResourceBundle bundle = ResourceBundle.getBundle("language", new Locale(language));

            try {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText(String.format(bundle.getString("start_message"), author.getFirstName()));
                sendMessage.setChatId(message.getChatId());

                InlineKeyboardMarkup inlineKeyboardMarkup = createKeyboard(bundle);
                sendMessage.setReplyMarkup(inlineKeyboardMarkup);

                Bot.getINSTANCE().execute(sendMessage);
                System.out.println("receive: " + message.getMessageId());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }else {
            System.out.println(event.getCallbackQuery().getData());
            EditMessageText editMessage = new EditMessageText();
            if(ctx.getArgs().get(0).equals("yes_lang")){
                editMessage.setText(event.getCallbackQuery().getMessage().getText());
            }else {
                ResourceBundle bundle = ResourceBundle.getBundle("language", new Locale(ctx.getArgs().get(0)));
                editMessage.setText(String.format(bundle.getString("start_message"), event.getCallbackQuery().getFrom().getFirstName()));
                editMessage.setReplyMarkup(createKeyboard(bundle));
            }
            editMessage.setMessageId(event.getCallbackQuery().getMessage().getMessageId());
            editMessage.setChatId(event.getCallbackQuery().getMessage().getChatId());
            editMessage.setInlineMessageId(event.getCallbackQuery().getInlineMessageId());
            System.out.println(editMessage);
            try {
                Bot.getINSTANCE().execute(editMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }
    }
    private InlineKeyboardMarkup createKeyboard(ResourceBundle bundle){
        List<String> languages = new ArrayList<>();
        languages.add("ru");
        languages.add("en");
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();

        languages.forEach(v->{
            if(!v.equals(bundle.getLocale().getLanguage())){
                keyboardButtonsRow1.add(new InlineKeyboardButton().setText(v).setCallbackData("start " + v));
            }
        });
        InlineKeyboardButton yes = new InlineKeyboardButton();
        yes.setText(bundle.getString("yes_btn"));
        yes.setCallbackData("start yes_lang");

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(yes);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    @Override
    public String getName() {
        return "start";
    }
}
