package Dashboard;

import POJOs.ImpressionRecord;
import POJOs.Metrics;
import POJOs.Record;
import POJOs.Records;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import java.time.LocalDate;
import java.util.Set;

public class Filter {
    private DashboardModel model;
    private Records records;

    public Filter(DashboardModel model) {
        this.model = model;
    }

    public Metrics calculateMetrics(Multimap<String, Record> filteredMap) {
        return new Metrics(new Records(filteredMap));
    }

    public Multimap<String, Record> impressionsAgeFilter(String ageRange) {
        records = model.getCurrentCampaign().getRecords();
        Multimap<String, Record> filteredRecordMap = records.getAllRecords();
        Set<String> setOfFilteredUsers;

        switch (ageRange) {
            case "<25":
                setOfFilteredUsers = getImpressionFilteredByAgeMap("<25", records.getImpressionRecords()).keySet();
                filteredRecordMap.keySet().retainAll(setOfFilteredUsers);
                break;
            case "25-34":
                setOfFilteredUsers = getImpressionFilteredByAgeMap("25-34", records.getImpressionRecords()).keySet();
                filteredRecordMap.keySet().retainAll(setOfFilteredUsers);
                break;
            case "35-44":
                setOfFilteredUsers = getImpressionFilteredByAgeMap("35-44", records.getImpressionRecords()).keySet();
                filteredRecordMap.keySet().retainAll(setOfFilteredUsers);
                break;
            case "45-54":
                setOfFilteredUsers = getImpressionFilteredByAgeMap("45-54", records.getImpressionRecords()).keySet();
                filteredRecordMap.keySet().retainAll(setOfFilteredUsers);
                break;
            case ">54":
                setOfFilteredUsers = getImpressionFilteredByAgeMap(">54", records.getImpressionRecords()).keySet();
                filteredRecordMap.keySet().retainAll(setOfFilteredUsers);
                break;
            default:
                //TODO: Exception Thrown here to say cannot filter
        }
        return filteredRecordMap;

    }

    public Multimap<String, Record> contextFilter(String context) {
        Set<String> setOfFilteredUsers;

        records = model.getCurrentCampaign().getRecords();
        Multimap<String, Record> filteredRecordMap = records.getAllRecords();

        setOfFilteredUsers = getImpressionsByContextMap(context, records.getImpressionRecords()).keySet();
        filteredRecordMap.keySet().retainAll(setOfFilteredUsers);

        return filteredRecordMap;

    }

    public Multimap<String, Record> impressionsIncomeFilter(String incomeLevel) {
        Records rec = model.getCurrentCampaign().getRecords();
        Set<String> setOfFilteredUsers;
        Multimap<String, Record> filteredRecordMap = records.getAllRecords();
        setOfFilteredUsers = getImpressionsByIncomeMap(incomeLevel, rec.getImpressionRecords()).keySet();
        filteredRecordMap.keySet().retainAll(setOfFilteredUsers);
        return filteredRecordMap;
    }

    public Multimap<String, Record> impressionsGenderFilter(String gender) {
        Records rec = model.getCurrentCampaign().getRecords();
        Multimap<String, Record> filteredRecordMap = records.getAllRecords();
        Set<String> setOfFilteredUsers;
        if (gender.equalsIgnoreCase("male")) {
            setOfFilteredUsers = getImpressionByGenderMap("male", rec.getImpressionRecords()).keySet();
            filteredRecordMap.keySet().retainAll(setOfFilteredUsers);
        }
        if (gender.equalsIgnoreCase("female")) {
            setOfFilteredUsers = getImpressionByGenderMap("female", rec.getImpressionRecords()).keySet();
            filteredRecordMap.keySet().retainAll(setOfFilteredUsers);
        }
        return filteredRecordMap;

    }

    private Multimap<String, Record> dateFilter(LocalDate startDate, LocalDate endDate) {
        Records rec = model.getCurrentCampaign().getRecords();
        Multimap<String, Record> allRecords = records.getAllRecords();
        Multimap<String, Record> filteredDateMap = ArrayListMultimap.create();

        allRecords.asMap().forEach((id, records) -> records.parallelStream()
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
