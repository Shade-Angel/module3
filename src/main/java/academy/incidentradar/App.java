package academy.incidentradar;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import academy.incidentradar.source.LogSource;
import academy.incidentradar.source.LogSourceFactory;

public class App {
    private final Configuration config;

    public App(Configuration config){
        this.config = config;
    }

    public  void run(){
        validate();
        try(LogSource source = LogSourceFactory.create(config.getPath())){
            try(var lines = source.lines()){
                long count = lines.count();
                System.out.println("Успшно прочитан : " + count + "строк.");
            }
        }catch(IOException e){
                throw new  RuntimeException("Ошибка чтения: " + e.getMessage(), e);
        }
    }

    private  void validate(){
        String format = config.getOutputFormat().toLowerCase();
        if(!"json".equals(format) && !"markdown".equals(format)){
            throw new CliException("Вы ввели непправильный формат --output-format, возможны json или markdown");
        }

        if(config.getFrom() != null && config.getTo() != null){
            if(!config.getFrom().isBefore(config.getTo())){
                throw new CliException("--from должен быть раньше --to");
            }
        }

        Path output = config.getOutputPath();
        if(output != null){
            String ext = getFileExtension(output.getFileName().toString()).toLowerCase();
            if("json".equals(format) && !"json".equals(ext)){
                throw new CliException("Расширение выходного файла не является json.");
            }
            if("markdown".equals(format) && !"md".equals(ext) && !"markdown".equals(ext)){
                throw new CliException("Расширение выходного файла не является markdown.");
            }
            if(Files.exists(output)){
                throw new  CliException("Файл отчёта уже есть. Он -  " + output);
            }
        }
    }

    private String getFileExtension(String name){
        int last = name.lastIndexOf(".");
        return last == -1 ? "" : name.substring(last + 1);
    }
}
