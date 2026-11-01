package academy.incidentradar.source;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import academy.incidentradar.CliException;

public class FileLogSource implements LogSource{
    private final Path path;

    public FileLogSource(Path path){
        if(!Files.exists(path)){
            throw new CliException("Файл не найден по адресу : " + path);
        }
        String name = path.getFileName().toString().toLowerCase();
        if(!name.endsWith(".log") && !name.endsWith(".txt")){
            throw new CliException("Неподдерживаемые расширения файлов : " + name);
        }
        this.path = path;
    }

    @Override 
    public Stream<String> lines() throws IOException{
        return Files.lines(path);
    }

    @Override 
    public void close(){}
}
