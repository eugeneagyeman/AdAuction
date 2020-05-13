package dashboard;

import POJOs.ImpressionRecord;
import POJOs.Records;
import com.google.common.collect.*;

import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Filter {
    private Records records;

    public Filter(DashboardModel model) {
        this.records = model.getCurrentCampaign().getRecords();
    }

    public Filter(Records records) {
        this.records = records;
    }

    public Map<String, Collection<ImpressionRecord>> impressionsAgeFilter(String ageRange, Map<String, Collection<ImpressionRecord>> filteredRecordMap) {

        switch (ageRange) {
            case "<25":
                return getImpressionFilteredByAgeMap("<25", records.getImpressionRecords());
            case "25-34":
                 return getImpressionFilteredByAgeMap("25-34", records.getImpressionRecords());
            case "35-44":
                return getImpressionFilteredByAgeMap("35-44", records.getImpressionRecords());
            case "45-54":
                return getImpressionFilteredByAgeMap("45-54", records.getImpressionRecords());
            case ">54":
                return getImpressionFilteredByAgeMap(">54", records.getImpressionRecords());
            default:
                //TODO: Exception Thrown here to say cannot filter
        }
        return filteredRecordMap;

    }

    public Multimap<String, ImpressionRecord> impressionsIncomeFilter(String incomeLevel, Multimap<String, ImpressionRecord> filteredRecordMap) {
        Set<String> setOfFilteredUsers;

        setOfFilteredUsers = getImpressionsByIncomeMap(incomeLevel, filteredRecordMap).keySet();
        filteredRecordMap.keySet().retainAll(setOfFilteredUsers);
        return filteredRecordMap;
    }

    public Multimap<String, ImpressionRecord> impressionsGenderFilter(String gender, Multimap<String, ImpressionRecord> filteredRecordMap) {
        Set<String> setOfFilteredUsers;

        if (gender.equalsIgnoreCase("male")) {
            setOfFilteredUsers = getImpressionByGenderMap("male", filteredRecordMap).keySet();
            filteredRecordMap.keySet().retainAll(setOfFilteredUsers);
        }
        if (gender.equalsIgnoreCase("female")) {
            setOfFilteredUsers = getImpressionByGenderMap("female", filteredRecordMap).keySet();
            filteredRecordMap.keySet().retainAll(setOfFilteredUsers);
        }
        return filteredRecordMap;
    }

    //TODO: Implement Multimap as a Parameter
    public Multimap<String, ImpressionRecord> contextFilter(String context, Multimap<String, ImpressionRecord> filteredRecordMap) {
        Set<String> setOfFilteredUsers;

        setOfFilteredUsers = getImpressionsByContextMap(context, filteredRecordMap).keySet();
        filteredRecordMap.keySet().retainAll(setOfFilteredUsers);
        return filteredRecordMap;
    }

    //TODO: Implement Multimap as a Parameter
    public Multimap<String, ImpressionRecord> dateFilter(LocalDate startDate, LocalDate endDate) {
        Map<String,Collection<ImpressionRecord>> recordMap = records.getImpressionRecords();
        Multimap<String, ImpressionRecord> filteredDateMap = ArrayListMultimap.create();

        recordMap.forEach((id, recs) -> recs.parallelStream()
                .filter(r -> r.dateInBetween(startDate, endDate))
                .forEach(r -> filteredDateMap.put(id, r)));

        return filteredDateMap;
    }

    //NEED TO LOOK AT MASTER COPY
    private Map<String,Collection<ImpressionRecord> > getImpressionFilteredByAgeMap(String ageRange, Map<String, Collection<ImpressionRecord>> impressionRecords) {
        Map<String, Collection<ImpressionRecord>> impression = records.getImpressionRecords();
        impression = impression.entrySet()
                  .stream()
                  .filter(entry -> entry.getValue()
                                        .stream()
                                        .anyMatch(l -> l.getAge().equalsIgnoreCase(ageRange)))
                  .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        ListMultimap lm = ArrayListMultimap.create();
        impression.forEach(lm::putAll);
        return impression;
    }

    private Multimap<String, ImpressionRecord> getImpressionByGenderMap(String gender, Multimap<String, ImpressionRecord> impressionRecords) {
        Map<String, Collection<ImpressionRecord>> impression = impressionRecords.asMap();
        impression = impression.entrySet()
                .stream()
                .filter(entry -> entry.getValue()
                        .stream()
                        .anyMatch(l -> l.getGender().equalsIgnoreCase(gender)))
                .collect(Collectors.toMap(e -> e.getKey(),e -> e.getValue()));

        ListMultimap lm = ArrayListMultimap.create();
        impression.forEach(lm::putAll);
        return lm;    }

    private Multimap<String, ImpressionRecord> getImpressionsByIncomeMap(String incomeLevel, Multimap<String, ImpressionRecord> impressionRecords) {
        Map<String, Collection<ImpressionRecord>> impression = impressionRecords.asMap();
        impression = impression.entrySet()
                .stream()
                .filter(entry -> entry.getValue()
                        .stream()
                        .anyMatch(l -> l.getAge().equalsIgnoreCase(incomeLevel)))
                .collect(Collectors.toMap(e -> e.getKey(),e -> e.getValue()));

        ListMultimap lm = ArrayListMultimap.create();
        impression.forEach(lm::putAll);
        return lm;    }

    private Multimap<String, ImpressionRecord> getImpressionsByContextMap(String context, Multimap<String, ImpressionRecord> impressionRecords) {
        Map<String, Collection<ImpressionRecord>> impression = impressionRecords.asMap();
        impression = impression.entrySet()
                .stream()
                .filter(entry -> entry.getValue()
                        .stream()
                        .anyMatch(l -> l.getAge().equalsIgnoreCase(context)))
                .collect(Collectors.toMap(e -> e.getKey(),e -> e.getValue()));

        ListMultimap lm = ArrayListMultimap.create();
        impression.forEach(lm::putAll);
        return lm;    }

}
