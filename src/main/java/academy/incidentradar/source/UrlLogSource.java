package academy.incidentradar.source;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Stream;

import org.jspecify.annotations.Nullable;

import academy.incidentradar.CliException;

public class UrlLogSource implements LogSource{
    private final URI uri;

    @Nullable 
    private HttpResponse<Stream<String>> res;

    public  UrlLogSource(URI uri){
        this.uri = uri;
    }

    @Override 
    public Stream<String> lines() throws IOException{
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder(uri).build();
            this.res = client.send(req, HttpResponse.BodyHandlers.ofLines());
            if(res.statusCode() == 404){
                throw new  CliException("URL вернул ошибку 404: " + uri);
            }
            if(res.statusCode() >= 400){
                throw new CliException("URL недоступен, код ошибки: " + res.statusCode() + "  -:- " + uri);
            }
            return  res.body();
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
            throw new IOException("Чтение URL было прервано!", e);
        }
    }

    @Override 
    public void close(){
        if(res != null && res.body() != null){
            res.body().close();
        }
    }
}
