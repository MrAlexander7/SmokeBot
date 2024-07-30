package org.Kimeri;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UserHandle {
    SendMessages sendMessages = new SendMessages();

    @SneakyThrows
    public void handle(Update update) {
        if (update.getMessage().getText().equals("/start")) {
            sendMessages.sendStartMessage(update);
        } else {
            System.out.println("default");
            //sendMessages.sendError(update);
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
                sendMessages.sendCatalog(update, callbackQuery);
                break;

            case "/contact":
                System.out.println("contact callback");
                sendMessages.sendContact(update, callbackQuery);
                break;

            case "/help":
                System.out.println("help");
                sendMessages.sendHelp(update, callbackQuery);
                break;

            case "/back":
                System.out.println("back");
                sendMessages.sendBack(update, callbackQuery);
                break;

            case "/next":
                System.out.println("nextPage");
                sendMessages.sendNext(update, callbackQuery);
                break;

            case "/backPage":
                System.out.println("backPage");
                sendMessages.sendBackPage(update, callbackQuery);
                break;

            default:
                System.out.println("default");
                break;
        }
    }
}