package org.Kimeri;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.SneakyThrows;


public class SendMessagesWithPhoto {

    private final Bot bot; // Використовуємо існуючого бота
    private final DataBase dataBase;

    // Конструктор приймає залежності
    public SendMessagesWithPhoto(Bot bot, DataBase dataBase) {
        this.bot = bot;
        this.dataBase = dataBase;
    }

    SendPhoto sendPhoto = new SendPhoto();
    SendMessage sendMessage = new SendMessage();
    EditMessageText editMessageText = new EditMessageText();


    @SneakyThrows
    public void sendStart(Update update) {
        // Створюємо об'єкт локально всередині методу
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText(MessageUser.STARTMESSAGEUSER);
        sendMessage.setReplyMarkup(Buttons.mainButtons());
        bot.execute(sendMessage);
    }

    @SneakyThrows
    public void sendContact(CallbackQuery callbackQuery) {
        Message messages = (Message) callbackQuery.getMessage();
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());
        editMessageText.setMessageId(messages.getMessageId());
        editMessageText.setText(MessageUser.CONTACT);
        editMessageText.setReplyMarkup(Buttons.backButton());
        bot.execute(editMessageText);
    }

    @SneakyThrows
    public void sendContact(Update  update, CallbackQuery callbackQuery) {
        System.out.println("contact");
        Message messages = (Message) callbackQuery.getMessage();
        editMessageText.setChatId(callbackQuery.getMessage().getChatId());
        editMessageText.setMessageId(messages.getMessageId());
        editMessageText.setText(MessageUser.CONTACT);
        editMessageText.setReplyMarkup(Buttons.backButton());
        this.bot.execute(editMessageText);
    }

    @SneakyThrows
    public void sendBackMenu(CallbackQuery callbackQuery) {
        Message messages = (Message) callbackQuery.getMessage();
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());
        editMessageText.setMessageId(messages.getMessageId());
        editMessageText.setText(MessageUser.STARTMESSAGEUSER);
        editMessageText.setReplyMarkup(Buttons.mainButtons());
        bot.execute(editMessageText);
    }

    @SneakyThrows
    public void sendBackMenuCatalog(CallbackQuery callbackQuery) {
        Message messages = (Message) callbackQuery.getMessage();
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(callbackQuery.getMessage().getChatId().toString());
        deleteMessage.setMessageId(messages.getMessageId());
        bot.execute(deleteMessage);
        
        // Після видалення каталогу можна знову надіслати головне меню
        // SendMessage sendMessage = new SendMessage();
        // sendMessage.setChatId(callbackQuery.getMessage().getChatId().toString());
        // sendMessage.setText(MessageUser.STARTMESSAGEUSER);
        // sendMessage.setReplyMarkup(Buttons.mainButtons());
        // bot.execute(sendMessage);
    }
}