package fr.unice.namb.spark.Operators;

import fr.unice.namb.utils.configuration.Config;
import fr.unice.namb.utils.configuration.schema.NambConfigSchema;

public class WorkloadHandler {

    // workload properties
    private double workloadProcessingLoad;
    private Config.LoadBalancing workloadBalancing;

    public WorkloadHandler(NambConfigSchema.Workload workload) {
        this.workloadProcessingLoad = workload.getProcessing();
        this.workloadBalancing = workload.getBalancing();
    }

    public int getLoad(int nodeId) {
        return 0;
    }
}
