package academy.incidentradar.source;

import java.io.IOException;
import java.util.stream.Stream;

public interface LogSource extends AutoCloseable {
    Stream<String> lines() throws IOException;

    @Override
    void close();
}
