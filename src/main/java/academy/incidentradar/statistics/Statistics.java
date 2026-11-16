package academy.incidentradar.statistics;

import academy.incidentradar.model.LogRecord;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Statistics {
    private final List<String> files;
    private final long totalRequests;
    private final Map<Integer, Long> resCode;
    private final List<Map.Entry<String, Long>> topResources;
    private final double avgSize;
    private final long maxSize;
    private final double p95Size;

    private static final int TOP_RESOURCES_LIMIT = 10;
    private static final double ROUNDING_MULTIPLIER = 100.0;
    private static final double P95_PERCENTILE = 0.95;

    public Statistics(List<LogRecord> record){
        this(List.of(), record);
    }

    public Statistics(List<String> files, List<LogRecord> records) {
        this.files = files != null ? List.copyOf(files) : List.of();
        this.totalRequests = records.size();
        this.resCode = records.stream()
            .collect(Collectors.groupingBy(LogRecord::status, Collectors.counting()));
        this.topResources = records.stream()
                .collect(Collectors.groupingBy(LogRecord::resource, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(TOP_RESOURCES_LIMIT)
                .collect(Collectors.toList());

        List<Long> sizes = records.stream()
            .map(LogRecord::bodyBytesSent)
            .sorted()
            .collect(Collectors.toList());

        if (sizes.isEmpty()) {
            this.avgSize = 0.0;
            this.maxSize = 0;
            this.p95Size = 0.0;
        } else {
            double sum = sizes.stream().mapToLong(Long::longValue).sum();
            this.avgSize = Math.round((sum / sizes.size()) * ROUNDING_MULTIPLIER) / ROUNDING_MULTIPLIER;
            this.maxSize = sizes.get(sizes.size() - 1);

            int n = sizes.size();
            int rank = (int) Math.ceil(P95_PERCENTILE * n);
            int idx = Math.max(0, Math.min(rank - 1, n - 1));
            this.p95Size = sizes.get(idx);
        }
    }

    public List<String> getFiles(){
        return files;
    }

    public long getTotalRequests() {
        return totalRequests;
    }

    public Map<Integer, Long> getResCode() {
        return resCode;
    }

    public List<Map.Entry<String, Long>> getTopResources() {
        return topResources;
    }

    public double getAvgSize() {
        return avgSize;
    }

    public long getMaxSize() {
        return maxSize;
    }

    public double getP95Size() {
        return p95Size;
    }
}
