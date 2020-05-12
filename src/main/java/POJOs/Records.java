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
    private  ImmutableListMultimap immutableRecordMultimap;
    private Multimap<String, ? extends Record> recordMultimap;
    private  Multimap<String, ImpressionRecord> impressionRecords;
    private  Multimap<String, ServerRecord> serverRecords;
    private  Multimap<String, ClickRecord> clickRecords;
    private final boolean isFiltered = false;

    public Records(Multimap impressionRecords, Multimap serverRecords, Multimap clickRecords) {
        recordMultimap = ArrayListMultimap.create();
        recordMultimap.putAll(impressionRecords);
        recordMultimap.putAll(serverRecords);
        recordMultimap.putAll(clickRecords);


        immutableRecordMultimap = ImmutableListMultimap
                .builder()
                .putAll(recordMultimap)
                .build();

        this.impressionRecords = (Multimap<String, ImpressionRecord>) setImpressionRecords();
        this.serverRecords = (Multimap<String, ServerRecord>) setServerRecords();
        this.clickRecords = (Multimap<String, ClickRecord>) setClickRecords();

    }

    public Multimap<String, ? extends Record> getAllRecords() {
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

    public Multimap<String, ? extends Record> setImpressionRecords() {

        return Multimaps.filterValues(recordMultimap, v -> v instanceof ImpressionRecord);


    }

    public Multimap<String, ? extends Record> setServerRecords() {
        return Multimaps.filterValues(recordMultimap, v -> v instanceof ServerRecord);


    }

    /* TODO: Overriden function for time intervals */
    public Multimap<String, ? extends Record> setClickRecords() {

        return Multimaps.filterValues(recordMultimap, v -> v instanceof ClickRecord);
    }

    public Multimap setClickRecords(Multimap multimap) {
        return Multimaps.filterValues(multimap, v -> v instanceof ClickRecord);
    }

    public Multimap setServerRecords(Multimap multimap) {
        return Multimaps.filterValues(multimap, v -> v instanceof ServerRecord);

    }

    public Multimap setImpressionRecords(Multimap multimap) {
        return Multimaps.filterValues(multimap, v -> v instanceof ImpressionRecord);
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

    public void update(Multimap filteredMap) {
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

        recordMultimap = Multimaps.newMultimap(filterAllRec,ArrayList::new);
        Multimap clicks =  Multimaps.newMultimap(filteredClickRecords,ArrayList::new);
        Multimap servers = serverRecords  = Multimaps.newMultimap(filteredServerRecords,ArrayList::new);

        recordMultimap.putAll(filteredMap);
        recordMultimap.putAll(clicks);
        recordMultimap.putAll(servers);


    }
}
