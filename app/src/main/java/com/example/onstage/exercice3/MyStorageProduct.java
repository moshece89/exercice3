package com.example.onstage.exercice3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import model.Car;
import model.Cars;
import model.User;
import model.Users;

public class MyStorageProduct extends AppCompatActivity {
    private static final String TAG = "My storage Product";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private Iterator<Map.Entry<String, Car>> iter;
    private String searchString = null;
    private Boolean firstRun = true;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_storage_product);
        mAuth = FirebaseAuth.getInstance();

        DatabaseToApplication.mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mReferenceCars = DatabaseToApplication.mDatabase.getReference(Constants.CARS);
        DatabaseReference mReferenceUsers = DatabaseToApplication.mDatabase.getReference(Constants.USERS);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mReferenceUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String,User>> genericTypeIndicator =new GenericTypeIndicator<HashMap<String, User>>(){};
                DatabaseToApplication.userList = dataSnapshot.getValue(genericTypeIndicator);
                DatabaseToApplication.updateIdFirebase(DatabaseToApplication.userList);
                DatabaseToApplication.users = new Users(DatabaseToApplication.userList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mReferenceCars.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long value = dataSnapshot.getChildrenCount();
                Log.d(TAG, "no of children: " + value);

                GenericTypeIndicator<List<Car>> genericTypeIndicator = new GenericTypeIndicator<List<Car>>() {
                };
                DatabaseToApplication.carList = dataSnapshot.getValue(genericTypeIndicator);
                DatabaseToApplication.myCars = new Cars(DatabaseToApplication.carList);

//--------------------search-----------------------------
                EditText search = findViewById(R.id.editText_Search);
                search.addTextChangedListener(mtextWatcher);

                //------------new user or not

                if (!DatabaseToApplication.userListAuth.containsKey(FirebaseAuth.getInstance().getUid())) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    User user1 = new User();
                    if (user != null) {
                        user1.setIdAuth(user.getUid());
                        user1.setId(DatabaseToApplication.userList.size() + 1);
                        String key = DatabaseToApplication.mDatabase.getReference(Constants.USERS).push().getKey();
                        DatabaseToApplication.mDatabase.getReference(Constants.USERS).child(key).setValue(user1);
                    }
                }

                setTableLayout();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//------------------------google--------------------------
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



    }
    public void onSortButtonClick(View view) {
        // Is the button now checked?
        DatabaseToApplication.modelClicked = ((RadioButton)findViewById(R.id.radioButton_Model)).isChecked();

        setTableLayout();
    }
    private TextWatcher mtextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {

        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if((s.toString().compareTo("") == 0) || (s.toString().compareTo(" ") == 0))
            {
                searchString = null;
            }
            else {
                searchString = s.toString().toLowerCase();
                setTableLayout();
            }
        }
    };
    //------------ the are you sure dialog is only set to appear on the back button clicked since when clicking on log out we are assuming
    // that you are well aware that you are about to do so--------------------
    public void onClickSignOut(View V)
    {
        signOut();
    }

    private void signOut() {

        Log.e(TAG, "signOut() >>");

        googlesignOut();
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(getApplicationContext(), Sign_In.class);
        this.finish();
        startActivity(intent);
        Log.e(TAG, "signOut() <<");
    }

    private void googlesignOut() {

        Log.e(TAG, "onGooglesignOut() >>");
        if(mGoogleSignInClient != null) {

            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        }
                    });
        }

        Log.e(TAG, "onGooglesignOut() <<");
    }

    public void onClickFilter(View v)
    {
        Intent intent = new Intent(getApplicationContext(),Filter.class);
        startActivity(intent);
    }


    private void setTableLayout()
    {
        TableLayout tableLayout = findViewById(R.id.layoutOptions);
        TextView textView;
        ImageView imageview;
        StringBuilder sb;
        TableRow tableRow;
        firstRun = false;

        if (DatabaseToApplication.modelClicked) {
            iter = DatabaseToApplication.myCars.getCarListToModel().entrySet().iterator();
        } else {
            iter = DatabaseToApplication.myCars.getCarListToMaker().entrySet().iterator();
        }
        tableLayout.removeAllViews();
        while (iter.hasNext()) {

            final Map.Entry<String, Car> entry = iter.next();

            String strings = entry.getValue().getPrice().substring(1,entry.getValue().getPrice().length());
            double price = Double.parseDouble(strings);
            if ((DatabaseToApplication.maxYear >= entry.getValue().getYear()) && (DatabaseToApplication.minYear <= entry.getValue().getYear())) {

                if ((DatabaseToApplication.maxPrice >= price) && (DatabaseToApplication.minPrice <= price)) {
                    if (searchString != null) {
                        if ((entry.getValue().getCar_maker().toLowerCase().contains(searchString)) ||
                                (entry.getValue().getCar_model().toLowerCase().contains(searchString)) ||
                                (entry.getValue().getColor().toLowerCase().contains(searchString))) {
                            tableRow = new TableRow(getApplicationContext());
                            sb = new StringBuilder();
                            textView = new TextView(getApplicationContext());
                            imageview = new ImageView(getApplicationContext());
                            sb.append(entry.getValue().getCar_maker());
                            sb.append("\n");
                            sb.append(entry.getValue().getCar_model());
                            sb.append("     ");
                            sb.append(entry.getValue().getPrice());

                            textView.setText(sb.toString());
                            Picasso.get().load(entry.getValue().getImage_URL()).fit().placeholder(R.mipmap.ic_launcher).into(imageview);

                            CardView cardView = new CardView(getApplicationContext());

                            textView.setVisibility(View.VISIBLE);
                            tableRow.addView(imageview);
                            cardView.addView(textView);
                            tableRow.addView(cardView);
                            tableRow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), ProductSetting.class);
                                    intent.putExtra(Constants.MAKER, entry.getValue().getCar_maker());
                                    intent.putExtra(Constants.ID, entry.getValue().getId());
                                    startActivity(intent);
                                }
                            });

                            tableLayout.addView(tableRow);
                            if (iter.hasNext()) {
                                sb.append(',').append(' ');
                            }
                        }
                    }
                    else
                    {
                        tableRow = new TableRow(getApplicationContext());
                        sb = new StringBuilder();
                        textView = new TextView(getApplicationContext());
                        imageview = new ImageView(getApplicationContext());
                        sb.append(entry.getValue().getCar_maker());
                        sb.append("\n");
                        sb.append(entry.getValue().getCar_model());
                        sb.append("     ");
                        sb.append(entry.getValue().getPrice());

                        textView.setText(sb.toString());
                        Picasso.get().load(entry.getValue().getImage_URL()).fit().placeholder(R.mipmap.ic_launcher).into(imageview);

                        CardView cardView = new CardView(getApplicationContext());

                        textView.setVisibility(View.VISIBLE);
                        tableRow.addView(imageview);
                        cardView.addView(textView);
                        tableRow.addView(cardView);
                        tableRow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), ProductSetting.class);
                                intent.putExtra(Constants.MAKER, entry.getValue().getCar_maker());
                                intent.putExtra(Constants.ID, entry.getValue().getId());
                                startActivity(intent);
                            }
                        });

                        tableLayout.addView(tableRow);
                        if (iter.hasNext()) {
                            sb.append(',').append(' ');
                        }
                    }
                }
            }
        }
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Log out?");
        builder.setMessage("Are you sure you want to log out?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                signOut();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }
}
