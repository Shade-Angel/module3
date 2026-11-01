package academy.incidentradar;

import java.nio.file.Path;
import java.time.LocalDate;

import org.jspecify.annotations.Nullable;

import picocli.CommandLine.Option;

public class Configuration {
    @Option(names = {"-p", "--path"}, required = true, description = "файл, glob-маска или URL")
    private  String path;

    @Option(names = {"-f", "--output-forma"}, defaultValue = "markdown", description="json или markdown")
    private String outputForma;

    @Option(names = {"0", "--output-path"}, description = "путь файла")
    @Nullable 
    private Path outputPath;

    @Option(names = {"--from"}, description = "когда начинается")
    @Nullable 
    private LocalDate from;

    @Option(names = {"--to"}, description = "когда заканчиватеся")
    private  LocalDate to;

    public String getPath(){
        return path;
    }

    public String getOutputFormat(){
        return outputForma;
    }

    @Nullable 
    public Path getOutputPath(){
        return outputPath;
    }

    @Nullable 
    public LocalDate getFrom(){
        return from;
    }

    @Nullable 
    public LocalDate getTo(){
        return to;
    }
}
