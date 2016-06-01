package edu.uw.alexchow.tradeup.dummy;

import android.location.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uw.alexchow.tradeup.TradeItem;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<TradeItem> ITEMS = new ArrayList<TradeItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, TradeItem> ITEM_MAP = new HashMap<String, TradeItem>();

//    static {
//        // Add some sample items.
//        for (int i = 1; i <= COUNT; i++) {
//            addItem(createDummyItem(i));
//        }
//    }

    public static void addItem(TradeItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static void clearItems() {
        ITEMS.clear();
        ITEM_MAP.clear();
    }

    private static TradeItem createDummyItem(int position) {
        return new TradeItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    public int size() {
        return ITEMS.size();
    }

}
