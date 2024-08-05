package org.Kimeri;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.*;
import lombok.SneakyThrows;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class DataBase {

    public static final String connectionString = "mongodb+srv://Sasha:koza1985@cluster0.nduzk4r.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
    public static final String databaseName = "Smoke";
    public static final String collectionName = "Smoken";

    StringBuilder text = new StringBuilder();

    private static MongoClient mongoClient;
    public static MongoCursor<Document> cursor;
    public static List<Document> shownDocuments = new ArrayList<>();
    private static int limit = 2;
    public static int currentPage = 0;
    private static long totalDocuments = 0;
    private static int stopNextPage = 0;

    public static void main(String[] args) {
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .applyToSocketSettings(builder -> builder.connectTimeout(60, TimeUnit.SECONDS))
                .applyToSslSettings(builder -> builder.enabled(true))
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
        }
    }

    @SneakyThrows
    public String viewData(String chatId, String callbackQueryId, Integer messageId, String callbackData) {
        //create a method to view data from the MongoDataBase in telegram bot and call it in the UserHandle class
        System.out.println("viewData");

        int count = 0;

        text.setLength(0);

        if ("/next:".equals(callbackData) || "/catalog:".equals(callbackData)) {
            if (currentPage * limit < totalDocuments) {
                currentPage++;
                int startIndex = (currentPage - 1) * limit;
                if (shownDocuments.size() < startIndex + limit) {
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
                    int endIndex = Math.min(startIndex + limit, shownDocuments.size());
                    for (int i = startIndex; i < endIndex; i++) {
                        printDocument(shownDocuments.get(i), text);
                    }
                }
            } else {
                int pages = (int) Math.ceil((double) totalDocuments / limit);
                if (stopNextPage == 1) {
                    text.append("Ви досягли кінця списку.\n");
                } else {
                    currentPage++;
                    stopNextPage = 1;
                    if (currentPage >= pages) {
                        text.append("Ви досягли кінця списку.\n");

                    }
                }
                //text.append("Ви досягли кінця списку.\n");
            }
        } else if ("/backPage:".equals(callbackData)) {
            if (currentPage > 1) {
                currentPage--;
                int startIndex = (currentPage - 1) * limit;
                int endIndex = Math.min(startIndex + limit, shownDocuments.size());
                for (int i = startIndex; i < endIndex; i++) {
                    printDocument(shownDocuments.get(i), text);
                }
            } else {
                currentPage = 0;
                text.append("Ви досягли початку списку.\n");
            }
        }

        String pageNumberText = "Сторінка " + currentPage + "\n\n";
        text.insert(0, pageNumberText);

        return text.toString();

    }

    @SneakyThrows
    private void printDocument(Document name, StringBuilder text) {
        System.out.println("printDocument");
        Set<String> keys = name.keySet();
        for (String key : keys) {
            if (!key.equals("_id")) {
                if (key.equals("Назва")) {
                    text.append("----------").append("\n").append(key).append(": ").append(name.get(key)).append("\n");
                } else {
                    text.append(key).append(": ").append(name.get(key)).append("\n");
                }
            }
        }
    }
}
