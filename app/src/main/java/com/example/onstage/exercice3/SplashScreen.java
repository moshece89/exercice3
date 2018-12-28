package com.example.onstage.exercice3;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

public class SplashScreen extends AppCompatActivity {
    private Handler m_Handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DatabaseToApplication.mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mReferenceCars = DatabaseToApplication.mDatabase.getReference("cars");
        DatabaseReference mReferenceUsers = DatabaseToApplication.mDatabase.getReference("users");
        DatabaseReference mReferenceComments = DatabaseToApplication.mDatabase.getReference("comments");

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mReferenceCars.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
        GenericTypeIndicator<List<Car>> genericTypeIndicator =new GenericTypeIndicator<List<Car>>(){};
        DatabaseToApplication.carList = dataSnapshot.getValue(genericTypeIndicator);
        DatabaseToApplication.myCars = new Cars(DatabaseToApplication.carList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mReferenceUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String,User>> genericTypeIndicator =new GenericTypeIndicator<HashMap<String, User>>(){};
                //GenericTypeIndicator<List<User>> genericTypeIndicator =new GenericTypeIndicator<List<User>>(){};
                //DatabaseToApplication.userslist =dataSnapshot.getValue(genericTypeIndicator);
                DatabaseToApplication.userList = dataSnapshot.getValue(genericTypeIndicator);
                updateIdFirebase(DatabaseToApplication.userList);
                DatabaseToApplication.users = new Users(DatabaseToApplication.userList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mReferenceComments.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String,CommentUser>> genericTypeIndicator =new GenericTypeIndicator<HashMap<String, CommentUser>>(){};
                DatabaseToApplication.commentUserList = dataSnapshot.getValue(genericTypeIndicator);
                //DatabaseToApplication.comments = new Comments(DatabaseToApplication.commentUserList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        super.onCreate(savedInstanceState);
        printkeyHash();
        setContentView(R.layout.activity_splash_screen);
        m_Handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), Sign_In.class);
                startActivity(intent);
            }
        }, 3000);
    }

    private void updateIdFirebase(HashMap<String, User> userList) {
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

    private void printkeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.onstage.exercice3", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("Hkey", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
