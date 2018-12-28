package model;

import java.util.HashMap;
import java.util.List;

public class Users {
    public Users(HashMap<String,User> userList) {
        this.userList = userList;
    }

    public Users() {
    }

    public HashMap<String,User> getUserList() {
        return userList;
    }

    public void setUserList(HashMap<String,User> userList) {
        this.userList = userList;
    }

    private HashMap<String,User> userList;
}
