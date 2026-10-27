package academy.incidentradar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Точка входа в Incident Radar. */
public final class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private Main() {}

    public static void main(String[] args) {
        LOG.debug("Аргументы запуска: {}", String.join(" ", args));

        // TODO: разобрать аргументы и запустить анализ.
        System.out.println("Incident Radar — шаблон проекта Т-Академии.");
        System.out.println("Ничего ещё не реализовано. Начни с README.");
    }
}
