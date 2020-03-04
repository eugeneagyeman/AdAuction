package POJOs;

public class ServerRecord extends Record {


    private String entryDate;
    private String exitDate;
    private String pagesViewed;
    private String conversion;

    public ServerRecord(String uniqueID, String entryDate, String exitDate, String pagesViewed, String conversion) {
        super(uniqueID);
        this.entryDate = entryDate;
        this.exitDate = exitDate;
        this.pagesViewed = pagesViewed;
        this.conversion = conversion;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getExitDate() {
        return exitDate;
    }

    public void setExitDate(String exitDate) {
        this.exitDate = exitDate;
    }

    public String getPagesViewed() {
        return pagesViewed;
    }

    public void setPagesViewed(String pagesViewed) {
        this.pagesViewed = pagesViewed;
    }

    public String getConversion() {
        return conversion;
    }

    public void setConversion(String conversion) {
        this.conversion = conversion;
    }

    @Override
    public String toString() {
        return "ServerRecord{" +
                "entryDate='" + entryDate + '\'' +
                ", exitDate='" + exitDate + '\'' +
                '}';
    }
}
