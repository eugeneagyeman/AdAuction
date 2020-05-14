package POJOs;

import com.google.common.collect.*;
import metrics.Metrics;
import org.apache.commons.collections.CollectionUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Records {
    private String context;
    private  ImmutableListMultimap<String,Record> immutableRecordMultimap;
    private  ImmutableListMultimap<String,ServerRecord> immutableServerRecordMultimap;
    private  ImmutableListMultimap<String,ClickRecord> immutableClickRecordMultimap;
    private  ImmutableListMultimap<String,ImpressionRecord> immutableImpressionRecordMultimap;
    private final Multimap<String, Record> recordMultimap;
    private  Multimap<String, ImpressionRecord> impressionRecords;
    private  Multimap<String, ServerRecord> serverRecords;
    private  Multimap<String, ClickRecord> clickRecords;
    private final boolean isFiltered = false;

    public Records(Multimap<String, ImpressionRecord> impressionRecords, Multimap<String,ServerRecord> serverRecords, Multimap<String,ClickRecord> clickRecords) {
        this.recordMultimap = ArrayListMultimap.create();
        this.recordMultimap.putAll(ArrayListMultimap.create(impressionRecords));
        this.recordMultimap.putAll(ArrayListMultimap.create(serverRecords));
        this.recordMultimap.putAll(ArrayListMultimap.create(clickRecords));

        this.impressionRecords = (Multimap<String, ImpressionRecord>) setImpressionRecords();
        this.serverRecords = (Multimap<String, ServerRecord>) setServerRecords();
        this.clickRecords = (Multimap<String, ClickRecord>) setClickRecords();

        immutableRecordMultimap = ImmutableListMultimap.copyOf(recordMultimap);
        immutableServerRecordMultimap = ImmutableListMultimap.copyOf(this.serverRecords);
        immutableClickRecordMultimap = ImmutableListMultimap.copyOf(this.clickRecords);
        immutableImpressionRecordMultimap = ImmutableListMultimap.copyOf(this.impressionRecords);
    }

    public Multimap<String, ? extends Record> getAllRecords() {
        return this.recordMultimap;
    }

    public Map<String,Collection<ImpressionRecord>> getOriginalRecords() {
        return ArrayListMultimap.create(this.impressionRecords).asMap();
    }

    public Map<String, Collection<ImpressionRecord>> getImpressionRecords() {
        if(this.impressionRecords.isEmpty()) setImpressionRecords();
        return ArrayListMultimap.create(this.impressionRecords).asMap();
    }

    public Map<String, Collection<ServerRecord>> getServerRecords() {
        return ArrayListMultimap.create(this.serverRecords).asMap();
    }

    public Map<String, Collection<ClickRecord>> getClickRecords() {
        return ArrayListMultimap.create(this.clickRecords).asMap();
    }

    public Multimap<String, ? super ImpressionRecord> setImpressionRecords() {
        if(recordMultimap.isEmpty()) immutableRecordMultimap.forEach(recordMultimap::put);
        return Multimaps.filterValues(ArrayListMultimap.create(recordMultimap), v -> v instanceof ImpressionRecord);
    }

    public Multimap<String, ? super ServerRecord> setServerRecords() {
        return Multimaps.filterValues(ArrayListMultimap.create(recordMultimap), v -> v instanceof ServerRecord);


    }

    /* TODO: Overriden function for time intervals */
    public Multimap<String, ? super ClickRecord> setClickRecords() {
        return Multimaps.filterValues(ArrayListMultimap.create(recordMultimap), v -> v instanceof ClickRecord);
    }



    public Map<LocalDate,Collection<ClickRecord>> dateToClickRecordsMap() {
        Collection<Collection<ClickRecord>> clickRecords = ArrayListMultimap.create(immutableClickRecordMultimap).asMap().values();
        Map<LocalDate, Collection<ClickRecord>> dateToClickRecords = new ConcurrentHashMap<>(clickRecords.parallelStream().flatMap(Collection::parallelStream).collect(Collectors.groupingByConcurrent(ClickRecord::getLocalDate)));

        return dateToClickRecords;
    }

    public Map<LocalDate,Collection<ImpressionRecord>> dateToImpressionMap() {
        Collection<Collection<ImpressionRecord>> impressionRecords = ArrayListMultimap.create(immutableImpressionRecordMultimap).asMap().values();
        Map<LocalDate, Collection<ImpressionRecord>> dateToImpressionRecordMap = new ConcurrentHashMap<>(impressionRecords.parallelStream().flatMap(Collection::parallelStream).collect(Collectors.groupingByConcurrent(ImpressionRecord::getLocalDate)));

        return dateToImpressionRecordMap;
    }

    public Map<LocalDate,Collection<ServerRecord>> dateToServerRecordMap() {
        Map<LocalDate,Collection<ServerRecord>> dateToServerRecordMap = new ConcurrentHashMap<>();
        Collection<Collection<ServerRecord>> serverRecords = ArrayListMultimap.create(immutableServerRecordMultimap).asMap().values();
        dateToServerRecordMap.putAll(serverRecords.parallelStream().flatMap(Collection::parallelStream).collect(Collectors.groupingByConcurrent(ServerRecord::getEntryLocalDate)));
        return dateToServerRecordMap;
    }

    public Map<LocalDate, Integer> dateToClickCountMap() {
        return Maps.transformValues(dateToClickRecordsMap(), CollectionUtils::size);
    }

    public Map<LocalDate, Integer> dateToImpressionCountMap() {
        return Maps.transformValues(dateToImpressionMap(),CollectionUtils::size);
    }

    public Map<LocalDate, Integer> dateToServerCountMap() {
        return Maps.transformValues(dateToServerRecordMap(),CollectionUtils::size);
    }

    public Map<LocalDate, Float> dateToAdCostMap() {
        return Maps.transformValues(dateToImpressionMap(),
                x -> x.stream()
                        .map(ImpressionRecord::getImpressionCost)
                        .reduce((float) 0.00,Float::sum));
    }

    public Metrics buildMetrics() {
        return new Metrics(this,context);
    }

    //May be filteredMap
    public void update(Map<String,Collection<ImpressionRecord>> filteredMap) {
        //System.out.println("Very start of update method: " + filteredMap.entrySet());
        this.impressionRecords.clear();
        filteredMap.forEach(impressionRecords::putAll);
        //System.out.println("Start of update: " + filteredMap.entrySet());

        Map<String,Collection<Record>> filterAllRec = ArrayListMultimap.create(this.immutableRecordMultimap).asMap();
        filterAllRec = filterAllRec
                .entrySet()
                .stream()
                .filter(x -> filteredMap.containsKey(x.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        //System.out.println("Filter All Records: " + filterAllRec.entrySet());

        Map<String, Collection<ClickRecord>> filteredClickRecords = ArrayListMultimap.create(this.immutableClickRecordMultimap).asMap();
        filteredClickRecords = filteredClickRecords
                .entrySet()
                .stream()
                .filter(x -> filteredMap.containsKey(x.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        //System.out.println("Filter Click Records: " + filteredClickRecords.entrySet());

        Map<String,Collection<ServerRecord>> filteredServerRecords = ArrayListMultimap.create(this.immutableServerRecordMultimap).asMap();
        filteredServerRecords = filteredServerRecords
                .entrySet()
                .stream()
                .filter(x -> filteredMap.containsKey(x.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        //System.out.println("Filter Server Records: " + filteredServerRecords.entrySet());
        //System.out.println("---------------------");

        //System.out.println("Record Multimap: " + recordMultimap.entries());
        //System.out.println("ServerRecord Multimap: " + serverRecords.entries());
        //System.out.println("ClickRecord Multimap: " + clickRecords.entries());

        this.recordMultimap.clear();
        this.serverRecords.clear();
        this.clickRecords.clear();
        //System.out.println("Filtered ClickRecord Multimap: " + filteredClickRecords.entrySet());

        filterAllRec.forEach(this.recordMultimap::putAll); //TODO: Adding duplicates to map
        filteredClickRecords.forEach(this.clickRecords::putAll);
        filteredServerRecords.forEach(this.serverRecords::putAll);

        //System.out.println("------------- AFTER UPDATE -------------");
        //System.out.println("Record Multimap: " + recordMultimap.entries());
        //System.out.println("ServerRecord Multimap: " + serverRecords.entries());
        //System.out.println("ClickRecord Multimap: " + clickRecords.entries());


        //System.out.println("---------------------");
        //System.out.println("---------------------");

        /*recordMultimap.putAll(filteredMap);
        recordMultimap.putAll(clickRecords);
        recordMultimap.putAll(serverRecords);*/
    }
}
