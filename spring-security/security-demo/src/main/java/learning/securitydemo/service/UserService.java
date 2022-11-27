package learning.securitydemo.service;

import learning.securitydemo.models.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    //Role saveRole(Role role);
    void addRoleToUser(String username,String roleName);
    User getUser(String username);
    List<User> getUsers();

    //usually when I fetch any kind of data I should fetch for example only page
    // and one page is gonna be for example 25 users
}
