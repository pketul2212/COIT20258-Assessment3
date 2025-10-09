
package com.ths.enhanced.server.net;

import com.google.gson.Gson;
import com.ths.enhanced.server.service.AuthService;
import com.ths.enhanced.server.service.BookingService;
import com.ths.enhanced.server.service.ReminderService;
import com.ths.enhanced.server.service.AvailabilityService;
import java.io.*;
import java.net.Socket;
import java.util.Map;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private static final Gson G = new Gson();

    public ClientHandler(Socket socket) { this.socket = socket; }

    @Override public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            String line;
            while ((line = in.readLine()) != null) {
                Map<?,?> req = G.fromJson(line, Map.class);
                String cmd = (String) req.get("cmd");
                Object body = req.get("body");
                Map<String, Object> res;
                switch (cmd) {
                    case "PING": res = Map.of("ok", true, "msg", "PONG"); break;
                    case "LOGIN": res = AuthService.login((Map<String, Object>) body); break;
                    case "BOOK_APPT": res = BookingService.book((Map<String, Object>) body); break;
                    case "RESCHEDULE": res = BookingService.reschedule((Map<String, Object>) body); break;
                    case "CANCEL": res = BookingService.cancel((Map<String, Object>) body); break;
                    case "LIST_AVAIL": res = AvailabilityService.list((Map<String, Object>) body); break;
                    case "ADD_AVAIL": res = AvailabilityService.add((Map<String, Object>) body); break;
                    case "LIST_REMINDERS": res = ReminderService.list((Map<String, Object>) body); break;
                    default: res = Map.of("ok", false, "error", "Unknown command");
                }
                out.write(G.toJson(res)); out.newLine(); out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
