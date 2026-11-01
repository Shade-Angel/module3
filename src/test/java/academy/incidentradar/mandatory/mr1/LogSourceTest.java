package academy.incidentradar.mandatory.mr1;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import academy.incidentradar.support.CliRunner;

/** Обязательные тесты: источники логов. */
@DisplayName("MR1. Источники логов")
class LogSourceTest {

    private static HttpServer server;
    private static int port;

    @BeforeAll
    static void setupServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/ok", exchange -> {
            String log = "80.91.33.133 - - [01/Mar/2025:00:00:33 +0000] \"GET /api/v1/payments HTTP/1.1\" 404 3073 \"-\" \"Debian APT-HTTP/1.3\"\n";
            byte[] bytes = log.getBytes();
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });
        server.createContext("/404", exchange -> {
            exchange.sendResponseHeaders(404, -1);
        });
        server.start();
        port = server.getAddress().getPort();
    }

    @AfterAll
    static void stopServer() {
        if (server != null) server.stop(0);
    }

    @Test
    @DisplayName("Локальный файл читается")
    void readsLocalFile() {
        CliRunner.Result result = CliRunner.run(
            "--path", "samples/logs/access-2025-03-01.log", 
            "--output-format", "json"
        );
        assertThat(result.exitCode()).isEqualTo(0);
    }

    @Test
    @DisplayName("Glob-маска: в отчёт попадают все подошедшие файлы")
    void readsAllFilesMatchingGlob() {
        CliRunner.Result result = CliRunner.run(
            "--path", "samples/logs/*.log", 
            "--output-format", "json"
        );
        assertThat(result.exitCode()).isEqualTo(0);
    }

    @Test
    @DisplayName("Лог читается по URL")
    void readsRemoteFile() {
        CliRunner.Result result = CliRunner.run(
            "--path", "http://localhost:" + port + "/ok", 
            "--output-format", "json"
        );
        assertThat(result.exitCode()).isEqualTo(0);
    }

    @Test
    @DisplayName("URL вернул 404 -> exit code 2")
    void remote404ExitsWithTwo() {
        CliRunner.Result result = CliRunner.run(
            "--path", "http://localhost:" + port + "/404", 
            "--output-format", "json"
        );
        assertThat(result.exitCode()).isEqualTo(2);
    }
}
