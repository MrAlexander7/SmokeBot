package org.Kimeri;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SendMessages {

    Bot bot = new Bot();
    SendMessage message = new SendMessage();
    EditMessageText editMessageText = new EditMessageText();
    DataBase dataBase = new DataBase();


    @SneakyThrows
    public void sendStartMessage(Update update) {
        System.out.println("start");
        message.setChatId(update.getMessage().getChatId());
        message.setText(MessageUser.STARTMESSAGEUSER);
        message.setReplyMarkup(Buttons.mainButtons());
        bot.execute(message);
    }

    @SneakyThrows
    public void sendCatalog(Update update, CallbackQuery callbackQuery) {
        System.out.println("catalog");
        dataBase.initCursor();
        DataBase.shownDocuments.clear();
        Message messages = (Message) callbackQuery.getMessage();
        String chatId = callbackQuery.getMessage().getChatId().toString();
        Integer messageId = messages.getMessageId();
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId);
        editMessageText.setText(MessageUser.CATALOG + dataBase.viewData(chatId, callbackQuery.getId(), messageId, "/catalog:"));
        editMessageText.setReplyMarkup(Buttons.catalogButtons());
        bot.execute(editMessageText);
    }

    @SneakyThrows
    public void sendHelp(Update update, CallbackQuery callbackQuery) {
        System.out.println("help");
        Message messages = (Message) callbackQuery.getMessage();
        editMessageText.setChatId(callbackQuery.getMessage().getChatId());
        editMessageText.setMessageId(messages.getMessageId());
        editMessageText.setText(MessageUser.HELP);
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
        bot.execute(editMessageText);

    }


    @SneakyThrows
    public void sendBack(Update update , CallbackQuery callbackQuery) {
        System.out.println("back");
        Message messages = (Message) callbackQuery.getMessage();
        editMessageText.setChatId(callbackQuery.getMessage().getChatId());
        editMessageText.setMessageId(messages.getMessageId());
        editMessageText.setText(MessageUser.STARTMESSAGEUSER);
        editMessageText.setReplyMarkup(Buttons.mainButtons());
        bot.execute(editMessageText);
        DataBase.cursor.close();
        DataBase.cursor = null;
        DataBase.currentPage = 0;
    }

    @SneakyThrows
    public void sendNext(Update update, CallbackQuery callbackQuery) {
        System.out.println("nextPage");
        Message messages = (Message) callbackQuery.getMessage();
        editMessageText.setChatId(callbackQuery.getMessage().getChatId());
        editMessageText.setMessageId(messages.getMessageId());
        editMessageText.setText(MessageUser.CATALOG + dataBase.viewData(messages.getChatId().toString(), update.getCallbackQuery().getId(), messages.getMessageId(), update.getCallbackQuery().getData()));
        editMessageText.setReplyMarkup(Buttons.catalogButtons());
        bot.execute(editMessageText);
    }

    @SneakyThrows
    public void sendBackPage(Update update, CallbackQuery callbackQuery) {
        System.out.println("backPage");
        Message messages = (Message) callbackQuery.getMessage();
        editMessageText.setChatId(callbackQuery.getMessage().getChatId());
        editMessageText.setMessageId(messages.getMessageId());
        editMessageText.setText(MessageUser.CATALOG + dataBase.viewData(messages.getChatId().toString(), update.getCallbackQuery().getId(), messages.getMessageId(), update.getCallbackQuery().getData()));
        editMessageText.setReplyMarkup(Buttons.catalogButtons());
        bot.execute(editMessageText);
    }
}
