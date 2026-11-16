package academy.incidentradar.source;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public interface LogSource extends AutoCloseable {
    Stream<String> lines() throws IOException;

    List<String> getFiles();

    @Override
    void close();
}
