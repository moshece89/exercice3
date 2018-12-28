package com.example.onstage.exercice3;

import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
    public static int minYear = 0;
    public static int maxYear = 3000;
    public static int minPrice = 0;
    public static int maxPrice = 50000000;
    public static boolean makeClicked;

    public static void updateIdFirebase(HashMap<String, User> userList) {
        Set keys = userList.keySet();
        Iterator it = keys.iterator();
        Object key = it;
        DatabaseToApplication.userListAuth= new HashMap<>();
        while (it.hasNext()){
            key = it.next();
            Log.d("keykey",key.toString());
            Log.d("keykey",userList.get(key).getIdAuth());
            DatabaseToApplication.userListAuth.put(userList.get(key).getIdAuth(),userList.get(key));

        }
    }
}
