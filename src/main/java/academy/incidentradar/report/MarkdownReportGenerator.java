package academy.incidentradar.report;

import academy.incidentradar.statistics.Statistics;
import java.util.Map;

public class MarkdownReportGenerator implements ReportGenerate {

    @SuppressWarnings("checkstyle:MagicNumber")
    private static final Map<Integer, String> HTTP_REASONS = Map.ofEntries(
            Map.entry(200, "200 OK"),
            Map.entry(201, "201 Created"),
            Map.entry(204, "204 No Content"),
            Map.entry(301, "301 Moved Permanently"),
            Map.entry(302, "302 Found"),
            Map.entry(304, "304 Not Modified"),
            Map.entry(400, "400 Bad Request"),
            Map.entry(401, "401 Unauthorized"),
            Map.entry(403, "403 Forbidden"),
            Map.entry(404, "404 Not Found"),
            Map.entry(500, "500 Internal Server Error"),
            Map.entry(502, "502 Bad Gateway"),
            Map.entry(503, "503 Service Unavailable"));

    @Override
    public String generate(Statistics stat) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Incident Radar Report\n\n");

        sb.append("## Files\n\n");
        for (String file : stat.getFiles()) {
            sb.append("- ").append(file).append("\n");
        }
        sb.append("\n");

        sb.append("## Summary\n\n");
        sb.append("- **Total Requests**: ").append(stat.getTotalRequests()).append("\n");
        sb.append("- **Average Response Size**: ").append(stat.getAvgSize()).append(" bytes\n");
        sb.append("- **Max Response Size**: ").append(stat.getMaxSize()).append(" bytes\n");
        sb.append("- **P95 Response Size**: ").append(stat.getP95Size()).append(" bytes\n\n");

        sb.append("## Response Codes\n\n");
        sb.append("| Code | Count |\n|------|-------|\n");
        stat.getResCode().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> sb.append("| ")
                        .append(getHttpReason(e.getKey()))
                        .append(" | ")
                        .append(e.getValue())
                        .append(" |\n"));
        sb.append("\n");

        sb.append("## Top Resources\n\n");
        sb.append("| Resource | Count |\n|----------|-------|\n");
        for (var entry : stat.getTopResources()) {
            sb.append("| ")
                    .append(entry.getKey())
                    .append(" | ")
                    .append(entry.getValue())
                    .append(" |\n");
        }

        return sb.toString();
    }

    private static String getHttpReason(int code) {
        return HTTP_REASONS.getOrDefault(code, code + " Unknown");
    }
}
