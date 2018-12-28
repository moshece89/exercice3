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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up);

        m_Auth = FirebaseAuth.getInstance();

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = m_Auth.getCurrentUser();
        // updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser == null) {
            Toast message = new Toast(this);
            message.makeText(this, "error system your autodestruction is immediate bye", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getApplicationContext(), MyStorageProduct.class);
            intent.putExtra(Constants.WAY_OF_CONNECTION, Constants.Email);
            startActivity(intent);
        }
    }


    private void createNewUser() {


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
                            String key = DatabaseToApplication.mDatabase.getReference("users").push().getKey();
                            DatabaseToApplication.mDatabase.getReference("users").child(key).setValue(user1);
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
    }

    public void onClick(View button) {
        m_email = ((TextView) findViewById(R.id.editText_Email)).getText().toString();
        m_password = ((TextView) findViewById(R.id.editText_Passworld)).getText().toString();

        Toast message = new Toast(this);

        if (verifyEmail(m_email))
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
    }

    public boolean verifyPassword() {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=.,!])(?=\\S+$).{6,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(m_password);

        return matcher.matches();

    }

    public boolean verifyPasswordIdentical() {

        String confirmPassword;
        boolean identical = false;

        confirmPassword = ((TextView) findViewById(R.id.editText_ConfirmPassworld)).getText().toString();

        if (m_password.compareTo(confirmPassword) == 0) {
            identical = true;
        }

        return identical;
    }

    public boolean verifyEmail(String mail) {
        Log.e(TAG, "verify m_email >>");

        Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
        );
        Log.e(TAG, "verify m_email <<");

        return EMAIL_ADDRESS_PATTERN.matcher(mail).matches();
    }
}
