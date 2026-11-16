package academy.incidentradar.report;

import academy.incidentradar.statistics.Statistics;
import java.util.Map;

public class MarkdownReportGenerator implements ReportGenerate {

    @Override
    public String generate(Statistics stat) {
        StringBuilder sb = new StringBuilder();

        sb.append("# Incident Radar Report \n \n");
        sb.append("## Files \n");
        for (String file : stat.getFiles()) {
            sb.append("- ").append(file + " \n");
        }
        sb.append(" \n # Report \n \n");
        sb.append("- **Total Requests**: ").append(stat.getTotalRequests()).append("\n");
        sb.append("- **Average Response Size**: ").append(stat.getAvgSize()).append(" bytes\n");
        sb.append("- **Max Response Size**: ").append(stat.getMaxSize()).append(" bytes\n");
        sb.append("- **P95 Response Size**: ").append(stat.getP95Size()).append(" bytes\n\n");

        sb.append("## Response Codes\n\n");
        sb.append("| Code | Count |\n|------|-------|\n");
        stat.getResCode().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> sb.append("| ")
                        .append(e.getKey())
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
}
