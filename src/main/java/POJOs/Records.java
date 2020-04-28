package POJOs;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

public class Records {
    private Multimap<String, Record> recordMultimap;
    private Multimap<String, ImpressionRecord> impressionRecords;
    private Multimap<String, ServerRecord> serverRecords;
    private Multimap<String, ClickRecord> clickRecords;

    public Records(Multimap<String, Record> impressionRecords, Multimap<String, Record> serverRecords, Multimap<String, Record> clickRecords) {
        recordMultimap = ArrayListMultimap.create();

        recordMultimap.putAll(impressionRecords);
        recordMultimap.putAll(serverRecords);
        recordMultimap.putAll(clickRecords);

        this.impressionRecords = setImpressionRecords();
        this.serverRecords = setServerRecords();
        this.clickRecords = setClickRecords();

    }

    public Records(Multimap<String, Record> overallRecords) {

        this.recordMultimap = overallRecords;
        this.impressionRecords = setImpressionRecords();
        this.clickRecords = setClickRecords();
        this.serverRecords = setServerRecords();
    }

    public Multimap<String, Record> getAllRecords() {
        return recordMultimap;
    }

    public Multimap<String, ImpressionRecord> getImpressionRecords() {
        return impressionRecords;
    }

    public Multimap<String, ServerRecord> getServerRecords() {
        return serverRecords;
    }

    public Multimap<String, ClickRecord> getClickRecords() {
        return clickRecords;
    }

    public Multimap setImpressionRecords() {


        Multimap<String, Record> multimap = Multimaps.filterValues(recordMultimap, v -> v instanceof ImpressionRecord);
        /*for (String k: multimap.keySet()) {
            System.out.println(k + "\t" +multimap.get(k));
        }*/
        return multimap;
    }

    public Multimap setServerRecords() {
        return Multimaps.filterValues(recordMultimap, v -> v instanceof ServerRecord);
        /*for (String k: serverView.keySet()) {
            System.out.println(k + "\t" +serverView.get(k));
        }*/

    }

    /* TODO: Overriden function for time intervals */
    public Multimap setClickRecords() {
        /*for (String k: clickView.keySet()) {
            System.out.println(k + "\t" +clickView.get(k));
        }*/
        return Multimaps.filterValues(recordMultimap, v -> v instanceof ClickRecord);
    }
}
