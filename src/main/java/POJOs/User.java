package POJOs;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class User {
    String username;
    byte[] hash;
    String loginDateTime;
    String type;

    public User(String username, byte[] hash, String type) {
        this.username = username;
        this.hash = hash;
        this.type = type;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime now = LocalDateTime.now();
        String formatDateTime = now.format(formatter);
        loginDateTime = formatDateTime;
    }

    public String getUsername() {
        return username;
    }

    public byte[] getHash() { return hash; }

    public String getType() { return type; }

}
