package com.example.onstage.exercice3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.User;

public class Sign_In extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;

    public static final String TAG = "Sign_InComment";
    public String m_password;
    public String m_email;
    private SignInButton mGoogleSignInButton;
    private FirebaseRemoteConfig anonymous = FirebaseRemoteConfig.getInstance();
    private Button mAnnonymosSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign__in);

        anonymous.setConfigSettings(new FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(true).build());

        HashMap<String, Object> defaults = new HashMap<>();
        defaults.put("anonymous_login_enabled", true);

        anonymous.activateFetched();
        //updateButtonVisibility();

        mAnnonymosSignin = findViewById(R.id.button_anonymousButton);
        // Facebook Login
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

        LoginButton mFacebookSignInButton = (LoginButton) findViewById(R.id.facebook_Button);
        mFacebookSignInButton.setReadPermissions("email", "public_profile", "user_birthday", "user_friends");

        mFacebookSignInButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                firebaseAuthWithFacebook(loginResult.getAccessToken());
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                User user1 = new User();
                if (user != null) {
                    user1.setIdAuth(user.getUid());

                    String key = DatabaseToApplication.mDatabase.getReference("users").push().getKey();
                    DatabaseToApplication.mDatabase.getReference("users").child(key).setValue(user1);
                }
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });

        // Google Sign-In
        // Assign fields
        mGoogleSignInButton = findViewById(R.id.google_sign_in_button);

        // Set click listeners
        mGoogleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, (GoogleApiClient.OnConnectionFailedListener) this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Intent intent = new Intent(getApplicationContext(),MyStorageProduct.class);
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    startActivity(intent);

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    private void updateButtonVisibility() {
        boolean show = anonymous.getBoolean("anonymous_login_enabled");
        if (show) {
            findViewById(R.id.button_anonymousButton).setVisibility(View.VISIBLE);

        } else {
            findViewById(R.id.button_anonymousButton).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void onClickAnonymos(View V) {
        mAuth.signInAnonymously();
        FirebaseUser user = mAuth.getCurrentUser();
        User user1 = new User();
        if (user != null) {
            user1.setIdAuth(user.getUid());
            user1.setId(DatabaseToApplication.userList.size() + 1);
            String key = DatabaseToApplication.mDatabase.getReference("users").push().getKey();
            DatabaseToApplication.mDatabase.getReference("users").child(key).setValue(user1);
        }
        updateUI(user, "anonymous");
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGooogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(Sign_In.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(Sign_In.this, MyStorageProduct.class));
                            finish();
                        }
                    }
                });
    }

    private void firebaseAuthWithFacebook(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(Sign_In.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(Sign_In.this, MyStorageProduct.class));
                            finish();
                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed
                Log.e(TAG, "Google Sign In failed.");
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    //----------------------------------- email---------------------------------------------------------
    public void onClickSignInEmail(View V) {

        Log.d(TAG, "sign Email <<<");

        m_email = ((TextView) findViewById(R.id.editText_Email)).getText().toString();
        m_password = ((TextView) findViewById(R.id.editText_EnterPassworld)).getText().toString();

        Toast message = new Toast(this);

        if (verifyEmail(m_email)) {
            if (verifyPassword()) {
                SignIEmail();

            } else {
                message.makeText(this, Constants.INVALID_PASSWORD_FORMAT, Toast.LENGTH_LONG).show();
            }
        } else {
            message.makeText(this, Constants.INVALID_EMAIL, Toast.LENGTH_LONG).show();
        }

        Log.d(TAG, "Sign Email <<<");
    }

    public void onClickResetPassword(View V) {
        m_email = ((TextView) findViewById(R.id.editText_Email)).getText().toString();
        Toast message = new Toast(this);

        if (verifyEmail(m_email)) {
            mAuth.sendPasswordResetEmail(m_email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Sign_In.this, Constants.SEND_EMAIL_RESET, Toast.LENGTH_SHORT).show();
                                // do something when mail was sent successfully.
                            } else {
                                Toast.makeText(Sign_In.this, Constants.DONT_REGISTER, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            message.makeText(this, Constants.INVALID_EMAIL, Toast.LENGTH_LONG).show();
        }

    }

    private void SignIEmail() {

        Log.d(TAG, "Sign Email private >>>");

        mAuth.signInWithEmailAndPassword(m_email, m_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user, Constants.Email);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, task.toString(), task.getException());
                            Toast.makeText(Sign_In.this, Constants.AUTHENTICATION_SIGN_IN_FAILED_MESSAGE, Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user, Constants.Email);
                        }

                        // ...
                    }
                });

        Log.d(TAG, "Sign Email private <<<");

    }


    public boolean verifyPassword() {

        Log.d(TAG, "verify Email <<<");

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=.,!])(?=\\S+$).{6,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(m_password);

        Log.d(TAG, "verify Email <<<");

        return matcher.matches();

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

    public void onClickNewUserEmail(View V) {

        Log.d(TAG, "new user >>>");

        Intent intent = new Intent(getApplicationContext(), Sign_Up.class);
        startActivity(intent);
        Log.d(TAG, "new user <<<");

    }

    private void updateUI(FirebaseUser currentUser, String wayOfConnection) {

        Log.d(TAG, "update UI >>>");

        if (currentUser == null) {
            //Toast message = new Toast(this);
            //message.makeText(this, "you don't exist", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getApplicationContext(), MyStorageProduct.class);
            if (wayOfConnection != null) {
                intent.putExtra(Constants.WAY_OF_CONNECTION, wayOfConnection);
            }
            startActivity(intent);
        }

        Log.d(TAG, "update UI >>>");

    }
}
