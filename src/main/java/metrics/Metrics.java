package metrics;

import POJOs.*;
import dashboard.DateRange;
import javafx.scene.chart.BarChart;
import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.util.Precision;

import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static configuration.Parser.dateDifference;
import static gui.charts.ChartBuilder.buildHistogramChart;

public class Metrics {
    private Campaign campaign;
    private int numOfImpressions;
    private int numOfClicks;
    private int numOfUniques;
    private int numOfBounces;
    private int numOfConversions;
    private float totalCost;
    private float clickThroughRate;
    private float costPerAction;
    private float costPerClick;
    private float costPerThousand;
    private float bounceRate;
    private Records records;
    private final ChartMetrics chartMetrics;

    private List<String> ageRanges;
    private ArrayList<String> recommendations = new ArrayList<>();

    public Metrics(Records records) {
        this.records = records;
        this.ageRanges = getAgeRanges();
        calculateMetrics();
        calculateRecommendations();
        printMetrics();
        chartMetrics = new ChartMetrics();
    }

    public List<String> getAgeRanges() {

        return records.getImpressionRecords().values()
                .stream()
                .map(ImpressionRecord::getAge)
                .distinct().sorted().collect(Collectors.toUnmodifiableList());
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
        System.out.println("Size of click costs: " + getListOfClickCosts().size());
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
        numOfBounces = 0;
        Collection<ServerRecord> serverRecords = records.getServerRecords().values();
        // Bounce logic
        serverRecords.forEach(
                record -> {
                    LocalDateTime entryDate = record.getEntryDate();
                    LocalDateTime exitDate = record.getExitDate();
                    boolean converted = record.getConversion();

                    int pagesViewed = record.getPagesViewed();
                    Long time = dateDifference(entryDate, exitDate);
                    if ((time != null) && !converted && (pagesViewed <= 1) && time <= 10)
                        numOfBounces++;
                });
        return numOfBounces;
    }

    public int calculateNumOfConversions() {
        numOfConversions = (int) records.getServerRecords().values()
                .parallelStream()
                .map(ServerRecord::getConversion)
                .filter(converted -> converted)
                .count();
        return numOfConversions;
    }

    public float calculateTotalCost() {
        totalCost = records.getImpressionRecords().values()
                .parallelStream()
                .map(ImpressionRecord::getImpressionCost)
                .reduce((float) 0.00, Float::sum);
        return totalCost;
    }

    public float calculateClickThroughRate() {
        clickThroughRate = (float) (((double) getNumOfClicks() / getNumOfImpressions()) * 100);
        return clickThroughRate;
    }

    public float calculateCostPerAction() {
        costPerAction = totalCost / numOfConversions;
        return costPerAction;
    }

    public float calculateCostPerClick() {
        costPerClick = totalCost / getNumOfClicks();
        return costPerClick;
    }

