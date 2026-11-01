package academy.incidentradar.source;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;

import academy.incidentradar.CliException;

public class LogSourceFactory {
    public  static LogSource create(String path){
        if(path.startsWith("http://") || path.startsWith("https://")){
            try{
                return new UrlLogSource(new URI(path));
            }catch(URISyntaxException e){
                throw new CliException("Неккоректный URL, сейчас он равен :  " + path);
            }
        }

        if(path.contains("*") || path.contains("?")){
            return new GlobLogSource(path);
        }

        return new FileLogSource(Path.of(path));
    }
}
