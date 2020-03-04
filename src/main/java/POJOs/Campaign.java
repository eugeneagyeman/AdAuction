package POJOs;

public class Campaign {
    private String campaignID;
    private Records records;
    private Metrics metrics;
    private String bounceDefinition;

    public Campaign(String campaignID, Records records, Metrics metrics, String bounceDefinition) {
        this.campaignID = campaignID;
        this.records = records;
        this.metrics = metrics;
        this.bounceDefinition = bounceDefinition;
    }

    public String getCampaignID() {
        return campaignID;
    }

    public Campaign setCampaignID(String campaignID) {
        this.campaignID = campaignID;
        return this;
    }

    public Records getRecords() {
        return records;
    }

    public Campaign setRecords(Records records) {
        this.records = records;
        return this;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public Campaign setMetrics(Metrics metrics) {
        this.metrics = metrics;
        return this;
    }

    public String getBounceDefinition() {
        return bounceDefinition;
    }

    public Campaign setBounceDefinition(String bounceDefinition) {
        this.bounceDefinition = bounceDefinition;
        return this;
    }
}
