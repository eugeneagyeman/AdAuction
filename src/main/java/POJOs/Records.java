package POJOs;

import com.google.common.collect.*;
import jdk.jfr.consumer.RecordedClass;
import metrics.Metrics;
import org.apache.commons.collections.CollectionUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Records {
    private String context;
    private  ImmutableListMultimap<String,Record> immutableRecordMultimap;
    private  Multimap<String, Record> recordMultimap;
    private  Multimap<String, ImpressionRecord> impressionRecords;
    private  Multimap<String, ServerRecord> serverRecords;
    private  Multimap<String, ClickRecord> clickRecords;
    private final boolean isFiltered = false;

    public Records(Multimap<String, ImpressionRecord> impressionRecords, Multimap<String,ServerRecord> serverRecords, Multimap<String,ClickRecord> clickRecords) {
        this.recordMultimap = ArrayListMultimap.create();
        this.recordMultimap.putAll(impressionRecords);
        this.recordMultimap.putAll(serverRecords);
        this.recordMultimap.putAll(clickRecords);

        immutableRecordMultimap = ImmutableListMultimap.copyOf(recordMultimap);

        this.impressionRecords = (Multimap<String, ImpressionRecord>) setImpressionRecords();
        this.serverRecords = (Multimap<String, ServerRecord>) setServerRecords();
        this.clickRecords = (Multimap<String, ClickRecord>) setClickRecords();
    }

    public Multimap<String, ? extends Record> getAllRecords() {
        return this.recordMultimap;
    }

    public Multimap<String, ImpressionRecord> getImpressionRecords() {
        return this.impressionRecords;
    }

    public Multimap<String, ServerRecord> getServerRecords() {
        return this.serverRecords;
    }

    public Multimap<String, ClickRecord> getClickRecords() {
        return this.clickRecords;
    }

    public Multimap<String, ? super ImpressionRecord> setImpressionRecords() {
        return Multimaps.filterValues(recordMultimap, v -> v instanceof ImpressionRecord);
    }

    public Multimap<String, ? super ServerRecord> setServerRecords() {
        return Multimaps.filterValues(recordMultimap, v -> v instanceof ServerRecord);


    }

    /* TODO: Overriden function for time intervals */
    public Multimap<String, ? super ClickRecord> setClickRecords() {
        return Multimaps.filterValues(recordMultimap, v -> v instanceof ClickRecord);
    }



    public Map<LocalDate,Collection<ClickRecord>> dateToClickRecordsMap() {
        Map<LocalDate, Collection<ClickRecord>> dateToClickRecords = new ConcurrentHashMap<>();
        Collection<ClickRecord> clickRecords = getClickRecords().values();
        dateToClickRecords.putAll(clickRecords.parallelStream().collect(Collectors.groupingByConcurrent(ClickRecord::getLocalDate)));

        return dateToClickRecords;
    }

    public Map<LocalDate,Collection<ImpressionRecord>> dateToImpressionMap() {
        Map<LocalDate, Collection<ImpressionRecord>> dateToImpressionRecordMap = new ConcurrentHashMap<>();
        Collection<ImpressionRecord> impressionRecords = getImpressionRecords().values();
        dateToImpressionRecordMap.putAll(impressionRecords.parallelStream().collect(Collectors.groupingByConcurrent(ImpressionRecord::getLocalDate)));

        return dateToImpressionRecordMap;
    }

    public Map<LocalDate,Collection<ServerRecord>> dateToServerRecordMap() {
        Map<LocalDate,Collection<ServerRecord>> dateToServerRecordMap = new ConcurrentHashMap<>();
        Collection<ServerRecord> serverRecords = getServerRecords().values();
        dateToServerRecordMap.putAll(serverRecords.parallelStream().collect(Collectors.groupingByConcurrent(ServerRecord::getEntryLocalDate)));
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
    public void update(Multimap<String,ImpressionRecord> filteredMap) {
        impressionRecords.clear();
        impressionRecords.putAll(filteredMap);

        Map<String,Collection<Record>> filterAllRec = immutableRecordMultimap.asMap();
        filterAllRec = filterAllRec
                .entrySet()
                .stream()
                .filter(x -> filteredMap.keySet().contains(x.getKey()))
                .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));

        Map<String, Collection<ClickRecord>> filteredClickRecords = clickRecords.asMap();
        filteredClickRecords = filteredClickRecords
                .entrySet()
                .stream()
                .filter(x -> filteredMap.keySet().contains(x.getKey()))
                .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));

        Map<String,Collection<ServerRecord>> filteredServerRecords = serverRecords.asMap();
        filteredServerRecords = filteredServerRecords
                .entrySet()
                .stream()
                .filter(x -> filteredMap.keySet().contains(x.getKey()))
                .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));

        recordMultimap.clear();
        serverRecords.clear();
        clickRecords.clear();

        filterAllRec.forEach(recordMultimap::putAll);
        filteredClickRecords.forEach(clickRecords::putAll);
        filteredServerRecords.forEach(serverRecords::putAll);

        /*recordMultimap.putAll(filteredMap);
        recordMultimap.putAll(clickRecords);
        recordMultimap.putAll(serverRecords);*/


    }
}
