package academy.incidentradar.report;
import academy.incidentradar.statistics.Statistics;

public interface ReportGenerate {
    String generate(Statistics stat);
}
