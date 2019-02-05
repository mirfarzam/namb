package fr.unice.namb.utils;

public class StormConfigScheme {
    public enum StormDeployment{
        local, cluster
    }

    private int workers = 1;
    private StormDeployment deployment = StormDeployment.local;

    public int getWorkers() {
        return workers;
    }

    public void setWorkers(int workers) {
        this.workers = workers;
    }

    public StormDeployment getDeployment() {
        return deployment;
    }

    public void setDeployment(StormDeployment deployment) {
        this.deployment = deployment;
    }


}
