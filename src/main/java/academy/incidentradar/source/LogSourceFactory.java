package academy.incidentradar.source;

import academy.incidentradar.CliException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;

public final class LogSourceFactory {
    private LogSourceFactory() {}

    public static LogSource create(String pathSpec) {
        if (pathSpec != null && pathSpec.startsWith("\"") && pathSpec.endsWith("\"") && pathSpec.length() >= 2) {
            pathSpec = pathSpec.substring(1, pathSpec.length() - 1);
        }

        if (pathSpec.startsWith("http://") || pathSpec.startsWith("https://")) {
            try {
                return new UrlLogSource(new URI(pathSpec));
            } catch (URISyntaxException e) {
                throw new CliException("Некорректный URL: " + pathSpec);
            }
        }

        if (pathSpec.contains("*") || pathSpec.contains("?")) {
            return new GlobLogSource(pathSpec);
        }

        return new FileLogSource(Path.of(pathSpec));
    }
}
