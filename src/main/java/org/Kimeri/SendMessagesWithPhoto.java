package org.Kimeri;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;


public class SendMessagesWithPhoto {

    Bot bot = new Bot();
    SendPhoto sendPhoto = new SendPhoto();
    SendMessage sendMessage = new SendMessage();
    EditMessageText editMessageText = new EditMessageText();
    DataBase dataBase = new DataBase();


    @SneakyThrows
    public void sendStart(Update update) {
        System.out.println("start");
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText(MessageUser.STARTMESSAGEUSER);
        sendMessage.setReplyMarkup(Buttons.mainButtons());
        bot.execute(sendMessage);

    }

    @SneakyThrows
    public void sendCataloge(Update update){
        System.out.println("catalog callback");
        String messageId = update.getCallbackQuery().getInlineMessageId();
        dataBase.handleCatalogCommand(update, "/catalog", messageId);
    }

    @SneakyThrows
    public void sendContact(Update  update, CallbackQuery callbackQuery) {
        System.out.println("contact");
        Message messages = (Message) callbackQuery.getMessage();
        editMessageText.setChatId(callbackQuery.getMessage().getChatId());
        editMessageText.setMessageId(messages.getMessageId());
        editMessageText.setText(MessageUser.CONTACT);
        editMessageText.setReplyMarkup(Buttons.backButton());
        bot.execute(editMessageText);
    }

    @SneakyThrows
    public void sendBackMenu(Update update, CallbackQuery callbackQuery) {
        System.out.println("back");
        dataBase.initCursor();
        Message messages = (Message) callbackQuery.getMessage();
        editMessageText.setChatId(callbackQuery.getMessage().getChatId());
        editMessageText.setMessageId(messages.getMessageId());
        editMessageText.setText(MessageUser.STARTMESSAGEUSER);
        editMessageText.setReplyMarkup(Buttons.mainButtons());
        bot.execute(editMessageText);
    }

    @SneakyThrows
    public void sendBackMenuCatalog(CallbackQuery callbackQuery) {
        DeleteMessage deleteMessage = new DeleteMessage();
        Message messages = (Message) callbackQuery.getMessage();
        deleteMessage.setChatId(callbackQuery.getMessage().getChatId());
        deleteMessage.setMessageId(messages.getMessageId());
        bot.execute(deleteMessage);

    }
}
