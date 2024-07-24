package org.educa.airline.repository.inmemory;

import org.educa.airline.entity.User;
import org.educa.airline.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserRepository implements UserRepository {

    private final Map<String, User> users = new HashMap<>();

    @Override
    public User getUser(String username) {
        return users.get(username);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void createUser(User user) {
        users.put(user.getUsername(), user);
    }

    @Override
    public void deleteUser(String username) {
        users.remove(username);
    }

    @Override
    public void updateUser(User user) {
        users.replace(user.getUsername(), user);
    }

    @Override
    public boolean existUser(String username) {
        return users.containsKey(username);
    }
}
