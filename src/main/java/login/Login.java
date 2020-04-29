package login;

import POJOs.User;
import java.util.ArrayList;
import java.util.Iterator;

public class Login {
    private ArrayList<User> listOfUsers;
    private User user;

    public Login() {
    }

    public Boolean removeUser(User user) {
        if (this.listOfUsers.contains(user)) {
            this.listOfUsers.remove(user);
            return true;
        } else {
            return false;
        }
    }

    public User addUser(String username, String password) {
        return new User(username, password);
    }

    public User login(String username, String password) {
        Iterator var3 = this.listOfUsers.iterator();

        User listOfUser;
        do {
            if (!var3.hasNext()) {
                return null;
            }

            listOfUser = (User)var3.next();
        } while(!listOfUser.getUsername().equals(username) || !listOfUser.getPassword().equals(password));

        return listOfUser;
    }

    public User getCurrentUser() {
        return this.user;
    }
}
