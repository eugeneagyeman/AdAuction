package dashboard;

import POJOs.ImpressionRecord;
import POJOs.Records;
import com.google.common.collect.*;

import java.time.LocalDate;
import java.util.*;
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
                return getImpressionsByAgeMap("<25", filteredRecordMap);
            case "25-34":
                return getImpressionsByAgeMap("25-34", filteredRecordMap);
            case "35-44":
                return getImpressionsByAgeMap("35-44", filteredRecordMap);
            case "45-54":
                return getImpressionsByAgeMap("45-54", filteredRecordMap);
            case ">54":
                return getImpressionsByAgeMap(">54", filteredRecordMap);
            default:
                //TODO: Exception Thrown here to say cannot filter
        }
        return filteredRecordMap;
    }

    public Map<String, Collection<ImpressionRecord>> impressionsIncomeFilter(String incomeLevel, Map<String, Collection<ImpressionRecord>> filteredRecordMap) {
        switch (incomeLevel) {
            case "low":
                return getImpressionsByIncomeMap("low", filteredRecordMap);
            case "medium":
                return getImpressionsByIncomeMap("medium", filteredRecordMap);
            case "high":
                return getImpressionsByIncomeMap("high", filteredRecordMap);
            default:
                //TODO: Exception Thrown here to say cannot filter
        }
        return filteredRecordMap;
    }

    public Map<String, Collection<ImpressionRecord>> impressionsGenderFilter(String gender, Map<String, Collection<ImpressionRecord>> filteredRecordMap) {
        switch (gender) {
            case "male":
                return getImpressionsByGenderMap("male", filteredRecordMap);
            case "female":
                return getImpressionsByGenderMap("female", filteredRecordMap);
            default:
                //TODO: Exception Thrown here to say cannot filter
        }
        return filteredRecordMap;
    }

    //TODO: Implement Multimap as a Parameter
    public Map<String, Collection<ImpressionRecord>> contextFilter(String context, Map<String, Collection<ImpressionRecord>> filteredRecordMap) {
        switch (context) {
            case "news":
                return getImpressionsByContextMap("news", filteredRecordMap);
            case "shopping":
                return getImpressionsByContextMap("shopping", filteredRecordMap);
            case "socialmedia":
                return getImpressionsByContextMap("socialmedia", filteredRecordMap);
            case "blog":
                return getImpressionsByContextMap("blog", filteredRecordMap);
            case "hobbies":
                return getImpressionsByContextMap("hobbies", filteredRecordMap);
            case "travel":
                return getImpressionsByContextMap("travel", filteredRecordMap);
            default:
                //TODO: Exception Thrown here to say cannot filter
        }
        return filteredRecordMap;
    }

    //TODO: Implement Multimap as a Parameter
    public Map<String,Collection<ImpressionRecord>> dateFilter(LocalDate startDate, LocalDate endDate) {
        Map<String,Collection<ImpressionRecord>> recordMap = records.getOriginalRecords();
        Map<String,Collection<ImpressionRecord>> filteredDateMap = new HashMap<>();

        recordMap.forEach((id, recs) -> recs.parallelStream()
                .filter(r -> r.dateInBetween(startDate, endDate))
                .forEach(r -> filteredDateMap.put(id, Collections.singleton(r))));

        return filteredDateMap;
    }

    //NEED TO LOOK AT MASTER COPY
    private Map<String,Collection<ImpressionRecord>> getImpressionsByAgeMap(String ageRange, Map<String, Collection<ImpressionRecord>> impressionRecords) {
        Map<String, Collection<ImpressionRecord>> impression = impressionRecords;
        impression = impression.entrySet()
                  .stream()
                  .filter(entry -> entry.getValue()
                                        .stream()
                                        .anyMatch(l -> l.getAge().equalsIgnoreCase(ageRange)))
                  .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return impression;
    }

    private Map<String,Collection<ImpressionRecord>> getImpressionsByGenderMap(String gender, Map<String, Collection<ImpressionRecord>> impressionRecords) {
        Map<String, Collection<ImpressionRecord>> impression = impressionRecords;
        impression = impression.entrySet()
                .stream()
                .filter(entry -> entry.getValue()
                        .stream()
                        .anyMatch(l -> l.getGender().equalsIgnoreCase(gender)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return impression;
    }

    private Map<String,Collection<ImpressionRecord>> getImpressionsByIncomeMap(String incomeLevel, Map<String, Collection<ImpressionRecord>> impressionRecords) {
        Map<String, Collection<ImpressionRecord>> impression = impressionRecords;
        impression = impression.entrySet()
                .stream()
                .filter(entry -> entry.getValue()
                        .stream()
                        .anyMatch(l -> l.getIncome().equalsIgnoreCase(incomeLevel)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return impression;
    }

    private Map<String,Collection<ImpressionRecord>> getImpressionsByContextMap(String context, Map<String, Collection<ImpressionRecord>> impressionRecords) {
        Map<String, Collection<ImpressionRecord>> impression = impressionRecords;
        impression = impression.entrySet()
                .stream()
                .filter(entry -> entry.getValue()
                        .stream()
                        .anyMatch(l -> l.getContext().equalsIgnoreCase(context)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return impression;
    }

}
