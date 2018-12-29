package com.example.onstage.exercice3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.Set;

import model.Car;
import model.Cars;
import model.User;
import model.Users;

public class MyStorageProduct extends AppCompatActivity {
    private static final String TAG = "My storage Product";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_storage_product);
        mAuth = FirebaseAuth.getInstance();


        DatabaseToApplication.mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mReference = DatabaseToApplication.mDatabase.getReference("cars");
        final TableLayout linearLayout = findViewById(R.id.layoutOptions);
        final ConstraintLayout daddy = findViewById(R.id.layoutOption);
        DatabaseReference mReferenceUsers = DatabaseToApplication.mDatabase.getReference("users");

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

        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long value = dataSnapshot.getChildrenCount();
                Log.d(TAG, "no of children: " + value);

                Iterator<Map.Entry<String, Car>> iter;
                GenericTypeIndicator<List<Car>> genericTypeIndicator = new GenericTypeIndicator<List<Car>>() {
                };
                DatabaseToApplication.carList = dataSnapshot.getValue(genericTypeIndicator);
                DatabaseToApplication.myCars = new Cars(DatabaseToApplication.carList);
                TextView textView;
                ImageView imageview;
                StringBuilder sb;
                Button signout = findViewById(R.id.button_SignOut);
                TableRow tableRow;


                if (!DatabaseToApplication.userListAuth.containsKey(FirebaseAuth.getInstance().getUid())) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    User user1 = new User();
                    if (user != null) {
                        user1.setIdAuth(user.getUid());
                        user1.setId(DatabaseToApplication.userList.size() + 1);
                        String key = DatabaseToApplication.mDatabase.getReference("users").push().getKey();
                        DatabaseToApplication.mDatabase.getReference("users").child(key).setValue(user1);
                    }
                }

                if (DatabaseToApplication.makeClicked) {
                    iter = DatabaseToApplication.myCars.getCarListToMaker().entrySet().iterator();
                } else {
                    iter = DatabaseToApplication.myCars.getCarListToModel().entrySet().iterator();
                }
               /* Button filter =findViewById(R.id.button3);
                daddy.removeView(filter);
                linearLayout.removeView(filter);
                linearLayout.addView(filter);*/
                while (iter.hasNext()) {

                    final Map.Entry<String, Car> entry = iter.next();

                    String strings = entry.getValue().getPrice().substring(1,entry.getValue().getPrice().length());
                    Log.d("keykeykeykey",strings);
                    double price = Double.parseDouble(strings);
                    if ((DatabaseToApplication.maxYear >= entry.getValue().getYear()) && (DatabaseToApplication.minYear <= entry.getValue().getYear())) {

                        if ((DatabaseToApplication.maxPrice >= price) && (DatabaseToApplication.minPrice <= price)) {
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

                            linearLayout.addView(tableRow);
                            if (iter.hasNext()) {
                                sb.append(',').append(' ');
                            }
                        }
                     /*   linearLayout.removeView(signout);
                        linearLayout.addView(signout);*/
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }

    private void updateTable()
    {

    }

    public void onClickSignOut(View V)
    {
        Log.e(TAG, "signOut() >>");

        googlesignOut();
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(getApplicationContext(), Sign_In.class);
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


}
