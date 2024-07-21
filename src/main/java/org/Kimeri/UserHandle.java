package org.Kimeri;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UserHandle {
    Bot bot = new Bot();
    SendMessage message = new SendMessage();

    DataBase dataBase = new DataBase();
    @SneakyThrows
    public void handle(Update update) {
        switch (update.getMessage().getText()) {
            case "/start":
                System.out.println("start");
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText(MessageUser.STARTMESSAGEUSER);
                message.setReplyMarkup(Buttons.mainButtons());
                bot.execute(message);
                break;
            case "/help":
                System.out.println("help");
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText(MessageUser.HELP);
                message.setReplyMarkup(Buttons.backButton());
                bot.execute(message);
                break;
            case "/catalog":
                System.out.println("catalog");
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText(MessageUser.CATALOG);
                bot.execute(message);
                dataBase.viewData(DataBase.connectionString, DataBase.databaseName, DataBase.collectionName, update);
                break;
            case "/contact":
                System.out.println("contact");
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText(MessageUser.CONTACT);
                message.setReplyMarkup(Buttons.backButton());
                bot.execute(message);
                break;
            default:
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText("Не вірна команда");
                bot.execute(message);
                break;
        }
    }
    @SneakyThrows
    public void handleCallback(Update update, CallbackQuery callbackQuery) {
        System.out.println("callback");

        SendMessage message = new SendMessage();

        Message messages = (Message) callbackQuery.getMessage();
        String[] data = callbackQuery.getData().split(":");
        String command = data[0];

        switch (command) {
            case "/catalog":
                System.out.println("catalog callback");
                message.setChatId(messages.getChatId().toString());
                message.setText(MessageUser.CATALOG);
                bot.execute(message);
                dataBase.viewData(DataBase.connectionString, DataBase.databaseName, DataBase.collectionName, update);
                break;
            case "/contact":
                System.out.println("contact");
                message.setChatId(messages.getChatId().toString());
                message.setText(MessageUser.CONTACT);
                message.setReplyMarkup(Buttons.backButton());
                bot.execute(message);
                break;
            case "/help":
                System.out.println("help");
                message.setChatId(messages.getChatId().toString());
                message.setText(MessageUser.HELP);
                message.setReplyMarkup(Buttons.backButton());
                bot.execute(message);
                break;
            case "/back":
                System.out.println("back");
                message.setChatId(messages.getChatId().toString());
                message.setText(MessageUser.STARTMESSAGEUSER);
                message.setReplyMarkup(Buttons.mainButtons());
                bot.execute(message);
                break;
            default:
                System.out.println("default");
                message.setChatId(messages.getChatId().toString());
                message.setText("Не вірна команда");
                bot.execute(message);
                break;
        }
    }
}
