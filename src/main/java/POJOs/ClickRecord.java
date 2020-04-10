package POJOs;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static Configuration.Parser.parseDateTime;

public class ClickRecord extends Record {
    private LocalDateTime date;
    private String clickCost;


    public ClickRecord(String uniqueID, String date, String clickCost) {
        super(uniqueID);
        this.date = parseDateTime(date);
        this.clickCost = clickCost;
    }

    public String getDate() {
        return date.toString();
    }

    public void setDate(String date) {
        this.date = parseDateTime(date);
    }

    public String getClickCost() {
        return clickCost;
    }

    public void setClickCost(String clickCost) {
        this.clickCost = clickCost;
    }

    @Override
    public String toString() {
        return String.format("ClickRecord{date='%s', clickCost='%s'}", date, clickCost);
    }

    @Override
    public Boolean dateInBetween(LocalDate start, LocalDate end) {
        LocalDate date = this.date.toLocalDate();
        if (date.isEqual(start) || date.isEqual(end)) return true;
        return date.isAfter(start) && date.isBefore(end);
    }
}