    public float calculateCostPerThousand() {
        costPerThousand = ((1000 * (totalCost)) / numOfImpressions);
        return costPerThousand;
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

    public float getTotalCost() {
        return totalCost;
    }

    public float getClickThroughRate() {
        return clickThroughRate;
    }

    public float getCostPerAction() {
        return costPerAction;
    }

    public float getCostPerClick() {
        return costPerClick;
    }

    public float getCostPerThousand() {
        return costPerThousand;
    }

    public float getBounceRate() {
        return bounceRate;
    }

    public List<String> getRecommendations() {
        return recommendations;
    }

    public List<String> getListOfClickCosts() {
        return records.getClickRecords()
                .values()
                .parallelStream()
                .map(ClickRecord::getClickCost)
                .collect(Collectors.toList());
    }

    public BarChart getHistogram() {
        return chartMetrics.buildHistogram();
    }


    private class ChartMetrics {
        private static final int BIN_WIDTH = 2;
        private List<String> frequencyOfClickCosts = getListOfClickCosts();

        public ChartMetrics() {

        }

        private List convertToInteger() {

            return frequencyOfClickCosts
                    .parallelStream()
                    .map(s -> {
                        double dbl = Double.parseDouble(s);
                        return Precision.round(dbl, 0, RoundingMode.UP.ordinal());
                    }).collect(Collectors.toList());
        }

        private Map<String, Integer> distributionMap;

        private Map<String, Integer> getHistogramData() {
            Frequency frequency = new Frequency();
            List<Double> streamOfClicks = convertToInteger();
            streamOfClicks.forEach(frequency::addValue);

            streamOfClicks.stream()
                    .distinct()
                    .sorted()
                    .forEach(observation -> {
                        int observationFrequency = (int) frequency.getCount(observation);

                        int upperBoundary;
                        if (observation > BIN_WIDTH) {
                            upperBoundary = (int) (Math.ceil(observation / BIN_WIDTH) * BIN_WIDTH);

                        } else upperBoundary = BIN_WIDTH;

                        int lowerBoundary;
                        if (upperBoundary > BIN_WIDTH)
                            lowerBoundary = upperBoundary - BIN_WIDTH;
                        else lowerBoundary = 0;
                        String bin = lowerBoundary + "-" + upperBoundary;

                        updateHistogramBins(lowerBoundary, bin, observationFrequency);
                    });
            return distributionMap;
        }

        private void updateHistogramBins(int lowerBoundary, String bin, int observationFrequency) {

            int prevLowerBoundary;
            if (lowerBoundary > BIN_WIDTH) prevLowerBoundary = lowerBoundary - BIN_WIDTH;
            else prevLowerBoundary = 0;

            String prevBin = prevLowerBoundary + "-" + lowerBoundary;
            if (!distributionMap.containsKey(prevBin))
                distributionMap.put(prevBin, 0);

            if (!distributionMap.containsKey(bin)) {
                distributionMap.put(bin, observationFrequency);
            } else {
                int oldFrequency = (distributionMap.get(bin));
                distributionMap.replace(bin, oldFrequency + observationFrequency);
            }
        }

        private BarChart buildHistogram() {
            distributionMap = new TreeMap<>(new DoublesComaprator());
            distributionMap = getHistogramData();
            return buildHistogramChart(distributionMap);
        }

        private void buildImpressionsChart() {
            Map impressionChartData = new TreeMap();
            impressionChartData = getNumOfImpressionsChartInRange();
        }

        private Map getNumOfImpressionsChartInRange(DateRange dateRange) {

            List<ImpressionRecord> impressionRecords = (List) records.getImpressionRecords();
            var datesInBetween = dateRange.toList();
            return impressionRecords.stream()
                    .map(impressionRecord -> {
                        return impressionRecord.getDate().toLocalDate();
                    })
                    .filter(date -> datesInBetween.contains(date))
                    .collect(Collectors.groupingBy(date -> date, Collectors.counting()));
        }

        private Map getNumOfImpressionsChartInRange() {
            List<ImpressionRecord> impressionRecords = (List) records.getImpressionRecords().values();
            return impressionRecords
                    .parallelStream()
                    .map(rec -> rec.getDate().toLocalDate())
                    .collect(Collectors.groupingBy(date -> date, Collectors.counting()));
        }

        private Map getNumOfClicksChartInRange() {
            return null;
        }

        public Map getClickThroughRateChartData() {
            return null;
        }

        public Map getCostPerActionChartData() {
            return null;
        }

        public Map getCostPerClickChartData() {
            return null;
        }

        public Map getCostPerThousandChartData() {
            return null;
        }

        public Map getBounceRateChartData() {
            return null;
        }

        private class DoublesComaprator implements Comparator {
            @Override
            public int compare(Object o1, Object o2) {

                String bin = ((String) o1).split("-")[0];
                String bin2 = ((String) o2).split("-")[0];

                Double binValue = Double.valueOf(bin);
                Double bin2Value = Double.valueOf(bin2);

                return Double.compare(binValue, bin2Value);
            }
        }


    }
}
