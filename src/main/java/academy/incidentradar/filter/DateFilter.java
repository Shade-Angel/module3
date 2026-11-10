package academy.incidentradar.filter;

import academy.incidentradar.model.LogRecord;
import java.time.LocalDate;
import java.util.function.Predicate;
import org.jspecify.annotations.Nullable;

public class DateFilter implements Predicate<LogRecord> {
    private final @Nullable LocalDate from;
    private final @Nullable LocalDate to;

    public DateFilter(@Nullable LocalDate from, @Nullable LocalDate to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean test(LogRecord record) {
        LocalDate recordDate = record.date();

        if (from != null && recordDate.isBefore(from)) {
            return false;
        }

        if (to != null && recordDate.isAfter(to)) {
            return false;
        }

        return true;
    }
}
