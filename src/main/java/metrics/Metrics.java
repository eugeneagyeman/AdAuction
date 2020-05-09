package metrics;

import POJOs.*;
import com.google.common.collect.Maps;
import dashboard.DateRange;
import dashboard.Filter;
import gui.charts.ChartBuilder;
import javafx.scene.chart.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.util.Precision;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static configuration.Parser.dateDifference;
import static gui.charts.ChartBuilder.buildHistogramChart;
import static gui.charts.ChartBuilder.buildTimeSeriesChart;
import static java.util.stream.Collectors.*;

public class Metrics {
    private ChartMetrics chartMetrics;
    private String context;
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
    private final Records records;
    private LocalDate startDate;
    private LocalDate endDate;
    private final List<String> ageRanges;
    private final ArrayList<String> recommendations = new ArrayList<>();

    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Metrics(Records records) {
        this.records = records;
        this.ageRanges = getAgeRanges();
        calculateMetrics();
        calculateRecommendations();
        //printMetrics();
        chartMetrics = new ChartMetrics();
    }

    public Metrics(Records records, String context) {
        this.context = context;
        this.records = records;
        this.ageRanges = getAgeRanges();
        calculateMetrics();
        calculateRecommendations();
        //printMetrics();
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
        calculateStartDate();
        calculateEndDate();

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

    public Map percentagesByAge() {
        return null;
    }

    public Map percentagesOfContext() {
        Collection<ImpressionRecord> values = records.getImpressionRecords().values();
        Map<String, Long> contextCount = values
                .stream()
                .map(ImpressionRecord::getContext)
                .collect(groupingBy(Function.identity(), Collectors.counting()));

        Map percentages = new TreeMap();
        contextCount.keySet()
                .stream()
                .forEach(occurrence -> {
                    Long percent = (contextCount.get(occurrence) * 100 / values.size());
                    percentages.put(occurrence, percent);
                });

        return percentages;
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

    public void calculateStartDate() {
        Map<String, Collection<ImpressionRecord>> clickRecords = records.getImpressionRecords().asMap();
        clickRecords.forEach((k, v) -> {
            LocalDate earliestDate = v.stream()
                    .map(ImpressionRecord::getLocalDate)
                    .min(LocalDate::compareTo)
                    .get();
            setStartDate(earliestDate);
        });
    }

    public void calculateEndDate() {
        Map<String, Collection<ImpressionRecord>> clickRecords = records.getImpressionRecords().asMap();
        clickRecords.forEach((k, v) -> {
            LocalDate latestDate = v.stream()
                    .map(ImpressionRecord::getLocalDate)
                    .max(LocalDate::compareTo)
                    .get();
            setEndDate(latestDate);
        });
    }

    public ChartMetrics getChartMetrics() {
        return this.chartMetrics;
    }

    public class ChartMetrics {
        private final List listOfCharts = new ArrayList();
        private Map<String, Integer> distributionMap;
        private final Collection<Chart> segmentCollection = new ArrayList<>();
        private final Collection<Chart> contextCollection = new ArrayList<>();

        private List convertToInteger() {
            final List<String> frequencyOfClickCosts = getListOfClickCosts();
            return frequencyOfClickCosts
                    .parallelStream()
                    .map(s -> {
                        double dbl = Double.parseDouble(s);
                        return Precision.round(dbl, 0, RoundingMode.UP.ordinal());
                    }).collect(Collectors.toList());
        }

        private Map<String, Integer> getHistogramData() {
            int BIN_WIDTH = 2;

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
            int BIN_WIDTH = 2;

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
            distributionMap = new TreeMap<>(new DoublesComparator());
            distributionMap = getHistogramData();
            return buildHistogramChart(distributionMap, buildTitle("Histogram of Click Costs"));
        }

        private String buildTitle(String title) {
            if(context!=null) return title +" by"+ context;
            return title;
        }

        private LineChart buildImpressionsChart() {
            Map impressionChartData = getNumOfImpressionsDateMap();
            return buildTimeSeriesChart(impressionChartData, "Date", "Count", "Impressions", buildTitle("Number of Impressions Daily"));
        }

        private LineChart buildClickChart() {
            Map clickChartData = getNumOfClicksDateMap();
            return buildTimeSeriesChart(clickChartData, "Date", "Count", "Number Of Clicks", buildTitle("Number of Clicks Daily"));
        }

        private StackedBarChart buildConversionChart() {
            Map data = getConversionsDateMap();
            StackedBarChart chart = ChartBuilder.buildStackedBarChart(data, "Date", "Count", "Converted", buildTitle("Conversion Chart"));

            Map unconverted = getUnconvertedDateMap();
            chart.getData().add(ChartBuilder.buildSeries(unconverted, "Unconverted"));
            return chart;
        }

        private Map getNumOfImpressionsDateMap(DateRange dateRange) {
            List<ImpressionRecord> impressionRecords = (List) records.getImpressionRecords();
            var datesInBetween = dateRange.toList();
            return impressionRecords.stream()
                    .map(impressionRecord -> {
                        return impressionRecord.getDate().toLocalDate();
                    })
                    .filter(datesInBetween::contains)
                    .collect(groupingBy(date -> date, Collectors.counting()));
        }

        private Map getNumOfImpressionsDateMap() {
            Collection<ImpressionRecord> impressionRecords = records.getImpressionRecords().values();
            Map<LocalDate, Long> reverse = new TreeMap<>(Collections.reverseOrder());
            reverse.putAll(impressionRecords
                    .parallelStream()
                    .map(rec -> rec.getDate().toLocalDate())
                    .collect(groupingBy(date -> date, Collectors.counting())));
            return reverse;
        }

        private Map getNumOfClicksDateMap() {
            Collection<ClickRecord> clickRecords = records.getClickRecords().values();
            Map<LocalDate, Long> reverse = new TreeMap<>(Collections.reverseOrder(LocalDate::compareTo));

            reverse.putAll(clickRecords
                    .parallelStream()
                    .map(rec -> rec.getDate().toLocalDate())
                    .collect(groupingBy(date -> date, Collectors.counting())));
            return reverse;

        }

        private Map getConversionsDateMap() {
            Collection<ServerRecord> serverRecords = records.getServerRecords().values();
            Map<LocalDate, Long> reverse = new TreeMap<>(Collections.reverseOrder(LocalDate::compareTo));
            reverse.putAll(serverRecords
                    .parallelStream()
                    .filter(ServerRecord::getConversion)
                    .map(serverRecord -> serverRecord.getEntryDate().toLocalDate())
                    .collect(groupingBy(date -> date, Collectors.counting())));
            return reverse;
        }

        private Map getUnconvertedDateMap() {
            Collection<ServerRecord> serverRecords = records.getServerRecords().values();
            Map<LocalDate, Long> reverse = new TreeMap<>(Collections.reverseOrder(LocalDate::compareTo));
            reverse.putAll(serverRecords
                    .parallelStream()
                    .filter(serverRecord -> !serverRecord.getConversion())
                    .map(serverRecord -> serverRecord.getEntryDate().toLocalDate())
                    .collect(groupingBy(date -> date, Collectors.counting())));

            return reverse;
        }

        public Map getClickThroughRateChartData() {
            Map<LocalDate, Integer> numOfClicks = records.dateToClickCountMap();
            Map<LocalDate, Integer> numOfImpressions = records.dateToImpressionCountMap();
            Map<LocalDate, Integer> ctrByDay = new ConcurrentHashMap<>(numOfClicks.size());
            numOfClicks.forEach((k, v) -> {
                int numOfDayClicks = v;
                int numOfImpressionDay = numOfImpressions.get(k);
                int ctr = numOfDayClicks / numOfImpressionDay;
                ctrByDay.put(k, ctr);

            });
            return ctrByDay;
        }

        //TODO: Equality by value of LocalDate... Convert toString
        public Map<LocalDate, Float> getCostPerActionChartData() {
            Map<LocalDate, Float> costPerDay = records.dateToAdCostMap();
            Map<LocalDate, Integer> impressionsDay = records.dateToImpressionCountMap();
            Map<LocalDate, Float> cpaPerDayMap = new TreeMap<>(LocalDate::compareTo);
            costPerDay.forEach((k, v) -> {
                Float adSpend = v;
                Float numOfImpressionDay = impressionsDay.get(k).floatValue();
                Float ctr = adSpend / numOfImpressionDay;
                cpaPerDayMap.put(k, ctr);

            });
            return cpaPerDayMap;
        }

        public Map getCostPerClickChartData() {
            Map<LocalDate, Float> costPerDay = records.dateToAdCostMap();
            Map<LocalDate, Integer> clicksPerDay = records.dateToClickCountMap();
            Map<LocalDate, Float> cpcPerDay = Maps.newTreeMap(LocalDate::compareTo);

            costPerDay.forEach((key, value) -> {
                Float cost = value;
                Float clicks = clicksPerDay.get(key).floatValue();
                Float cpc = cost / clicks;
                cpcPerDay.put(key, cpc);
            });
            return cpcPerDay;
        }

        public Map getCostPerThousandChartData() {
            return Maps.transformValues(getCostPerActionChartData(), value -> value * 1000f);
        }

        public Map getBounceRateChartData() {
            return null;
        }

        public Map getTotalCostPerDayData() {
            return records.dateToAdCostMap();
        }
        public Map getNumOfUniquesChartData() {
            Map<String, Collection<ClickRecord>> clickRecords = records.getClickRecords().asMap();
            Map<String, LocalDate> uniquesPerDateMap = new TreeMap<>();
            clickRecords.forEach((k, v) -> {
                LocalDate earliestDate = v.stream()
                        .map(ClickRecord::getLocalDate)
                        .min(LocalDate::compareTo)
                        .get();

                uniquesPerDateMap.put(k, earliestDate);
            });

            Map<LocalDate,Long> result = new TreeMap<>(LocalDate::compareTo);
            result.putAll(uniquesPerDateMap.values()
                                .stream()
                                .collect(groupingBy(date -> date, counting())));

            Map<String, Long> newMap = result
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(e -> e.getKey().toString(),Map.Entry::getValue));

            return newMap;

        }

        public Collection<Chart> buildSetOfCharts() {

            BarChart histogram = buildHistogram();
            StackedBarChart conversionsChart = buildConversionChart();
            LineChart impressionsChart = buildImpressionsChart();
            LineChart numOfClicksChart = buildClickChart();
            BarChart numOfUniquesChart = ChartBuilder.buildBarChart(getNumOfUniquesChartData(), buildTitle("Number of Uniques"));
            LineChart totalCostChart = ChartBuilder.buildTimeSeriesChart(getTotalCostPerDayData(),"Date","Cost","Total Cost", buildTitle("Total Cost Per Day"));
            LineChart costChart = ChartBuilder.buildTimeSeriesChart(getClickThroughRateChartData(),"Date","Amount", "Click Through Rate", buildTitle("Click Calculations"));
            costChart.getData()
                     .addAll(ChartBuilder.buildSeries(getCostPerActionChartData(), "Cost Per Action"),
                             ChartBuilder.buildSeries(getCostPerClickChartData(), "Cost per click"));

            segmentCollection.add(histogram);
            segmentCollection.add(impressionsChart);
            segmentCollection.add(numOfClicksChart);
            segmentCollection.add(numOfUniquesChart);
            segmentCollection.add(conversionsChart);
            segmentCollection.add(totalCostChart);
            segmentCollection.add(costChart);
            return segmentCollection;
        }

        public PieChart buildContextDistribution() {
            Map data = percentagesOfContext();
            return ChartBuilder.buildPieChart(data, "Context");
        }

        public Collection<Chart> getSegmentCharts() {
            if (segmentCollection.isEmpty()) return buildSetOfCharts();
            return segmentCollection;
        }

        public Collection<Chart> buildContextCharts() {
            Filter contextRecords = new Filter(records);
            Metrics newsRec = contextRecords.contextFilter("News").buildMetrics();
            Metrics shoppingRec = contextRecords.contextFilter("Shopping").buildMetrics();
            Metrics socialMediaRec = contextRecords.contextFilter("Social Media").buildMetrics();
            Metrics hobbiesRec = contextRecords.contextFilter("Hobbies").buildMetrics();
            Metrics travelRec = contextRecords.contextFilter("Travel").buildMetrics();

            contextCollection.addAll(newsRec.getChartMetrics().getContextCharts());
           /* contextCollection.addAll(shoppingRec.getChartMetrics().buildSetOfCharts());
            contextCollection.addAll(socialMediaRec.getChartMetrics().buildSetOfCharts());
            contextCollection.addAll(hobbiesRec.getChartMetrics().buildSetOfCharts());
            contextCollection.addAll(newsRec.getChartMetrics().buildSetOfCharts());
            contextCollection.addAll(travelRec.getChartMetrics().buildSetOfCharts());*/

            return contextCollection;

        }
        public Collection<Chart> getContextCharts() {
            if(contextCollection.isEmpty()) return buildContextCharts();
            return contextCollection;
        }

        public Collection<Chart> getAllCharts() {
            Collection<Chart> charts = new HashSet();
            charts.addAll(getContextCharts());
            charts.addAll(getSegmentCharts());

            return charts;

        }
        private class DoublesComparator implements Comparator {
            @Override
            public int compare(Object o1, Object o2) {

                String bin = ((String) o1).split("-")[0];
                String bin2 = ((String) o2).split("-")[0];

                double binValue = Double.parseDouble(bin);
                double bin2Value = Double.parseDouble(bin2);

                return Double.compare(binValue, bin2Value);
            }
        }

    }

}
