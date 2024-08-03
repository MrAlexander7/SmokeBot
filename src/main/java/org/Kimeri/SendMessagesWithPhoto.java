package org.Kimeri;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;


public class SendMessagesWithPhoto {

    Bot bot = new Bot();
    SendPhoto sendPhoto = new SendPhoto();


    @SneakyThrows
    public void sendStart(Update update) {
        System.out.println("start");

        sendPhoto.setChatId(update.getMessage().getChatId().toString());
        sendPhoto.setPhoto(new InputFile(MessageUser.STARTURL));
        sendPhoto.setCaption(MessageUser.STARTMESSAGEUSER);
        sendPhoto.setReplyMarkup(Buttons.mainButtons());
        bot.execute(sendPhoto);

    }

}
