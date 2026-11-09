package academy.incidentradar.parser;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import academy.incidentradar.model.LogRecord;

public class LogParser {
    private  static  final Logger LOG = LoggerFactory.getLogger(LogParser.class);

    private  static final Pattern LOG_PATTERN = Pattern.compile(
        "^(\\S+) \\S+ \\S+ \\[([^\\]]+)\\] \"([^\"]*)\" (\\d+) (\\d+) \".*\" \".*\"$"
    );

    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);

    public Optional<LogRecord> parse(String line){
        if(line == null || line.isBlank()){
            LOG.warn("LogParser   |  warn line");
            return Optional.empty();
        }

        Matcher matcher = LOG_PATTERN.matcher(line);
        if(!matcher.matches()){
            LOG.warn("LogParser   |  warn matcher");
            return Optional.empty();
        }

        String ip = matcher.group(1);
        String dateString = matcher.group(2);
        String request = matcher.group(3);
        String statusStr = matcher.group(4);
        String byteStr = matcher.group(5);

        int status;
        long byteSent;
        try{
            status = Integer.parseInt(statusStr);
            byteSent = Long.parseLong(byteStr);
        }catch(NumberFormatException e){
            LOG.warn("LogParser   |  warn ", e);
            return Optional.empty();
        }

        String[] reqParts = request.split(" ");
        if(reqParts.length != 3){
            LOG.warn("LogParser   |  warn reqParts");
            return Optional.empty();
        }

        ZonedDateTime zoneTime;
        try{
            zoneTime = ZonedDateTime.parse(dateString, DATE_FORMATTER);
        }catch(Exception e){
            LOG.warn("LogParser   |  warn DateTime ", e);
            return Optional.empty();
        }

        return Optional.of(new LogRecord(
            ip,
            zoneTime.toLocalDate(),
            reqParts[1],
            reqParts[1],
            reqParts[2],
            status,
            byteSent
        ));
    }
}
