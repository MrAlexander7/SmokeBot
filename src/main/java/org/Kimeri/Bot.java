package org.Kimeri;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Bot extends TelegramLongPollingBot {
    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            execute(SendMessage
                    .builder()
                    .chatId(update.getMessage().getChatId().toString())
                    .text(update.getMessage().getText())
                    .build());
        }
    }

    @Override
    public String getBotUsername() {
        return Message.BOTNAME;
    }

    @Override
    public String getBotToken() {
        return Message.TOKENBOT;
    }
}
