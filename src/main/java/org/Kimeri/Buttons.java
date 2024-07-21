package org.Kimeri;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Buttons {
    public static InlineKeyboardMarkup mainButtons() {

        InlineKeyboardButton catalog = new InlineKeyboardButton();
        catalog.setText("Каталог");
        catalog.setCallbackData("/catalog:");

        InlineKeyboardButton contact = new InlineKeyboardButton();
        contact.setText("Контакти");
        contact.setCallbackData("/contact:");

        InlineKeyboardButton help = new InlineKeyboardButton();
        help.setText("Допомога");
        help.setCallbackData("/help:");

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(catalog);


        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(contact);
        row2.add(help);

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(row1);
        rows.add(row2);

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.setKeyboard(rows);

        return keyboard;
    }

    public static InlineKeyboardMarkup backButton() {
        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText("Назад");
        back.setCallbackData("/back:");

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(back);

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(row1);

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.setKeyboard(rows);

        return keyboard;
    }
}
