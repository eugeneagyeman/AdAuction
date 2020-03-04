package Dashboard;

import POJOs.Campaign;
import POJOs.Metrics;

import java.util.ArrayList;

public class DashboardModel {
    private Campaign currentCampaign;
    private ArrayList<Campaign> listOfCampaigns;
    private Metrics metrics;

    public DashboardModel() {
        listOfCampaigns = new ArrayList<>();
    }

    public Campaign getCurrentCampaign() {
        return currentCampaign;
    }

    public DashboardModel setCurrentCampaign(Campaign currentCampaign) {
        this.currentCampaign = currentCampaign;
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

    public DashboardModel setMetrics(Metrics metrics) {
        this.metrics = metrics;
        return this;
    }
}
