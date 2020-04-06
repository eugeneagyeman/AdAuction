package POJOs;

import java.time.LocalDateTime;

import static Configuration.Parser.parseDateTime;

public class ServerRecord extends Record {


    private LocalDateTime entryDate;
    private LocalDateTime exitDate;
    private int pagesViewed;
    private Boolean conversion;

    public ServerRecord(String uniqueID, String entryDate, String exitDate, String pagesViewed, String conversion) {
        super(uniqueID);
        this.entryDate = parseDateTime(entryDate);
        this.exitDate = parseDateTime(exitDate);
        this.pagesViewed = Integer.parseInt(pagesViewed.replaceAll("\\s", ""));
        this.conversion = setConversion(conversion);
    }

    public LocalDateTime getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = parseDateTime(entryDate);
    }

    public LocalDateTime getExitDate() {
        if (exitDate == null) return LocalDateTime.MAX;
        return exitDate;
    }

    public void setExitDate(String exitDate) {
        parseDateTime(exitDate);
    }

    public int getPagesViewed() {
        return pagesViewed;
    }

    public void setPagesViewed(int pagesViewed) {
        this.pagesViewed = pagesViewed;
    }

    public Boolean getConversion() {
        return conversion;
    }

    public boolean setConversion(String conversion) {
        if (conversion.equalsIgnoreCase("Yes")) return true;
        else return false;
    }

    @Override
    public String toString() {
        return "ServerRecord{" +
                "entryDate='" + entryDate + '\'' +
                ", exitDate='" + exitDate + '\'' +
                '}';
    }
}
