package org.Kimeri;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Bot extends TelegramLongPollingBot {

    private final UserHandle userHandle;

    public Bot(DefaultBotOptions options) {
        super(options);
        this.userHandle = new UserHandle(this); // Передаємо посилання на бота
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            userHandle.handle(update);
        } else if (update.hasCallbackQuery()) {
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
