package login;

import POJOs.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Login {
    private HashMap<User,String> users;
    private User user;
    public HashMap<User,String> getUsers() { return users; }
    public User getCurrentUser() {
        return this.user;
    }
    public int getUserID() {
        ArrayList<User> IDs = new ArrayList<>();
        IDs.addAll(users.keySet());
        return IDs.indexOf(user)+1;
    }

    public Login() {
        users = new HashMap<>();
        user = addUser("admin", "Admin2020", "admin");
    }

    public User addUser(String username, String password, String type) {
        User user = new User(username, password, type);
        if (username.equals("") || password.equals("") || password.length() < 8 || password.length() > 16 || !password.matches(".*\\d.*") || !password.matches(".*[A-Z].*"))
            return null;
        for (User u : users.keySet()) {
            if (username.equals(u.getUsername()))
                return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:");
        users.put(user,LocalDateTime.now().format(formatter));
        return user;
    }

    public Boolean removeUser(User user) {
        if (this.users.keySet().contains(user)) {
            this.users.remove(user);
            return true;
        } else {
            return false;
        }
    }

    public User login(String username, String password, String type) {
        Iterator iterator = this.users.keySet().iterator();
        User user;
        do {
            if (!iterator.hasNext()) {
                return null;
            }
            user = (User)iterator.next();
        } while(!user.getUsername().equals(username) || !user.getPassword().equals(password) || !user.getType().equals(type));
        this.user = user;
        return user;
    }

    public void logout() {
        user = null;
    }
}
