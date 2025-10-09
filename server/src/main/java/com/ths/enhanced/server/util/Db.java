
package com.ths.enhanced.server.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Db {
    private static String url, user, pass;
    public static void init() throws Exception {
        Properties p = new Properties();
        try (InputStream in = Db.class.getClassLoader().getResourceAsStream("application.properties")) {
            p.load(in);
        }
        url = p.getProperty("db.url");
        user = p.getProperty("db.user");
        pass = p.getProperty("db.pass");
        Class.forName("com.mysql.cj.jdbc.Driver");
    }
    public static Connection get() throws Exception {
        return DriverManager.getConnection(url, user, pass);
    }
}
