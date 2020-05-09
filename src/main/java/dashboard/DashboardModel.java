package dashboard;

import POJOs.Campaign;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import metrics.Metrics;

import java.util.ArrayList;

public class DashboardModel {
    private Campaign currentCampaign;
    private ArrayList<Campaign> listOfCampaigns;
    private Metrics metrics;
    private ObservableList<?> recommendations;

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
}
