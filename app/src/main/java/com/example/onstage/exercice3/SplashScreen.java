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
import java.util.List;

import model.Car;
import model.Cars;
import model.CommentUser;
import model.User;

public class SplashScreen extends AppCompatActivity {
    private Handler m_Handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mReference = mDatabase.getReference("cars");

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long value=dataSnapshot.getChildrenCount();
        GenericTypeIndicator<List<Car>> genericTypeIndicator =new GenericTypeIndicator<List<Car>>(){};
        Constants.carList = dataSnapshot.getValue(genericTypeIndicator);
        Constants.myCars = new Cars(Constants.carList);
        /*GenericTypeIndicator<List<User>> genericTypeIndicatorc =new GenericTypeIndicator<List<User>>(){};
        Constants.userList = dataSnapshot.getValue(genericTypeIndicatorc);

        GenericTypeIndicator<List<CommentUser>> genericTypeIndicators =new GenericTypeIndicator<List<CommentUser>>(){};
        Constants.commentUserList = dataSnapshot.getValue(genericTypeIndicators);*/

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
