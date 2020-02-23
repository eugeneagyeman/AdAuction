package DataHandler;

import DataHandler.pojos.ClickRecord;
import DataHandler.pojos.ImpressionRecord;
import DataHandler.pojos.Record;
import DataHandler.pojos.ServerRecord;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import java.util.Collection;
import java.util.HashMap;

class DataStorage {
    private static final Multimap<String, Record> storage = ArrayListMultimap.create();
    private static Multimap<String, Record> impressionView = ArrayListMultimap.create();
    private static Multimap<String, Record> serverView = ArrayListMultimap.create();
    private static Multimap<String, Record> clickView = ArrayListMultimap.create();

    DataStorage() {
        System.out.println("Initialising Storage...");
    }

    static Multimap getImpressionRecords() {
        impressionView = Multimaps.filterValues(storage, v -> v instanceof ImpressionRecord);
        /*for (String k: impressionView.keySet()) {
            System.out.println(k + "\t" +impressionView.get(k));
        }*/
        return impressionView;
    }

    public static Multimap getServerRecords() {
        serverView = Multimaps.filterValues(storage, v -> v instanceof ServerRecord);
        /*for (String k: serverView.keySet()) {
            System.out.println(k + "\t" +serverView.get(k));
        }*/
        return serverView;
    }

    public static Multimap getClickRecords() {
        clickView = Multimaps.filterValues(storage, v -> v instanceof ClickRecord);
        /*for (String k: clickView.keySet()) {
            System.out.println(k + "\t" +clickView.get(k));
        }*/
        return clickView;
    }

    Multimap<String, Record> getStorage() {
        return storage;
    }

    public void idMappings() {
        for (String keys : storage.keySet()) {
            System.out.println(keys + "\t" + storage.get(keys));
        }

    }
}
