package com.example.onstage.exercice3;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.User;

public class Sign_Up extends AppCompatActivity {
    public static final String TAG = "Sign_Up";
    public String m_password;
    public String m_email;
    private FirebaseAuth m_Auth;

    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate() >>");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        m_Auth = FirebaseAuth.getInstance();
        Log.e(TAG, "onCreate() >>");

    }

    @Override
    public void onStart() {
        Log.e(TAG, "onStart() >>");

        super.onStart();

        FirebaseUser currentUser = m_Auth.getCurrentUser();
        Log.e(TAG, "onStart() >>");

        // updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        Log.e(TAG, "updateUI() >>");

        if (currentUser == null) {
            Toast message = new Toast(this);
            message.makeText(this, "error system your autodestruction is immediate bye", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getApplicationContext(), MyStorageProduct.class);
            intent.putExtra(Constants.WAY_OF_CONNECTION, Constants.Email);
            startActivity(intent);
        }
        Log.e(TAG, "updateUI() >>");

    }


    private void createNewUser() {
        Log.e(TAG, "createNewUser() >>");

        m_Auth.createUserWithEmailAndPassword(m_email, m_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = m_Auth.getCurrentUser();
                            User user1 = new User();
                            user1.setIdAuth(user.getUid());
                            user1.setId(DatabaseToApplication.userList.size()+1);
                            String key = DatabaseToApplication.mDatabase.getReference(Constants.USERS).push().getKey();
                            DatabaseToApplication.mDatabase.getReference(Constants.USERS).child(key).setValue(user1);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, task.toString(), task.getException());
                            Toast.makeText(Sign_Up.this, Constants.AUTHENTICATION_FAILED_MESSAGE, Toast.LENGTH_SHORT).show();
                            FirebaseUser user = m_Auth.getCurrentUser();
                            updateUI(user);
                        }

                        // ...
                    }
                });
        Log.e(TAG, "createNewUser() >>");

    }

    public void onClick(View button) {
        Log.e(TAG, "onClick() >>");

        m_email = ((TextView) findViewById(R.id.editText_Email)).getText().toString();
        m_password = ((TextView) findViewById(R.id.editText_Passworld)).getText().toString();

        Toast message = new Toast(this);

        if (Sign_In.verifyEmail(m_email))
        {
            if (verifyPassword())
            {
                if (verifyPasswordIdentical())
                {
                    createNewUser();
                }
                else
                {
                    message.makeText(this, Constants.WRONG_PASSSWORD_MESSAGE, Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                message.makeText(this, Constants.INVALID_PASSWORD_FORMAT, Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            message.makeText(this, Constants.INVALID_EMAIL, Toast.LENGTH_LONG).show();
        }
        Log.e(TAG, "onClick() >>");

    }

    public boolean verifyPassword() {
        Log.e(TAG, "verifyPassword() >>");

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=.,!])(?=\\S+$).{6,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(m_password);

        Log.e(TAG, "verifyPassword() >>");

        return matcher.matches();

    }

    public boolean verifyPasswordIdentical() {
        Log.e(TAG, "verifyPasswordIdentical() >>");


        String confirmPassword;
        boolean identical = false;

        confirmPassword = ((TextView) findViewById(R.id.editText_ConfirmPassworld)).getText().toString();

        if (m_password.compareTo(confirmPassword) == 0) {
            identical = true;
        }
        Log.e(TAG, "verifyPasswordIdentical() >>");

        return identical;
    }


}
