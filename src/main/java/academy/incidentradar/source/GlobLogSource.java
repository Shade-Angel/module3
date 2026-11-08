package academy.incidentradar.source;

import academy.incidentradar.CliException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GlobLogSource implements LogSource {
    private final String glob;
    private static final String STAR = "*";
    private static final String QUESTION = "?";
    private static final String SLASH = "/";
    private static final String BACKSLASH = "\\";
    private static final String CURRENT_DIR = ".";
    private static final int CODE_MINUS_ONE = -1;
    private List<Path> files = new ArrayList<>();

    public GlobLogSource(String glob) {
        this.glob = glob;
    }

    @Override
    @SuppressWarnings("StreamResourceLeak")
    public Stream<String> lines() throws IOException {
        // 1. Приводим входящую маску к единому виду с прямыми слешами
        String normalizedGlob = glob.replace(BACKSLASH, SLASH);

        // 2. Адаптируем маску под текущую ОС
        // На Windows разделитель путей — '\'. Но в синтаксисе glob символ '\' — это экранирование.
        // Чтобы PathMatcher искал буквальный '\', в строке паттерна должно быть написано '\\'.
        // На Linux разделитель '/', и его экранировать не нужно.
        String systemGlob;
        if (FileSystems.getDefault().getSeparator().equals(BACKSLASH)) {
            systemGlob = normalizedGlob.replace(SLASH, BACKSLASH + BACKSLASH);
        } else {
            systemGlob = normalizedGlob;
        }

        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + systemGlob);

        Path baseDir = getBaseDir(glob);
        if (!Files.exists(baseDir)) {
            throw new CliException("Директория для glob-маски не найдена: " + baseDir.toAbsolutePath());
        }

        Path currentDir = Paths.get(CURRENT_DIR).toAbsolutePath().normalize();
        Path absoluteBaseDir = baseDir.toAbsolutePath().normalize();

        try (Stream<Path> walk = Files.walk(absoluteBaseDir)) {
            files = walk.filter(Files::isRegularFile)
                    .filter(file -> {
                        Path relativePath =
                                currentDir.relativize(file.toAbsolutePath().normalize());
                        return matcher.matches(relativePath);
                    })
                    .collect(Collectors.toList());
        }

        if (files.isEmpty()) {
            throw new CliException("По glob-маске не найдено файлов: " + glob);
        }

        for (Path p : files) {
            String name = p.getFileName().toString().toLowerCase();
            if (!name.endsWith(".log") && !name.endsWith(".txt")) {
                throw new CliException("Неподдерживаемое расширение файла: " + name);
            }
        }

        return files.stream().flatMap(p -> {
            try {
                return Files.lines(p);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }

    private Path getBaseDir(String globPattern) {
        int startIdx = globPattern.indexOf(STAR);
        int qIdx = globPattern.indexOf(QUESTION);
        int min = CODE_MINUS_ONE;

        if (startIdx >= 0) {
            min = startIdx;
        }
        if (qIdx >= 0 && (min == CODE_MINUS_ONE || qIdx < min)) {
            min = qIdx;
        }

        if (min == CODE_MINUS_ONE) {
            Path p = Paths.get(globPattern);
            return p.getParent() == null ? Paths.get(CURRENT_DIR) : p.getParent();
        }

        String before = globPattern.substring(0, min);
        int lastSep = Math.max(before.lastIndexOf(SLASH), before.lastIndexOf(BACKSLASH));

        if (lastSep == CODE_MINUS_ONE) {
            return Paths.get(CURRENT_DIR);
        }
        return Paths.get(before.substring(0, lastSep));
    }

    @Override
    public void close() {}
}
