package POJOs;

public class ClickRecord extends Record {
    private String date;
    private String clickCost;


    public ClickRecord(String uniqueID, String date, String clickCost) {
        super(uniqueID);
        this.date = date;
        this.clickCost = clickCost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClickCost() {
        return clickCost;
    }

    public void setClickCost(String clickCost) {
        this.clickCost = clickCost;
    }

    @Override
    public String toString() {
        return "ClickRecord{" +
                "date='" + date + '\'' +
                ", clickCost='" + clickCost + '\'' +
                '}';
    }
}
