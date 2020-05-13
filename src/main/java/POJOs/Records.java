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
    private final Multimap<String, Record> recordMultimap;
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

    public Map<String, Collection<ImpressionRecord>> getImpressionRecords() {
        if(this.impressionRecords.isEmpty()) setImpressionRecords();
        return this.impressionRecords.asMap();
    }

    public Map<String, Collection<ServerRecord>> getServerRecords() {
        return this.serverRecords.asMap();
    }

    public Map<String, Collection<ClickRecord>> getClickRecords() {
        return this.clickRecords.asMap();
    }

    public Multimap<String, ? super ImpressionRecord> setImpressionRecords() {
        if(recordMultimap.isEmpty()) immutableRecordMultimap.forEach(recordMultimap::put);
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
        Collection<Collection<ClickRecord>> clickRecords = getClickRecords().values();
        Map<LocalDate, Collection<ClickRecord>> dateToClickRecords = new ConcurrentHashMap<>(clickRecords.parallelStream().flatMap(Collection::parallelStream).collect(Collectors.groupingByConcurrent(ClickRecord::getLocalDate)));

        return dateToClickRecords;
    }

    public Map<LocalDate,Collection<ImpressionRecord>> dateToImpressionMap() {
        Collection<Collection<ImpressionRecord>> impressionRecords = getImpressionRecords().values();
        Map<LocalDate, Collection<ImpressionRecord>> dateToImpressionRecordMap = new ConcurrentHashMap<>(impressionRecords.parallelStream().flatMap(Collection::parallelStream).collect(Collectors.groupingByConcurrent(ImpressionRecord::getLocalDate)));

        return dateToImpressionRecordMap;
    }

    public Map<LocalDate,Collection<ServerRecord>> dateToServerRecordMap() {
        Map<LocalDate,Collection<ServerRecord>> dateToServerRecordMap = new ConcurrentHashMap<>();
        Collection<Collection<ServerRecord>> serverRecords = getServerRecords().values();
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
        this.impressionRecords.clear();
        filteredMap.forEach(impressionRecords::putAll);

        Map<String,Collection<Record>> filterAllRec = this.immutableRecordMultimap.asMap();
        filterAllRec = filterAllRec
                .entrySet()
                .stream()
                .filter(x -> filteredMap.containsKey(x.getKey()))
                .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));

        Map<String, Collection<ClickRecord>> filteredClickRecords = this.clickRecords.asMap();
        filteredClickRecords = filteredClickRecords
                .entrySet()
                .stream()
                .filter(x -> filteredMap.keySet().contains(x.getKey()))
                .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));

        Map<String,Collection<ServerRecord>> filteredServerRecords = this.serverRecords.asMap();
        filteredServerRecords = filteredServerRecords
                .entrySet()
                .stream()
                .filter(x -> filteredMap.keySet().contains(x.getKey()))
                .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));

        this.recordMultimap.clear();
        this.serverRecords.clear();
        this.clickRecords.clear();

        filterAllRec.forEach(this.recordMultimap::putAll);
        filteredClickRecords.forEach(this.clickRecords::putAll);
        filteredServerRecords.forEach(this.serverRecords::putAll);

        /*recordMultimap.putAll(filteredMap);
        recordMultimap.putAll(clickRecords);
        recordMultimap.putAll(serverRecords);*/

    }
}
