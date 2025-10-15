package infra;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * Minimal multi-threaded server to meet the assignment's concurrency requirement.
 * Protocol (very simple text-based):
 *   - "PING" -> "PONG"
 *   - "COUNT_APPTS" -> returns a number
 * Extend with JSON lines for real endpoints.
 */
public final class Server {

    private final int port;

    public Server(int port) {
        this.port = port;
    }

    public void startAsync() {
        var pool = Executors.newCachedThreadPool();
        pool.submit(() -> {
            try (ServerSocket server = new ServerSocket(port)) {
                System.out.println("THS server listening on " + port);
                while (true) {
                    Socket client = server.accept();
                    pool.submit(() -> handle(client));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void handle(Socket client) {
        try (client;
             BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true)) {

            String line = in.readLine();
            if (line == null) return;
            switch (line.trim()) {
                case "PING" -> out.println("PONG");
                case "COUNT_APPTS" -> {
                    // Ideally call the service here; keeping it minimal
                    out.println("0");
                }
                default -> out.println("ERR:UNKNOWN_CMD");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
