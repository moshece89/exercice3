package com.example.onstage.exercice3;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

import model.Car;
import model.Cars;
import model.CommentUser;
import model.Comments;
import model.User;
import model.Users;

public class DatabaseToApplication {
    public static List<Car> carList;
    public static HashMap<String,User> userList;
    public static HashMap<String,User> userListAuth ;
    public static List<User> userslist;
    public static HashMap<String,CommentUser> commentUserList;
    public static Cars myCars;
    public static FirebaseDatabase mDatabase;
    public static Users users;
    public static Comments comments;
}
