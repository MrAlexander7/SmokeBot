package org.Kimeri;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Bot extends TelegramLongPollingBot {
    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().getChatId().equals(MessageUser.ADMINID)) {
            System.out.println("Admin");
            AdminHandle adminHandle = new AdminHandle();
            adminHandle.handle(update);
        } else if (update.hasMessage() && update.getMessage().hasText()){
            System.out.println("User");
            UserHandle userHandle = new UserHandle();
            userHandle.handle(update);
        } else {
            UserHandle userHandle = new UserHandle();
            userHandle.handleCallback(update, update.getCallbackQuery());
        }
    }

    @Override
    public String getBotUsername() {
        return MessageUser.BOTNAME;
    }

    @Override
    public String getBotToken() {
        return MessageUser.TOKENBOT;
    }
}
