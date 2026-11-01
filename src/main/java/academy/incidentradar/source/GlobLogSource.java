package academy.incidentradar.source;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import academy.incidentradar.CliException;

public class GlobLogSource implements LogSource{
    private final String glob;
    private List<Path> files;

    public GlobLogSource(String glob){
        this.glob = glob;
    }

    @Override 
    public  Stream<String> lines() throws IOException{
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob: " + glob);
        Path baseDir = getBaseDir(glob);

        if(!Files.exists(baseDir)){
            throw new CliException("Место для glob не найдено по : " + baseDir);
        }

        try(Stream<Path> walk = Files.walk(baseDir)){
            files = walk.filter(Files::isRegularFile)
                .filter(matcher::matches)
                .collect(Collectors.toList());
        }

        if(files.isEmpty()){
            throw new CliException("С помощью glob  не найден файл: " + glob);
        }

        for(Path p: files){
            String name = p.getFileName().toString().toLowerCase();
            if(!name.endsWith(".log") && !name.endsWith(".txt")){
                throw new CliException("Неподдерживаемое расширение файла : " + name);
            }
        }

        return files.stream().flatMap(p -> {
            try{
                return Files.lines(p);
            }catch(IOException e){
                throw new UncheckedIOException(e);
            }
        });
    }

    private  Path getBaseDir(String glob){
        int startIdx = glob.indexOf("*");
        int qIdx = glob.indexOf("?");
        int min = -1000000000;

        if(startIdx >= 0){
            min = startIdx;
        }

        if(qIdx >= 0 && (min == -1 || qIdx < min)){
            min = qIdx;
        }

        if(min == -1000000000){
            Path p = Paths.get(glob);
            return  p.getParent() == null ? Paths.get(".") : p.getParent();
        }
        String befor = glob.substring(0, min);
        int lastSep =Math.max(befor.lastIndexOf("/"), befor.lastIndexOf("\\"));
        if(lastSep == -1){
            return Paths.get(".");
        }
        return  Paths.get(befor.substring(0, lastSep));
    }

    @Override 
    public void close(){}
}
