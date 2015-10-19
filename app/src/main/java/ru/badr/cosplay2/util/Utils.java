package ru.badr.cosplay2.util;

import android.content.Context;

import java.util.Properties;

import ru.badr.cosplay2.Cosplay2BeanContainer;
import ru.badr.cosplay2.api.cards.Card;
import ru.badr.cosplay2.api.cards.CardImage;

/**
 * Created by ABadretdinov
 * 19.10.2015
 * 16:33
 */
public class Utils {
    public static String getCardImageUrl(Context context, Card card) {
        Properties properties = Cosplay2BeanContainer.getInstance(context).getProperties();
        String baseUrl = properties.getProperty("global.url");
        if (card.getImage() != null) {
            CardImage cardImage = card.getImage();
            String festId = properties.getProperty("global.fest_id");
            return baseUrl + "uploads/" + festId + "/" + card.getId() + "/" + cardImage.getFilename() + "_large.jpg";
        }
        return baseUrl + "images/avatars/" + card.getUserId() + ".png";
    }
}