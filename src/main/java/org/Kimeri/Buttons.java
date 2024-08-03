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

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(catalog);


        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(contact);

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(row1);

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.setKeyboard(rows);

        return keyboard;
    }

    public static InlineKeyboardMarkup backButton() {
        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText("Головне меню");
        back.setCallbackData("/back:");

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(back);

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(row1);

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.setKeyboard(rows);

        return keyboard;
    }

    public static InlineKeyboardMarkup catalogButtons() {

        InlineKeyboardButton backMain = new InlineKeyboardButton();
        backMain.setText("Головне меню");
        backMain.setCallbackData("/back:");

        InlineKeyboardButton next = new InlineKeyboardButton();
        next.setText(MessageUser.NEXT);
        next.setCallbackData("/next:");

        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText(MessageUser.BACK);
        back.setCallbackData("/backPage:");

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(backMain);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(back);
        row2.add(next);

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(row1);
        rows.add(row2);

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.setKeyboard(rows);

        return keyboard;
    }
}
