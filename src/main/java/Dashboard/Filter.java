package Dashboard;

import POJOs.ImpressionRecord;
import POJOs.Record;
import com.google.common.collect.Multimap;

public class Filter {

    public Filter() {

    }

    private void impressionsByAge(Multimap<String, ImpressionRecord> records) {
        int impressionsBelow25 = 0;
        int impressions25To34 = 0;
        int impressions35To44 = 0;
        int impressions45To54 = 0;
        int impressionsAbove54 = 0;
        for (Object value : records.values()) {
            String stringValue = value.toString();
            if (stringValue.contains("<25"))
                impressionsBelow25++;
            else if (stringValue.contains("25-34"))
                impressions25To34++;
            else if (stringValue.contains("35-44"))
                impressions35To44++;
            else if (stringValue.contains("45-54"))
                impressions45To54++;
            else if (stringValue.contains(">54"))
                impressionsAbove54++;
        }
    }

    private void impressionsByGender(Multimap<String, Record> records) {
        int impressionsMale = 0;
        for (Record value : records.values()) {
            String stringValue = value.toString();
            if (stringValue.contains("Male"))
                impressionsMale++;
        }
        int impressionsFemale = 0;
        for (Record value : records.values()) {
            String stringValue = value.toString();
            if (stringValue.contains("Female"))
                impressionsFemale++;
        }
    }
}
