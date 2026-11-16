package academy.incidentradar.statistics;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReportDto {
    private final List<String> files;

    @JsonProperty("totalRequestsCount")
    private final long totalRequestsCount;

    @JsonProperty("responseSizeInBytes")
    private final ResponseSize responseSizeInBytes;

    private final List<ResourceCount> resources;
    private final Map<Integer, Long> responseCodes;

    public  ReportDto(Statistics stat){
        this.files = stat.getFiles();
        this.totalRequestsCount = stat.getTotalRequests();
        this.responseSizeInBytes = new ResponseSize(stat.getAvgSize(), stat.getMaxSize(), stat.getP95Size());
        this.resources = stat.getTopResources().stream()
            .map(e -> new ResourceCount(e.getKey(), e.getValue()))
            .toList();
        this.responseCodes = stat.getResCode();
    }

    public List<String> getFiles(){
        return files;
    }

    public long getTotalRequestsCount(){
        return totalRequestsCount;
    }

    public ResponseSize getResponseSizeInBytes(){
        return responseSizeInBytes;
    }

    public List<ResourceCount> getResources(){
        return resources;
    }

    public Map<Integer, Long> getReaponseCode(){
        return responseCodes;
    }
}
