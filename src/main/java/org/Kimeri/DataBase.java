package org.Kimeri;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.*;
import lombok.SneakyThrows;
import org.bson.Document;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataBase {

    public static final String connectionString = "mongodb+srv://Sasha:koza1985@cluster0.nduzk4r.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
    public static final String databaseName = "Smoke";
    public static final String collectionName = "SmokenPhoto";

    Bot bot = new Bot();
    CallbackQuery callbackQuery = new CallbackQuery();

    private static MongoClient mongoClient;
    public static MongoCursor<Document> cursor;
    public static List<Document> shownDocuments = new ArrayList<>();
    public static int currentPage = 0;
    private static long totalDocuments = 0;

    public static void main(String[] args) {
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();
        // Create a new client and connect to the server
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                // Send a ping to confirm a successful connection
                MongoDatabase database = mongoClient.getDatabase(databaseName);
                database.runCommand(new Document("ping", 1));
                System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
            } catch (MongoException e) {
                e.printStackTrace();
            }
        }
    }

    public void initCursor() {
        System.out.println("initCursor");
        if (cursor == null || !cursor.hasNext()) {
            if (mongoClient == null) {
                mongoClient = MongoClients.create(connectionString);
            }
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            MongoCollection<Document> collection = database.getCollection(collectionName);
            cursor = collection.find().iterator();
            totalDocuments = collection.countDocuments();
            shownDocuments.clear();
            currentPage = 0; // Почати з першої сторінки
        }
    }

    @SneakyThrows
    public void handleCatalogCommand(Update update, String command, String messageID) {
        if (cursor == null || !cursor.hasNext()) {
            initCursor();
        }

        CallbackQuery callbackQuery = update.getCallbackQuery();
        String chatId = callbackQuery.getMessage().getChatId().toString();

        switch (command) {
            case "/catalog":
                sendCatalogMessage(chatId);
                break;
            case "/next":
                sendNextPage(chatId, Integer.valueOf(messageID));
                break;
            case "/backPage":
                sendBackPage(chatId, Integer.valueOf(messageID));
                break;
            default:
                break;
        }
    }

    @SneakyThrows
    private void sendCatalogMessage(String chatId) {
        if (cursor.hasNext()) {
            Document doc = cursor.next();
            shownDocuments.add(doc);

            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(chatId);
            sendPhoto.setPhoto(new InputFile(doc.getString("PhotoUrl")));
            sendPhoto.setCaption(formatCaption(doc));
            sendPhoto.setReplyMarkup(Buttons.catalogButtons());

            bot.execute(sendPhoto);
        }
    }

    private void sendNextPage(String chatId, Integer messageId) {
        if (currentPage < totalDocuments - 1) {
            currentPage++;
            if (currentPage >= shownDocuments.size()) {
                if (cursor.hasNext()) {
                    Document doc = cursor.next();
                    shownDocuments.add(doc);
                }
            }
            sendEditCatalogMessage(chatId, messageId, shownDocuments.get(currentPage));
        } else {
            currentPage = 0;
            initCursor();
            sendEditCatalogMessage(chatId, messageId, shownDocuments.get(currentPage));
        }
    }

    private void sendBackPage(String chatId, Integer messageId) {
        if (currentPage > 0) {
            currentPage--;
        } else {
            currentPage = (int) totalDocuments - 1;
            reinitializeCursorToCurrentPage();
        }
        sendEditCatalogMessage(chatId, messageId, shownDocuments.get(currentPage));
    }

    private void reinitializeCursorToCurrentPage() {
        initCursor();
        for (int i = 0; i < currentPage; i++) {
            cursor.next();
        }
    }

    @SneakyThrows
    private void sendEditCatalogMessage(String chatId, Integer messageId, Document doc) {
            EditMessageMedia editMessageMedia = new EditMessageMedia();
            editMessageMedia.setChatId(chatId);
            editMessageMedia.setMessageId(messageId);

            InputMediaPhoto inputMediaPhoto = new InputMediaPhoto();
            inputMediaPhoto.setMedia(doc.getString("PhotoUrl"));
            editMessageMedia.setMedia(inputMediaPhoto);

            EditMessageCaption editMessageCaption = new EditMessageCaption();
            editMessageCaption.setChatId(chatId);
            editMessageCaption.setMessageId(messageId);
            editMessageCaption.setCaption(formatCaption(doc));

            editMessageMedia.setReplyMarkup(Buttons.catalogButtons());
            editMessageCaption.setReplyMarkup(Buttons.catalogButtons());

            bot.execute(editMessageMedia);
            bot.execute(editMessageCaption);
    }

    private String formatCaption(Document doc) {
        return
                "Назва: " + doc.getString("Назва") + "\n" +
                "Смак: " + doc.getString("Смак") + "\n" +
                "Нікотин: " + doc.getString("Нікотин") + "\n" +
                "Ціна: " + doc.getString("Ціна (Грн)");
    }
}