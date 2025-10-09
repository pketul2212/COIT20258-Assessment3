
package com.ths.enhanced.server;

import com.ths.enhanced.server.util.Db;
import com.ths.enhanced.server.util.SchemaInitializer;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    public static void main(String[] args) throws Exception {
        Db.init();
        SchemaInitializer.init();
        int port = Integer.parseInt(System.getProperty("server.port", "5050"));
        ExecutorService pool = Executors.newCachedThreadPool();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("[SERVER] Listening on port " + port);
            while (true) {
                Socket client = serverSocket.accept();
                pool.submit(new com.ths.enhanced.server.net.ClientHandler(client));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
