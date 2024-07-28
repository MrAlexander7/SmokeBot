package org.Kimeri;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.*;
import lombok.SneakyThrows;
import org.bson.Document;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DataBase {

    public static final String connectionString = "mongodb+srv://Sasha:koza1985@cluster0.nduzk4r.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
    public static final String databaseName = "Smoke";
    public static final String collectionName = "Smoken";

    StringBuilder text = new StringBuilder();

    private static MongoClient mongoClient;
    public static MongoCursor<Document> cursor;
    private static List<Document> shownDocuments = new ArrayList<>();
    private static int limit = 3;
    public static int editMode = 0;
    private static int currentPage = 0;

    Bot bot = new Bot();
    SendMessage message = new SendMessage();

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
        if (cursor == null || !cursor.hasNext()) {
            if (mongoClient == null) {
                mongoClient = MongoClients.create(connectionString);
            }
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            MongoCollection<Document> collection = database.getCollection(collectionName);
            cursor = collection.find().iterator();
        }
    }

    @SneakyThrows
    public void viewData(String chatId, String callbackQueryId, Integer messageId, String callbackData) {
        //create a method to view data from the MongoDataBase in telegram bot and call it in the UserHandle class
        System.out.println("viewData");

        int count = 0;
        if ("/next:".equals(callbackData) || "/catalog:".equals(callbackData)) {
            currentPage++;
            if (shownDocuments.size() < currentPage * limit) {
                while (cursor.hasNext() && count < limit) {
                    Document doc = cursor.next();
                    shownDocuments.add(doc);
                    printDocument(doc, text);
                    count++;
                }
                if (!cursor.hasNext()) {
                    cursor.close();
                    cursor = null;
                }
            } else {
                int startIndex = (currentPage - 1) * limit;
                int endIndex = Math.min(startIndex + limit, shownDocuments.size());
                for (int i = startIndex; i < endIndex; i++) {
                    printDocument(shownDocuments.get(i), text);
                }
            }
        } else if ("/backPage:".equals(callbackData) && currentPage > 1) {
            currentPage--;
            int startIndex = (currentPage - 1) * limit;
            int endIndex = Math.min(startIndex + limit, shownDocuments.size());
            for (int i = startIndex; i < endIndex; i++) {
                printDocument(shownDocuments.get(i), text);
            }
        }


        /*while (cursor.hasNext() && count < limit) {
                    Document name = cursor.next();
                    printDocument(name, text);
                    count++;
                }

            if (!cursor.hasNext()) {
                cursor.close();
                cursor = null;
            }*/

        if (editMode == 1) {
            if (callbackQueryId != null) {
                EditMessageText newMessage = new EditMessageText();
                newMessage.setChatId(chatId);
                newMessage.setMessageId(messageId);
                newMessage.setText(text.toString());
                newMessage.setReplyMarkup(Buttons.catalogButtons());
                bot.execute(newMessage);
            }
        } else {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(text.toString());
            message.setReplyMarkup(Buttons.catalogButtons());
            editMode = 1;
            bot.execute(message);

        }

    }

    @SneakyThrows
    private void printDocument(Document name, StringBuilder text) {
        System.out.println("printDocument");
        Set<String> keys = name.keySet();
        for (String key : keys) {
            if (!key.equals("_id")) {
                if (key.equals("Назва")) {
                    text.append("----------").append("\n").append(key).append(": ").append(name.get(key)).append("\n");
                    //text += "-----------------" + "\n" + key + " : " + name.get(key) + "\n";
                } else {
                    text.append(key).append(": ").append(name.get(key)).append("\n");
                    //text += key + " : " + name.get(key) + "\n";
                }
            }
        }
    }
}
