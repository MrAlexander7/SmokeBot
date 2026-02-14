package org.Kimeri;

import lombok.SneakyThrows;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        DataBase.init();

        DefaultBotOptions options = new DefaultBotOptions();
        options.setMaxThreads(15); 

        try {
            Bot bot = new Bot(options);
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}