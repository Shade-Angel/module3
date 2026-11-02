package academy.incidentradar;

import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.IExecutionExceptionHandler;
import picocli.CommandLine.ParseResult;

/** Точка входа в Incident Radar. */
@Command(name = "incident-radar", mixinStandardHelpOptions = true)
public final class Main implements Callable<Integer> {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    @CommandLine.Mixin
    private Configuration config = new Configuration();

    private Main() {}

    public static void main(String[] args) {
        LOG.debug("Аргументы запуска: {}", String.join(" ", args));

        if (args.length == 0) {
            System.out.println("Incident Radar — шаблон проекта Т-Академии.");
            System.out.println("Ничего ещё не реализовано. Начни с README.");
            System.exit(0);
            return;
        }

        CommandLine cmd = new CommandLine(new Main());
        cmd.setExecutionExceptionHandler(new ExceptionHandler());
        int exitCode = cmd.execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        LOG.debug("Аргументы запуска: {}", config);
        App app = new App(config);
        app.run();
        return 0;
    }

    private static final class ExceptionHandler implements IExecutionExceptionHandler {
        @Override
        public int handleExecutionException(Exception ex, CommandLine cmd, ParseResult parse) {
            if (ex instanceof CliException) {
                cmd.getErr().println("Error: " + ex.getMessage());
                return 2;
            }
            cmd.getErr().println("No found Error:" + ex.getMessage());
            ex.printStackTrace(cmd.getErr());
            return 1;
        }
    }
}
