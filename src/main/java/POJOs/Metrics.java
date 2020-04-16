package POJOs;

import org.apache.commons.math3.stat.Frequency;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static Configuration.Parser.dateDifference;

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

    private List<String> ageRanges;
    private ArrayList<String> recommendations = new ArrayList<>();

    public Metrics(Records records) {
        this.records = records;
        this.ageRanges = getAgeRanges();
        calculateMetrics();
        calculateRecommendations();
        printMetrics();
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
        /*System.out.println("Total Cost of Campaign: " + getTotalCost());
        System.out.println("CPM: " + getCostPerThousand());
        System.out.println("Number of Conversions: " + getNumOfConversions());
        System.out.println("Cost Per Acquisition: " + getCostPerAction());
        System.out.println("Cost Per Click: " + getCostPerClick());
        System.out.println("Number of Bounces: " + getNumOfBounces());
        System.out.println("Bounce Rate: " + getBounceRate());*/
        //System.out.println("Size of click costs: " + getListOfClickCosts().size());
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
                    if (time != null) {
                        if (!converted && pagesViewed <= 1 && time <= 10)
                            numOfBounces++;
                    }
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
        clickThroughRate = (getNumOfClicks() / getNumOfImpressions()) * 100;
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

    public void getChartMetrics() {
        new Thread(
                new ChartMetrics()
        ).start();
    }

    public class ChartMetrics implements Runnable {

        private final List<String> frequencyOfClickCosts = getListOfClickCosts();
        private final BigDecimal bigClassWidth = BigDecimal.valueOf(2.50d);
        private Map<String, Long> distributionMap;

        public ChartMetrics() {

        }

        public List<BigDecimal> convertToBigDecimal() {
            return frequencyOfClickCosts
                    .stream()
                    .map(BigDecimal::new)
                    .map(t -> t.setScale(0, RoundingMode.CEILING))
                    .collect(Collectors.toList());
        }

        private Map<String, Long> getHistogramData() {
            Frequency frequency = new Frequency();
            List<BigDecimal> bigDecimals = convertToBigDecimal();
            bigDecimals.forEach(frequency::addValue);

            bigDecimals.stream()
                    .distinct()
                    .sorted()
                    .forEach(observation -> {
                        long observationFrequency = frequency.getCount(observation);

                        BigDecimal upperBoundary;
                        if (observation.compareTo(bigClassWidth) > 0) {
                            BigDecimal division = observation.divide(bigClassWidth, RoundingMode.CEILING);
                            upperBoundary = division.multiply(bigClassWidth);
                        } else upperBoundary = bigClassWidth;

                        BigDecimal lowerBoundary;
                        if (upperBoundary.compareTo(bigClassWidth) > 0)
                            lowerBoundary = upperBoundary.subtract(bigClassWidth);
                        else lowerBoundary = BigDecimal.ZERO;
                        String bin = lowerBoundary.toPlainString() + "-" + upperBoundary.toPlainString();

                        updateDistributionMap(lowerBoundary, bin, observationFrequency);
                    });
            return distributionMap;
        }

        private void updateDistributionMap(BigDecimal lowerBoundary, String bin, long observationFrequency) {

            BigDecimal prevLowerBoundary;
            if (lowerBoundary.compareTo(bigClassWidth) > 0) prevLowerBoundary = lowerBoundary.subtract(bigClassWidth);
            else prevLowerBoundary = BigDecimal.ZERO;

            String prevBin = prevLowerBoundary.toPlainString() + "-" + lowerBoundary.toPlainString();
            if (!distributionMap.containsKey(prevBin))
                distributionMap.put(prevBin, 0L);

            if (!distributionMap.containsKey(bin)) {
                distributionMap.put(bin, observationFrequency);
            } else {
                Long oldFrequency = Long.parseLong(distributionMap.get(bin).toString());
                distributionMap.replace(bin, oldFrequency + observationFrequency);
            }
        }

        private void buildHistogram() {
            distributionMap = new TreeMap<String, Long>(new StringsComparator());
            distributionMap = getHistogramData();
            List yData = new ArrayList(distributionMap.values());
            List xData = Arrays.asList(distributionMap.keySet().toArray());
            CategoryChart chart = buildChart(xData, yData);
            new SwingWrapper<>(chart).displayChart();
        }

        private CategoryChart buildChart(List xData, List yData) {

            // Create Chart
            CategoryChart chart = new CategoryChartBuilder().width(800).height(600)
                    .title("Distribution of Costs")
                    .xAxisTitle("Costs")
                    .yAxisTitle("Frequency")
                    .build();

            chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
            chart.getStyler().setAvailableSpaceFill(0.99);
            chart.getStyler().setOverlapped(true);

            chart.addSeries("Click Costs", xData, yData);

            return chart;
        }

        @Override
        public void run() {
            buildHistogram();
        }
    }

    private class StringsComparator implements Comparator {


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
