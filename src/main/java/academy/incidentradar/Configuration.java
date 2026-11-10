package academy.incidentradar;

import java.nio.file.Path;
import java.time.LocalDate;
import org.jspecify.annotations.Nullable;
import picocli.CommandLine.Option;

public class Configuration {
    @Option(
            names = {"-p", "--path"},
            required = true,
            description = "файл, glob-маска или URL")
    @Nullable
    private String path;

    @Option(
            names = {"-f", "--output-format"},
            defaultValue = "markdown",
            description = "json или markdown")
    @Nullable
    private String outputFormat;

    @Option(
            names = {"-o", "--output-path"},
            description = "путь файла")
    @Nullable
    private Path outputPath;

    @Option(
            names = {"--from"},
            description = "когда начинается")
    @Nullable
    private LocalDate from;

    @Option(
            names = {"--to"},
            description = "когда заканчиватеся")
    @Nullable
    private LocalDate to;

    @Nullable
    public String getPath() {
        return path;
    }

    @Nullable
    public String getOutputFormat() {
        return outputFormat;
    }

    @Nullable
    public Path getOutputPath() {
        return outputPath;
    }

    @Nullable
    public LocalDate getFrom() {
        return from;
    }

    @Nullable
    public LocalDate getTo() {
        return to;
    }
}
