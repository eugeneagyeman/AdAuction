package POJOs;

import com.google.common.collect.*;
import org.apache.commons.collections.CollectionUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

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

    public Map<LocalDate,Collection<ClickRecord>> dateToClickRecordsMap() {
        Map<LocalDate, Collection<ClickRecord>> dateToClickRecords = new ConcurrentHashMap<>();
        Collection<ClickRecord> clickRecords = getClickRecords().values();
        dateToClickRecords.putAll(clickRecords.parallelStream().collect(groupingByConcurrent(ClickRecord::getLocalDate)));

        return dateToClickRecords;
    }

    public Map<LocalDate,Collection<ImpressionRecord>> dateToImpressionMap() {
        Map<LocalDate, Collection<ImpressionRecord>> dateToImpressionRecordMap = new ConcurrentHashMap<>();
        Collection<ImpressionRecord> impressionRecords = getImpressionRecords().values();
        dateToImpressionRecordMap.putAll(impressionRecords.parallelStream().collect(groupingByConcurrent(ImpressionRecord::getLocalDate)));

        return dateToImpressionRecordMap;
    }

    public Map<LocalDate,Collection<ServerRecord>> dateToServerRecordMap() {
        Map<LocalDate,Collection<ServerRecord>> dateToServerRecordMap = new ConcurrentHashMap<>();
        Collection<ServerRecord> serverRecords = getServerRecords().values();
        dateToServerRecordMap.putAll(serverRecords.parallelStream().collect(groupingByConcurrent(ServerRecord::getEntryLocalDate)));
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

}
