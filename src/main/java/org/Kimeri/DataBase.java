package org.Kimeri;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.SneakyThrows;
import org.bson.Document;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;

public class DataBase {

    public static final String connectionString = "mongodb+srv://Sasha:koza1985@cluster0.nduzk4r.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
    public static final String  databaseName = "Smoke";
    public static final String  collectionName = "Smoken";

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

    @SneakyThrows
    public void viewData(String connectionString, String databaseName, String collectionName, Update update) {
        //create a method to view data from the MongoDataBase in telegram bot and call it in the UserHandle class
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            System.out.println("Databases:");
            MongoCollection<Document> collection = database.getCollection(collectionName);
            for (Document name : collection.find()) {
                printDocument(name , update);
            }
        }
    }

    @SneakyThrows
    private void printDocument(Document name, Update update) {
        System.out.println("printDocument");
        String text = "";
        Set<String> keys = name.keySet();
        for (String key : keys) {
            text += key + " : " + name.get(key) + "\n";
        }
        message.setChatId(update.getMessage().getChatId().toString());
        message.setText(text);
        message.setReplyMarkup(Buttons.backButton());
        bot.execute(message);
        System.out.println("message sent");

    }

    public void addData() {
        //create a method to add data to the database in telegram bot and call it in the UserHandle class

    }
}
