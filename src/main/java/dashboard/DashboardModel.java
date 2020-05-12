package dashboard;

import POJOs.Campaign;
import POJOs.ImpressionRecord;
import com.google.common.collect.Multimap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import metrics.Metrics;

import java.util.ArrayList;

public class DashboardModel {
    private Campaign currentCampaign;
    private ArrayList<Campaign> listOfCampaigns;
    private Metrics metrics;
    private ObservableList<?> recommendations;
    private Filter filter;
    private FilterTree<Multimap<String, ImpressionRecord>> filterTree;

    public DashboardModel() {
        listOfCampaigns = new ArrayList<>();
    }

    public Campaign getCurrentCampaign() {
        return currentCampaign;
    }

    public DashboardModel setCurrentCampaign(Campaign currentCampaign) {
        this.currentCampaign = currentCampaign;
        this.metrics = currentCampaign.getMetrics();
        this.recommendations = FXCollections.observableArrayList(currentCampaign.getMetrics().getRecommendations());
        this.filter = new Filter(this);
        this.filterTree = new FilterTree<>(filter, currentCampaign.getRecords().getImpressionRecords());
        return this;
    }

    public ArrayList<Campaign> getListOfCampaigns() {
        return listOfCampaigns;
    }

    public DashboardModel setListOfCampaigns(ArrayList<Campaign> listOfCampaigns) {
        this.listOfCampaigns = listOfCampaigns;
        return this;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public Metrics.ChartMetrics getChartMetrics() {
        return metrics.getChartMetrics();
    }


    public DashboardModel setMetrics(Metrics metrics) {
        this.metrics = metrics;
        return this;
    }

    public ObservableList<?> getRecommendations() {
        return recommendations;
    }

    public DashboardModel setRecommendations(ObservableList<?> recommendations) {
        this.recommendations = recommendations;
        return this;
    }

    public boolean update (Multimap filteredMap) {
        getCurrentCampaign().getRecords().update(filteredMap);
        return true;
    }

    public Filter getFilter() {
        return filter;
    }

    public FilterTree<Multimap<String, ImpressionRecord>> getFilterTree() {
        return filterTree;
    }

    public Boolean updateCharts(Multimap<String,ImpressionRecord> filteredMap) {
        getCurrentCampaign().getRecords().update(filteredMap);
        getCurrentCampaign().getMetrics().updateMetrics();

        return true;
    }
}
