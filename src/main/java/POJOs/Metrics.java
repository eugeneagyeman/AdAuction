package POJOs;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static Configuration.Parser.dateDifference;

public class Metrics {
    private Campaign campaign;
    private int numOfImpressions;
    private int numOfClicks;
    private int numOfUniques;
    private int numOfBounces;
    private int numOfConversions;
    private double totalCost;
    private double clickThroughRate;
    private double costPerAction;
    private double costPerClick;
    private double costPerThousand;
    private double bounceRate;
    private Records records;

    private List<String> ageRanges;
    private ArrayList<String> recommendations = new ArrayList<>();

    public Metrics(Records records) {
        this.records = records;
        this.ageRanges = getAgeRanges();
        calculateMetrics();
        calculateRecommendations();
        printMetrics();
    }

    public List getAgeRanges() {

        List<String> ranges = records.getImpressionRecords().values()
                .stream()
                .map(ImpressionRecord::getAge)
                .distinct().sorted().collect(Collectors.toUnmodifiableList());
        return ranges;
    }

    public void calculateRecommendations() {
        recommendations.add("No Recommendations");
    }

    private void calculateMetrics() {
        calculateNumOfImpressions();
        calculateNumOfClicks();
        calculateNumOfUniques();
        calculateNumOfBounces();
        calculateNumOfConversions();
        calculateTotalCost();
        calculateCostPerAction();
        calculateClickThroughRate();
        calculateCostPerClick();
        calculateCostPerThousand();
        calculateBouncerate();
    }

    private void printMetrics() {
        System.out.println("Total Cost of Campaign: " + getTotalCost());
        System.out.println("CPM: " + getCostPerThousand());
        System.out.println("Number of Conversions: " + getNumOfConversions());
        System.out.println("Cost Per Acquisition: " + getCostPerAction());
        System.out.println("Cost Per Click: " + getCostPerClick());
        System.out.println("Number of Bounces: " + getNumOfBounces());
        System.out.println("Bounce Rate: " + getBounceRate());
    }

    public int calculateNumOfImpressions() {
        numOfImpressions = records.getImpressionRecords().size();
        return numOfImpressions;
    }

    public int calculateNumOfClicks() {
        numOfClicks = records.getClickRecords().size();
        return numOfClicks;
    }

    public int calculateNumOfUniques() {
        numOfUniques = records.getAllRecords().keySet().size();
        return numOfUniques;
    }

    public int calculateNumOfBounces() {
        this.numOfBounces = 0;
        Collection<ServerRecord> serverRecords = records.getServerRecords().values();
        // Bounce logic
        serverRecords.forEach(
                record -> {
                    LocalDateTime entryDate = record.getEntryDate();
                    LocalDateTime exitDate = record.getExitDate();
                    boolean converted = record.getConversion();
                    int pagesViewed = record.getPagesViewed();
                    Long time = dateDifference(entryDate, exitDate);
                    if (time != null) {
                        if (converted && pagesViewed <= 1 && time <= 10)
                            this.numOfBounces++;
                    }
                });
        return this.numOfBounces;
    }

    public int calculateNumOfConversions() {
        this.numOfConversions = (int) records.getServerRecords().values()
                .stream()
                .map(ServerRecord::getConversion)
                .filter(converted -> converted)
                .count();
        return numOfConversions;
    }

    public Double calculateTotalCost() {
        this.totalCost = records.getImpressionRecords().values()
                .stream()
                .map(ImpressionRecord::getImpressionCost)
                .reduce((float) 0.00, Float::sum);
        return totalCost;
    }

    public double calculateClickThroughRate() {
        this.clickThroughRate = (getNumOfClicks() / getNumOfImpressions()) * 100;
        return this.clickThroughRate;
    }

    public double calculateCostPerAction() {
        this.costPerAction = totalCost / numOfConversions;
        return this.costPerAction;
    }

    public double calculateCostPerClick() {
        this.costPerClick = totalCost / getNumOfClicks();
        return this.costPerClick;
    }

    public Double calculateCostPerThousand() {
        this.costPerThousand = ((1000 * (totalCost)) / numOfImpressions);
        return this.costPerThousand;
    }

    public float calculateBouncerate() {
        bounceRate = (float) ((numOfBounces / (numOfClicks * 1.0)) * 100.0);

        return (float) (bounceRate * 1.0);
    }

    public int getNumOfImpressions() {
        return numOfImpressions;
    }

    public int getNumOfClicks() {
        return numOfClicks;
    }

    public int getNumOfUniques() {
        return numOfUniques;
    }

    public int getNumOfBounces() {
        return numOfBounces;
    }

    public int getNumOfConversions() {
        return numOfConversions;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public double getClickThroughRate() {
        return clickThroughRate;
    }

    public double getCostPerAction() {
        return costPerAction;
    }

    public double getCostPerClick() {
        return costPerClick;
    }

    public double getCostPerThousand() {
        return costPerThousand;
    }

    public double getBounceRate() {
        return bounceRate;
    }

    public List<String> getRecommendations() {
        return recommendations;
    }
}
