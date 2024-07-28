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

    int backPage = 0;

    @SneakyThrows
    public void handle(Update update) {
        switch (update.getMessage().getText()) {
            case "/start":
                System.out.println("start");
                if (DataBase.editMode == 1) {
                    DataBase.editMode = 0;
                    DataBase.cursor.close();
                    DataBase.cursor = null;
                }
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
                String id = update.getMessage().getChatId().toString();
                message.setChatId(id);
                message.setText(MessageUser.CATALOG);
                bot.execute(message);
                dataBase.initCursor();
                dataBase.viewData(id, null, update.getMessage().getMessageId(), "/next:");
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
        dataBase.initCursor();

        SendMessage message = new SendMessage();

        Message messages = (Message) callbackQuery.getMessage();
        String[] data = callbackQuery.getData().split(":");
        String command = data[0];

        switch (command) {
            case "/catalog":
                System.out.println("catalog callback");
                String idCallBack = update.getCallbackQuery().getMessage().getChatId().toString();
                Integer messageId = messages.getMessageId();
                message.setChatId(idCallBack);
                message.setText(MessageUser.CATALOG);
                bot.execute(message);
               dataBase.viewData(idCallBack, update.getCallbackQuery().getId(), messageId, update.getCallbackQuery().getData());
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
                DataBase.editMode = 0;
                message.setChatId(messages.getChatId().toString());
                message.setText(MessageUser.STARTMESSAGEUSER);
                message.setReplyMarkup(Buttons.mainButtons());
                bot.execute(message);
                break;

            case "/next":
                System.out.println("nextPage");
                dataBase.viewData(messages.getChatId().toString(), update.getCallbackQuery().getId(), messages.getMessageId(), update.getCallbackQuery().getData());
                break;

            case "/backPage":
                System.out.println("backPage");
                dataBase.viewData(messages.getChatId().toString(), update.getCallbackQuery().getId(), messages.getMessageId(), update.getCallbackQuery().getData());
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
