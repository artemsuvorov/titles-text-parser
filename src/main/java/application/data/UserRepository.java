package application.data;

import application.models.User;

import java.util.Hashtable;
import java.util.Map;

public class UserRepository {

    private static final Map<String, User> users = new Hashtable<String, User>();

    static {
        users.put("Admin", new User("Admin", 999));
        users.put("Dmitriy", new User("Dmitriy", 26));
        users.put("Vladimir", new User("Vladimir", 35));
    }

    public User getUserOrNull(String name) {
        return users.get(name);
    }

    public void addOrUpdateUser(User user) {
        users.put(user.getName(), user);
    }

}