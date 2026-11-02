package academy.incidentradar;

import academy.incidentradar.source.LogSource;
import academy.incidentradar.source.LogSourceFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Objects;

public class App {
    private final Configuration config;

    public App(Configuration config) {
        this.config = config;
    }

    public void run() {

        String path = Objects.requireNonNull(config.getPath(), "Не указан обязательный параметр --path");

        validateForma();
        validateDate();
        validateOutput();

        try (LogSource source = LogSourceFactory.create(path)) {
            try (var lines = source.lines()) {
                long count = lines.count();
                System.out.println("Успшно прочитан : " + count + "строк.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения: " + e.getMessage(), e);
        }
    }

    private void validateForma() {
        String format =
                config.getOutputFormat() != null ? config.getOutputFormat().toLowerCase() : "markdown";
        if (!"json".equals(format) && !"markdown".equals(format)) {
            throw new CliException("Вы ввели непправильный формат --output-format, возможны json или markdown");
        }
    }

    private void validateDate() {
        LocalDate from = config.getFrom();
        LocalDate to = config.getTo();
        if (from != null && to != null && !from.isBefore(to)) {
            throw new CliException("Вы ввели непправильный формат --output-format, возможны json или markdown");
        }
    }

    private void validateOutput() {
        Path output = config.getOutputPath();
        if (output == null) {
            System.out.print("output == null");
            return;
        }
        String format = currentFormat();
        String ext = getFileExtension(output.getFileName().toString());
        if ("json".equals(format) && !"json".equals(ext)) {
            throw new CliException("Расширение выходного файла не является json.");
        }
        if ("markdown".equals(format) && !"md".equals(ext) && !"markdown".equals(ext)) {
            throw new CliException("Расширение выходного файла не является markdown.");
        }
        if (Files.exists(output)) {
            throw new CliException("Файл отчёта уже есть. Он -  " + output);
        }
    }

    private String currentFormat() {
        String raw = config.getOutputFormat();
        return raw == null ? "markdown" : raw.toLowerCase();
    }

    private String getFileExtension(String name) {
        int last = name.lastIndexOf(".");
        return last == -1 ? "" : name.substring(last + 1);
    }
}
