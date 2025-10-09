
package com.ths.enhanced.client.net;

import com.google.gson.Gson;
import java.io.*;
import java.net.Socket;
import java.util.Map;

public class NetClient {
    private final String host; 
    private final int port;
    private Socket socket; 
    private BufferedReader in; 
    private BufferedWriter out;
    private static final Gson G = new Gson();

    public NetClient(String host, int port){ 
        this.host=host; 
        this.port=port; 
    }

    public void connect() throws Exception {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public Map<?,?> send(String cmd, Map<String,Object> body) throws Exception {
        String json = G.toJson(Map.of("cmd", cmd, "body", body));
        out.write(json); out.newLine(); out.flush();
        String line = in.readLine();
        return G.fromJson(line, Map.class);
    }

    public void close() throws Exception { 
        socket.close(); 
    }
}
