package academy.incidentradar.filter;

import java.time.LocalDate;
import java.util.function.Predicate;

import academy.incidentradar.model.LogRecord;

public class DateFilter implements Predicate<LogRecord> {
    private final LocalDate from;
    private final LocalDate to;

    public  DateFilter(LocalDate from, LocalDate to){
        this.from = from;
        this.to = to;
    }

    @Override 
    public boolean test(LogRecord record){
        LocalDate recordDate = record.date();

        if(from != null && recordDate.isBefore(from)){
            return false;
        }

        if(to != null && recordDate.isAfter(to)){
            return false;
        }

        return true;
    }
}
