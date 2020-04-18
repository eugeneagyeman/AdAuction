package gui.charts;

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

        final LineChart timeSeriesChart =
                null;

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Timeseries");

        xyMap.forEach((k, v) -> series.getData().add(new XYChart.Data(k, v)));
        timeSeriesChart.getData().addAll(series);
        return timeSeriesChart;


    }
}
