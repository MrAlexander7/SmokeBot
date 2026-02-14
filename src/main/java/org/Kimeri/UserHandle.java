package org.Kimeri;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UserHandle {
    private final Bot bot;
    private final SendMessagesWithPhoto sendMessagesWithPhoto;
    private final DataBase dataBase;

    public UserHandle(Bot bot) {
        this.bot = bot;
        this.dataBase = new DataBase(bot); // Передаємо бота в базу
        this.sendMessagesWithPhoto = new SendMessagesWithPhoto(bot, dataBase);
    }

    @SneakyThrows
    public void handle(Update update) {
        String text = update.getMessage().getText();
        if (text.equals("/start")) {
            sendMessagesWithPhoto.sendStart(update);
        } else {
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText("Не вірна команда");
            bot.execute(message);
        }
    }

    @SneakyThrows
    public void handleCallback(Update update, CallbackQuery callbackQuery) {
        String[] data = callbackQuery.getData().split(":");
        String command = data[0];
        String chatId = callbackQuery.getMessage().getChatId().toString();

        // Безпечно отримуємо MessageId, якщо це повідомлення
        Integer messageId = null;
        if (callbackQuery.getMessage() instanceof Message message) {
            messageId = message.getMessageId();
        }

        switch (command) {
            case "/catalog":
                // При першому натисканні на "Каталог" передаємо null як messageId, 
                // щоб надіслати НОВЕ повідомлення з картинкою
                dataBase.sendCatalogMessage(chatId, 0, null);
                break;

            case "/next":
                // Витягуємо номер сторінки з даних кнопки (наприклад, "/next:1")
                int nextPage = data.length > 1 ? Integer.parseInt(data[1]) : 0;
                dataBase.sendCatalogMessage(chatId, nextPage, messageId);
                break;

            case "/backPage":
                int prevPage = data.length > 1 ? Integer.parseInt(data[1]) : 0;
                dataBase.sendCatalogMessage(chatId, prevPage, messageId);
                break;

            case "/backCatalog":
                sendMessagesWithPhoto.sendBackMenuCatalog(callbackQuery);
                break;
            case "/contact":
                sendMessagesWithPhoto.sendContact(callbackQuery);
                break;
            case "/back":
                sendMessagesWithPhoto.sendBackMenu(callbackQuery);
                break;
        }
    }
}