package Login;

import POJOs.User;

import java.util.ArrayList;

public class Login {
    private ArrayList<User> listOfUsers;
    private User user;

    public Login() {

    }

    public Boolean removeUser(User user) {
        if (listOfUsers.contains(user)) {
            listOfUsers.remove(user);
            return true;
        }
        return false;
    }

    public User addUser(String username, String password) {
        return new User(username, password);
    }

    public User login(String username, String password) {
        for (User listOfUser : listOfUsers) {
            if (listOfUser.getUsername().equals(username)) {
                if (listOfUser.getPassword().equals(password)) return listOfUser;
            }
        }
        return null;
    }

    public User getCurrentUser() {
        return user;
    }
}
