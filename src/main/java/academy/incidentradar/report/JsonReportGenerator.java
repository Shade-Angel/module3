package academy.incidentradar.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import academy.incidentradar.statistics.ReportDto;
import academy.incidentradar.CliException;
import academy.incidentradar.statistics.Statistics;

public class JsonReportGenerator implements ReportGenerate{
    private static  final ObjectMapper MAPPER = new ObjectMapper()
        .enable(SerializationFeature.INDENT_OUTPUT);

    @Override 
    public String generate(Statistics stat){
        try{
            return MAPPER.writeValueAsString(new ReportDto(stat));
        }catch(Exception e){
            throw new CliException("Ошибка сериализации JSON(JsonReportGenerate)  : " + e.getMessage());
        }
    }
}
