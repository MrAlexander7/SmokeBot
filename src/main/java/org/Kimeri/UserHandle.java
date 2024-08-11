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
    SendMessagesWithPhoto sendMessagesWithPhoto = new SendMessagesWithPhoto();

    @SneakyThrows
    public void handle(Update update) {
        if (update.getMessage().getText().equals("/start")) {
            System.out.println("start");
            sendMessagesWithPhoto.sendStart(update);
        } else {
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText("Не вірна команда");
            bot.execute(message);
        }
    }
    @SneakyThrows
    public void handleCallback(Update update, CallbackQuery callbackQuery) {
        System.out.println("callback");


        Message messages = (Message) callbackQuery.getMessage();
        String[] data = callbackQuery.getData().split(":");
        String command = data[0];

        switch (command) {
            case "/catalog":
                System.out.println("catalog callback");
                sendMessagesWithPhoto.sendCataloge(update);
                break;

            case "/contact":
                System.out.println("contact");
                sendMessagesWithPhoto.sendContact(update, callbackQuery);
                break;

            case "/back":
                System.out.println("back");
                sendMessagesWithPhoto.sendBackMenu(update, callbackQuery);
                break;

            case "/next":
                System.out.println("nextPage");
                dataBase.handleCatalogCommand(update, "/next", messages.getMessageId().toString());
                //sendMessagesWithPhoto.sendNextPage(update);
                //dataBase.viewData(messages.getChatId().toString(), update.getCallbackQuery().getId(), messages.getMessageId(), update.getCallbackQuery().getData());
                break;

            case "/backPage":
                System.out.println("backPage");
                dataBase.handleCatalogCommand(update, "/backPage", messages.getMessageId().toString());
                //sendMessagesWithPhoto.sendBackPage(update);
                //dataBase.viewData(messages.getChatId().toString(), update.getCallbackQuery().getId(), messages.getMessageId(), update.getCallbackQuery().getData());
                break;

            case "/backCatalog":
                System.out.println("backCatalog");
                sendMessagesWithPhoto.sendBackMenuCatalog(callbackQuery);
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
