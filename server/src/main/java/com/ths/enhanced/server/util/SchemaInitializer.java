
package com.ths.enhanced.server.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

public class SchemaInitializer {
    public static void init() throws Exception {
        runSql("db/schema.sql");
        runSql("db/seed.sql");
    }
    private static void runSql(String resource) throws Exception {
        try (Connection c = Db.get(); Statement st = c.createStatement()) {
            String sql = readResource(resource);
            for (String part : sql.split(";\s*\n")) {
                String s = part.trim();
                if (!s.isEmpty()) st.execute(s);
            }
        }
    }
    private static String readResource(String path) throws Exception {
        try (InputStream in = SchemaInitializer.class.getClassLoader().getResourceAsStream(path)) {
            return new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("
"));
        }
    }
}
