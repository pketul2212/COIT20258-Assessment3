package infra;

import java.sql.Connection;
import java.sql.DriverManager;

public final class Database {
    private Database() {}

    // TODO: externalize into a properties file if you prefer
    private static final String URL  = "jdbc:mysql://localhost:3306/ths?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "ths_user";
    private static final String PASS = "ths_pass";

    public static Connection get() throws Exception {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
