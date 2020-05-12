package dashboard;

import POJOs.ImpressionRecord;
import POJOs.Records;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import java.time.LocalDate;
import java.util.Set;

public class Filter {
    private Records records;

    public Filter(DashboardModel model) {
        this.records = model.getCurrentCampaign().getRecords();
    }

    public Filter(Records records) {
        this.records = records;
    }

    public Multimap<String, ImpressionRecord> impressionsAgeFilter(String ageRange, Multimap<String, ImpressionRecord> filteredRecordMap) {
        Set<String> setOfFilteredUsers;

        switch (ageRange) {
            case "<25":
                setOfFilteredUsers = getImpressionFilteredByAgeMap("<25", filteredRecordMap).keySet();
                filteredRecordMap.keySet().retainAll(setOfFilteredUsers);
                break;
            case "25-34":
                setOfFilteredUsers = getImpressionFilteredByAgeMap("25-34", filteredRecordMap).keySet();
                filteredRecordMap.keySet().retainAll(setOfFilteredUsers);
                break;
            case "35-44":
                setOfFilteredUsers = getImpressionFilteredByAgeMap("35-44", filteredRecordMap).keySet();
                filteredRecordMap.keySet().retainAll(setOfFilteredUsers);
                break;
            case "45-54":
                setOfFilteredUsers = getImpressionFilteredByAgeMap("45-54", filteredRecordMap).keySet();
                filteredRecordMap.keySet().retainAll(setOfFilteredUsers);
                break;
            case ">54":
                setOfFilteredUsers = getImpressionFilteredByAgeMap(">54", filteredRecordMap).keySet();
                filteredRecordMap.keySet().retainAll(setOfFilteredUsers);
                break;
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
        Multimap<String, ImpressionRecord> recordMap = records.getImpressionRecords();
        Multimap<String, ImpressionRecord> filteredDateMap = ArrayListMultimap.create();

        recordMap.asMap().forEach((id, recs) -> recs.parallelStream()
                .filter(r -> r.dateInBetween(startDate, endDate))
                .forEach(r -> filteredDateMap.put(id, r)));

        return filteredDateMap;
    }

    private Multimap<String, ImpressionRecord> getImpressionFilteredByAgeMap(String ageRange, Multimap<String, ImpressionRecord> impressionRecords) {
        return Multimaps.filterValues(impressionRecords,
                impressionRecord -> impressionRecord != null && impressionRecord.getAge()
                        .equalsIgnoreCase(ageRange));
    }

    private Multimap<String, ImpressionRecord> getImpressionByGenderMap(String gender, Multimap<String, ImpressionRecord> impressionRecords) {
        return Multimaps.filterValues(impressionRecords, impressionRecord -> impressionRecord != null && impressionRecord.getGender().equalsIgnoreCase(gender));
    }

    private Multimap<String, ImpressionRecord> getImpressionsByIncomeMap(String incomeLevel, Multimap<String, ImpressionRecord> impressionRecords) {
        return Multimaps.filterValues(impressionRecords, impressionRecord -> impressionRecord != null && impressionRecord.getIncome().equalsIgnoreCase(incomeLevel));
    }

    private Multimap<String, ImpressionRecord> getImpressionsByContextMap(String context, Multimap<String, ImpressionRecord> impressionRecords) {
        return Multimaps.filterValues(impressionRecords, impressionRecord -> impressionRecord != null && impressionRecord.getContext().equalsIgnoreCase(context));
    }

}
