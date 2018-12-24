package com.example.onstage.exercice3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Car;
import model.Cars;

public class MyStorageProduct extends AppCompatActivity {
    private static final String TAG = "My storage Product";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_storage_product);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        final TextView textView= findViewById(R.id.textView_Test);
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mReference = mDatabase.getReference("cars");
        textView.setText("dsadaww");

        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String message="";
                long value=dataSnapshot.getChildrenCount();
                Log.d(TAG,"no of children: "+value);

                GenericTypeIndicator<List<Car>> genericTypeIndicator =new GenericTypeIndicator<List<Car>>(){};

                List<Car> taskDesList=dataSnapshot.getValue(genericTypeIndicator);

                for(int i=0;i<taskDesList.size();i++){
                    message += "TaskTitle = " + taskDesList.get(i).getCar_Maker();
                }
                textView.setText(message);
                //ArrayList<Car> value = dataSnapshot.getValue(Car);
                //Log.d("nextH",value.toString());
                /*Iterator I = dataSnapshot.getChildren().iterator();
                while (I.hasNext()) {
                    Log.d("nextH",I.toString());
                    try {
                        Cars cars = (((DataSnapshot) I.next()).getValue(Cars.class));
                        String message = "";
                        for (int i = 0; i < cars.getCarList().size(); i++) {
                            message += cars.getCarList().get(i).getCar_Model();
                        }
                        textView.setText(message);

                    }
                    catch(Exception e)
                    {
                        Log.d("nextH",e.toString());
                    }
                }*/
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

    public void onClickTest(View v)
    {
        final TextView textView= findViewById(R.id.textView_Test);
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mReference = mDatabase.getReference("cars");
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator I = dataSnapshot.getChildren().iterator();
                while (I.hasNext()) {
                    try {
                        Cars cars = (((DataSnapshot) I.next()).getValue(Cars.class));
                        String message = "";
                        for (int i = 0; i < cars.getCarList().size(); i++) {
                            message += cars.getCarList().get(i).getCar_Model();
                        }
                        textView.setText(message);
                    }
                    catch(Exception e)
                    {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
