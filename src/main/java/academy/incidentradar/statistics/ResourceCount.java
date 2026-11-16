package academy.incidentradar.statistics;

public class ResourceCount {
    private final String resource;
    private final long count;

    public ResourceCount(String resource, long count){
        this.resource = resource;
        this.count = count;
    }

    public String getResource(){
        return resource;
    }

    public long getCount(){
        return count;
    }
}