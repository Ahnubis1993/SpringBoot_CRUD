package org.educa.airline.repository;

import org.educa.airline.entity.User;

import java.util.List;

public interface UserRepository {


    User getUser(String email);

    List<User> getUsers();

    void createUser(User user);

    void deleteUser(String username);

    void updateUser(User user);

    boolean existUser(String username);
}
