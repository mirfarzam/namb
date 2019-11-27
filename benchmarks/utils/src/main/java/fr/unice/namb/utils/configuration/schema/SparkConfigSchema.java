package fr.unice.namb.utils.configuration.schema;

public class SparkConfigSchema extends ConfigSchema {

    private static float debugFrequency;
    private static int batchTime;
    private static String master;
    private static String applicationName;

    public static float getDebugFrequency() {
        return debugFrequency;
    }
    public static int getBatchTime() { return batchTime; }
    public static String getMaster() {return master;}
    public static String getApplicationName() {return applicationName;}

    public void setDebugFrequency(float debugFrequency) {
        this.debugFrequency = debugFrequency;
    }
    public void setBatchTime(int batchtime) {
        this.batchTime = batchtime;
    }
    public void setMaster(String master) { this.master = master; }
    public void setApplicationName(String name) { this.applicationName = name; }
}
