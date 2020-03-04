package GUI.Context;

import Dashboard.DashboardModel;

public class ContextController {
    DashboardModel model;

    public void initModel(DashboardModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialised once");
        }
        this.model = model;

    }
}
