package POJOs;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static Configuration.Parser.parseDateTime;

public class ImpressionRecord extends Record {

    private LocalDateTime date;
    private String gender;
    private String age;
    private String income;
    private String context;
    private Float impressionCost;

    public ImpressionRecord(String uniqueID, String date, String gender, String age, String income, String context, String impressionCost) {
        super(uniqueID);
        this.date = parseDateTime(date);
        this.gender = gender;
        this.age = age;
        this.income = income;
        this.context = context;
        this.impressionCost = Float.valueOf(impressionCost);
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = parseDateTime(date);
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Float getImpressionCost() {
        return impressionCost;
    }

    public void setImpressionCost(Float impressionCost) {
        this.impressionCost = impressionCost;
    }

    @Override
    public String toString() {
        return String.format("ImpressionRecord{date='%s', gender='%s', age='%s', income='%s', context='%s', impressionCost='%s'}", date.toString(), gender, age, income, context, impressionCost);
    }

    @Override
    public Boolean dateInBetween(LocalDate start, LocalDate end) {
        LocalDate date = this.date.toLocalDate();
        if (date.isEqual(start) || date.isEqual(end)) return true;
        return date.isAfter(start) && date.isBefore(end);
    }
}
