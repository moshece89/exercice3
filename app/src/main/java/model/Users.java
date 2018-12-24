package model;

import java.util.List;

public class Users {
    public Users(List<User> userList) {
        this.userList = userList;
    }

    public Users() {
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    private List<User> userList;
}
