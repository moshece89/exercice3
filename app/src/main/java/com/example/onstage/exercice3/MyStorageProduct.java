package com.example.onstage.exercice3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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
    private ArrayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_storage_product);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();


        DatabaseToApplication.mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mReference = DatabaseToApplication.mDatabase.getReference("cars");
        final TableLayout linearLayout = findViewById(R.id.layoutOption);
        DatabaseReference mReferenceUsers = DatabaseToApplication.mDatabase.getReference("users");

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

        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long value=dataSnapshot.getChildrenCount();
                Log.d(TAG,"no of children: "+value);

                Iterator<Map.Entry<String, Car>> iter;
                GenericTypeIndicator<List<Car>> genericTypeIndicator =new GenericTypeIndicator<List<Car>>(){};
                DatabaseToApplication.carList = dataSnapshot.getValue(genericTypeIndicator);
                DatabaseToApplication.myCars = new Cars(DatabaseToApplication.carList);
                TextView textView;
                ImageView imageview;
                StringBuilder sb;
                Button signout =findViewById(R.id.button_SignOut);
                Boolean makeClicked = ((RadioButton)findViewById(R.id.radioButton_Maker)).isChecked();
                TableRow tableRow ;


                if(!DatabaseToApplication.userListAuth.containsKey(FirebaseAuth.getInstance().getUid()))
                {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    User user1 = new User();
                    if (user != null) {
                        user1.setIdAuth(user.getUid());
                        user1.setId(DatabaseToApplication.userList.size()+1);
                        String key = DatabaseToApplication.mDatabase.getReference("users").push().getKey();
                        DatabaseToApplication.mDatabase.getReference("users").child(key).setValue(user1);
                    }
                }

                if(makeClicked)
                {
                    iter = DatabaseToApplication.myCars.getCarListToMaker().entrySet().iterator();
                }
                else {
                    iter = DatabaseToApplication.myCars.getCarListToModel().entrySet().iterator();
                }
                int i=0;
                while (iter.hasNext()) {
                    tableRow = new TableRow(getApplicationContext());
                    //tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    sb = new StringBuilder();
                    textView = new TextView(getApplicationContext());
                    imageview =new ImageView(getApplicationContext());
                    final Map.Entry<String, Car> entry = iter.next();
                    sb.append(i+"maker :");
                    i++;
                    sb.append(entry.getValue().getCar_maker());
                    sb.append("     model:");
                    sb.append(entry.getValue().getCar_model());
                    sb.append("     Color: ");
                    sb.append(entry.getValue().getColor());
                    sb.append("     Price: ");
                    sb.append(entry.getValue().getPrice());
                    sb.append("     ID: ");
                    sb.append(entry.getValue().getId());
                    sb.append("     Stock: ");
                    sb.append(entry.getValue().getStock());
                    sb.append("     Year: ");
                    sb.append(entry.getValue().getYear());
                    textView.setText(sb.toString());
                    Picasso.get().load(entry.getValue().getImage_URL()).fit().placeholder(R.mipmap.ic_launcher).into(imageview);

                    CardView cardView = new CardView(getApplicationContext());

                    //imageview.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    //textView.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

                    //tableRow.addView(imageview);
                    //tableRow.addView(textView);
                    textView.setVisibility(View.VISIBLE);
                    tableRow.addView(imageview);
                    tableRow.addView(textView);
                    //tableRow.setClickable(true);
                    tableRow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(),ProductSetting.class);
                            intent.putExtra(Constants.MAKER, entry.getValue().getCar_maker());
                            intent.putExtra(Constants.ID,entry.getValue().getId());
                            startActivity(intent);
                        }
                    });

                    linearLayout.addView(tableRow);
                    if (iter.hasNext()) {
                        sb.append(',').append(' ');
                    }
                }
                linearLayout.removeView(signout);
                linearLayout.addView(signout);
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
}
