package org.Kimeri;

import com.mongodb.client.*;
import lombok.SneakyThrows;
import org.bson.Document;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;

import java.util.ArrayList;

public class DataBase {
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private final Bot bot;

    public DataBase(Bot bot) {
        this.bot = bot;
    }

    public static void init() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create("Str connect");
            database = mongoClient.getDatabase("Smoke");
        }
    }

    @SneakyThrows
    public void sendCatalogMessage(String chatId, int page, Integer messageId) {
        MongoCollection<Document> collection = database.getCollection("SmokenPhoto");
        long total = collection.countDocuments();
        
        if (page < 0) page = (int) total - 1;
        if (page >= total) page = 0;

        Document doc = collection.find().skip(page).limit(1).first();

        if (doc != null) {
            String photoUrl = doc.getString("PhotoUrl");
            String caption = formatCaption(doc);

            if (messageId == null) {
                // Якщо повідомлення ще немає — надсилаємо нове
                SendPhoto photo = new SendPhoto();
                photo.setChatId(chatId);
                photo.setPhoto(new InputFile(photoUrl));
                photo.setCaption(caption);
                photo.setReplyMarkup(Buttons.catalogButtons(page));
                bot.execute(photo);
            } else {
                // Якщо повідомлення вже є — замінюємо його вміст
                EditMessageMedia editMedia = new EditMessageMedia();
                editMedia.setChatId(chatId);
                editMedia.setMessageId(messageId);

                InputMediaPhoto inputMediaPhoto = new InputMediaPhoto();
                inputMediaPhoto.setMedia(photoUrl);
                inputMediaPhoto.setCaption(caption); // Важливо: підпис тепер у Media
                
                editMedia.setMedia(inputMediaPhoto);
                editMedia.setReplyMarkup(Buttons.catalogButtons(page));

                bot.execute(editMedia);
            }
        }
    }

    private String formatCaption(Document doc) {
        return 
                "Назва: " + doc.getString("Назва") + "\n" +
                "Смак: " + doc.getString("Смак") + "\n" +
                "Нікотин: " + doc.getString("Нікотин") + "\n" +
                "Ціна: " + doc.getString("Ціна (Грн)");
    }
}
