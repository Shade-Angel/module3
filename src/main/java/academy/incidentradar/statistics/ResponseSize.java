package academy.incidentradar.statistics;

public class ResponseSize {
    private final double average;
    private final long max;
    private final double p95;

    public ResponseSize(double average, long max, double p95) {
        this.average = average;
        this.max = max;
        this.p95 = p95;
    }

    public double getAverage() {
        return average;
    }

    public long getMax() {
        return max;
    }

    public double getP95() {
        return p95;
    }
}
