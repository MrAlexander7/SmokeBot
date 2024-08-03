package org.Kimeri;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UserHandle {
    Bot bot = new Bot();
    SendMessage message = new SendMessage();
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

                //sendMessagesWithPhoto.sendCatalogMessage(update);

                /*String idCallBack = update.getCallbackQuery().getMessage().getChatId().toString();
                Integer messageId = messages.getMessageId();
                message.setChatId(idCallBack);
                message.setText(MessageUser.CATALOG);
                bot.execute(message);
               dataBase.viewData(idCallBack, update.getCallbackQuery().getId(), messageId, update.getCallbackQuery().getData());*/
                break;

            case "/contact":
                System.out.println("contact");
                //.sendCatalogMessage(update);
                break;

            case "/back":
                System.out.println("back");
                //sendMessagesWithPhoto.sendBackMenu(update);
                break;

            case "/next":
                System.out.println("nextPage");
                //sendMessagesWithPhoto.sendNextPage(update);
                //dataBase.viewData(messages.getChatId().toString(), update.getCallbackQuery().getId(), messages.getMessageId(), update.getCallbackQuery().getData());
                break;

            case "/backPage":
                System.out.println("backPage");
                //sendMessagesWithPhoto.sendBackPage(update);
                //dataBase.viewData(messages.getChatId().toString(), update.getCallbackQuery().getId(), messages.getMessageId(), update.getCallbackQuery().getData());
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
