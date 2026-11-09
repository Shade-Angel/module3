package academy.incidentradar.parser;

import academy.incidentradar.model.LogRecord;
import java.time.DateTimeException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogParser {
    private static final int IP_INDEX = 1;
    private static final int DATE_STRING_INDEX = 2;
    private static final int REQUEST_INDEX = 3;
    private static final int STATUS_STR_INDEX = 4;
    private static final int BYTE_STR_INDEX = 5;

    private static final int EXPECTED_LEN = 3;

    private static final Logger LOG = LoggerFactory.getLogger(LogParser.class);

    private static final Pattern LOG_PATTERN =
            Pattern.compile("^(\\S+) \\S+ \\S+ \\[([^\\]]+)\\] \"([^\"]*)\" (\\d+) (\\d+) \".*\" \".*\"$");

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);

    public Optional<LogRecord> parse(String line) {
        if (line == null || line.isBlank()) {
            LOG.warn("LogParser   |  warn line");
            return Optional.empty();
        }

        Matcher matcher = LOG_PATTERN.matcher(line);
        if (!matcher.matches()) {
            LOG.warn("LogParser   |  warn matcher");
            return Optional.empty();
        }

        String ip = matcher.group(IP_INDEX);
        String dateString = matcher.group(DATE_STRING_INDEX);
        String request = matcher.group(REQUEST_INDEX);
        String statusStr = matcher.group(STATUS_STR_INDEX);
        String byteStr = matcher.group(BYTE_STR_INDEX);

        int status;
        long byteSent;
        try {
            status = Integer.parseInt(statusStr);
            byteSent = Long.parseLong(byteStr);
        } catch (NumberFormatException e) {
            LOG.warn("LogParser   |  warn ", e);
            return Optional.empty();
        }

        String[] reqParts = request.split(" ");
        if (reqParts.length != EXPECTED_LEN) {
            LOG.warn("LogParser   |  warn reqParts");
            return Optional.empty();
        }

        ZonedDateTime zoneTime;
        try {
            zoneTime = ZonedDateTime.parse(dateString, DATE_FORMATTER);
        } catch (DateTimeException e) {
            LOG.warn("LogParser   |  warn DateTime ", e);
            return Optional.empty();
        }

        return Optional.of(
                new LogRecord(ip, zoneTime.toLocalDate(), reqParts[1], reqParts[1], reqParts[2], status, byteSent));
    }
}
