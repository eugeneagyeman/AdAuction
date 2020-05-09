package POJOs;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class User {
    String username, password;
    String loginDateTime;
    String type;

    public User(String username, String password, String type) {
        this.username = username;
        this.password = password;
        this.type = type;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime now = LocalDateTime.now();
        String formatDateTime = now.format(formatter);
        loginDateTime = formatDateTime;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getType() { return type; }

}
