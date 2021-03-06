package ru.badr.cosplay2.util;

import android.content.Context;

import com.badr.cosplay2.model.cards.Card;
import com.badr.cosplay2.model.cards.CardImage;
import com.badr.cosplay2.model.cards.User;
import com.badr.cosplay2.model.cards.list.ListCard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

import ru.badr.cosplay2.Cosplay2BeanContainer;
import ru.badr.opencon.R;

/**
 * Created by ABadretdinov
 * 19.10.2015
 * 16:33
 */
public class Utils {
    public static final DateFormat SCHEDULE_TIME_FORMAT = new SimpleDateFormat("HH:mm");

    public static volatile Date APP_START_TIME = new Date();

    static {
        SCHEDULE_TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public static boolean isMenuTimeHasCome() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.DAY_OF_MONTH, 20);
        calendar.set(Calendar.MONTH, Calendar.NOVEMBER);
        calendar.set(Calendar.YEAR, 2016);
        return calendar.getTime().before(APP_START_TIME);
    }

    public static boolean isTimeHasCome() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.DAY_OF_MONTH, 11);
        calendar.set(Calendar.MONTH, Calendar.NOVEMBER);
        calendar.set(Calendar.YEAR, 2016);
        return calendar.getTime().before(APP_START_TIME);
    }

    public static String getSmallCardImageUrl(Context context, Card card) {
        return getCardImageUrl(context, card, "small");
    }

    public static String getCardImageUrl(Context context, Card card) {
        return getCardImageUrl(context, card, "large");
    }

    public static String getCardImageUrl(Context context, Card card, String size) {
        if (card == null) {
            return null;
        }
        Properties properties = Cosplay2BeanContainer.getInstance(context).getProperties();
        String baseUrl = properties.getProperty("global.url16");
        if (card instanceof ListCard && ((ListCard) card).getImage() != null) {
            CardImage cardImage = ((ListCard) card).getImage();
            String eventId = properties.getProperty("global.event_id16");
            return baseUrl + "uploads/" + eventId + "/" + card.getId() + "/" + cardImage.getFileName() + "_" + size + ".jpg";
        }
        return baseUrl + "images/avatars/" + card.getUserId() + "_full.png";
    }

    public static String getUserAvatar(Context context, User user) {
        Properties properties = Cosplay2BeanContainer.getInstance(context).getProperties();
        String baseUrl = properties.getProperty("global.url16");
        return baseUrl + "images/avatars/" + user.getId() + "_full.png";
    }

    public static String getUserSex(Context context, User user) {
        switch (user.getSex()) {
            case male:
                return context.getString(R.string.male);
            case female:
                return context.getString(R.string.female);
            default:
                return context.getString(R.string.unknown);
        }
    }
}
