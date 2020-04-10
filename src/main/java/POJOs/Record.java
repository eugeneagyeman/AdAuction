package POJOs;

import java.time.LocalDate;

public abstract class Record {
    private String id;

    public Record(String uniqueID) {
        this.id = uniqueID;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public abstract Boolean dateInBetween(LocalDate start, LocalDate end);
}
