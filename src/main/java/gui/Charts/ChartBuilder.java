package gui.charts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;

import java.util.Map;

public class ChartBuilder {
    public static BarChart buildHistogramChart(Map xyMap) {

        // Create Chart
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Costs in Pence");
        yAxis.setLabel("Frequency");

        final BarChart<String, Number> histogram =
                new BarChart<>(xAxis, yAxis);

        XYChart.Series<String, Number> series = new XYChart.Series();
        series.setName("Histogram");

        xyMap.forEach((k, v) -> series.getData().add(new XYChart.Data(k, v)));
        histogram.getData().addAll(series);
        return histogram;
    }

    public static LineChart buildTimeSeriesChart(Map xyMap, String xLabel, String yLabel) {
        final CategoryAxis dateAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

        dateAxis.setLabel("Date in: " + xLabel);
        yAxis.setLabel(yLabel);

        final LineChart timeSeriesChart = new LineChart(dateAxis, yAxis);

        XYChart.Series<String, Number> series = buildSeries(xyMap);
        timeSeriesChart.getData().addAll(series);
        return timeSeriesChart;
    }

    public static StackedBarChart buildStackedBarChart(Map xyMap, String xLabel, String yLabel) {
        final CategoryAxis dateAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

        dateAxis.setLabel("Date" + xLabel);
        yAxis.setLabel(yLabel);

        final StackedBarChart stackedBarChart = new StackedBarChart(dateAxis, yAxis);

        XYChart.Series<String, Number> series = buildSeries(xyMap);
        stackedBarChart.getData().addAll(series);

        return stackedBarChart;
    }

    public static PieChart buildPieChart(Map xyMap) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        xyMap.forEach((key, value) -> {
            Double val = Double.parseDouble(Long.toString((long) value));
            pieChartData.add(new PieChart.Data((String) key, val));
        });
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Context");
        return chart;

    }

    public static BarChart buildBarChart(Map xyMap) {
        final CategoryAxis dateAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart bc = new BarChart(dateAxis, yAxis);
        bc.setTitle("Bar Chart");
        XYChart.Series series = new XYChart.Series();
        xyMap.forEach((key, val) -> series.getData().add(new XYChart.Data(key, val)));
        bc.getData().add(series);
        return bc;
    }

    public static XYChart.Series<String, Number> buildSeries(Map xyMap) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Timeseries");

        xyMap.forEach((k, v) -> {
            String date = k.toString();
            series.getData().add(new XYChart.Data(date, v));
        });
        return series;
    }
}
