package com.example.onstage.exercice3;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
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

public class ProductSetting extends AppCompatActivity {


    private CardView cardView;
    private TextView textView;
    private Car myCar;
    private LinearLayout linearLayout;
    private TextView newComment ;
    private Button submit ;
    private StringBuilder sb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_setting);

        final String maker = getIntent().getStringExtra(Constants.MAKER);
        final String ids =getIntent().getStringExtra(Constants.ID);
        myCar = DatabaseToApplication.myCars.getCarListToMaker().get(maker+ids);
        URL image_URL = null;
        linearLayout = findViewById(R.id.linearLayout);
        newComment = findViewById(R.id.editTextComment);
        submit = findViewById(R.id.submitButton);
        Button buy = findViewById(R.id.button_Buy);
        ImageView carImage;
        TextView makers, model, year, color , price, id;


        final TextView stock;
        makers =  findViewById(R.id.textView_Maker);
        model = findViewById(R.id.textView_Model);
        year = findViewById(R.id.textView_Year);
        color = findViewById(R.id.textView_Color);
        price = findViewById(R.id.textView_Price);
        stock = findViewById(R.id.textView_Stock);
        id = findViewById(R.id.textView_ID);
        carImage = findViewById(R.id.imageViewCarPic);

        makers.setText(myCar.getCar_maker());
        model.setText(myCar.getCar_model());
        year.setText(Integer.toString(myCar.getYear()));
        color.setText(myCar.getColor());
        price.setText(myCar.getPrice());
        stock.setText(Integer.toString(myCar.getStock()));

        Picasso.get().load(myCar.getImage_URL()).fit().placeholder(R.mipmap.ic_launcher).into(carImage);
        if(myCar.getStock() == 0)
        {
            buy.setEnabled(false);
        }
        else
        {
            buy.setEnabled(true);
        }


        Set keys = DatabaseToApplication.commentUserList.keySet();
        Iterator it = keys.iterator();
        Object key = it;
        DatabaseToApplication.userListAuth= new HashMap<>();
        while (it.hasNext()){
            key = it.next();
            Log.d("keykey",key.toString());
            Log.d("keykey",DatabaseToApplication.commentUserList.get(key).getComment());
            if(DatabaseToApplication.commentUserList.get(key).getIdOfProduct().compareTo(myCar.getId())==0)
            {
                cardView = new CardView(getApplicationContext());
                textView =new TextView(getApplicationContext());
                sb = new StringBuilder();
                sb.append("user ").append(DatabaseToApplication.commentUserList.get(key).getIdOfUser());
                sb.append(":    ").append(DatabaseToApplication.commentUserList.get(key).getComment());
                textView.setText(sb);
                cardView.addView(textView);
                linearLayout.addView(cardView);
            }

        }

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mReference = mDatabase.getReference("cars").child(Integer.toString(myCar.getRow()));
        DatabaseReference mReferenceUsers = DatabaseToApplication.mDatabase.getReference("users");

        mReferenceUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String,User>> genericTypeIndicator =new GenericTypeIndicator<HashMap<String, User>>(){};
                DatabaseToApplication.userList = dataSnapshot.getValue(genericTypeIndicator);
                updateIdFirebase(DatabaseToApplication.userList);
                DatabaseToApplication.users = new Users(DatabaseToApplication.userList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference mReferenceComments = DatabaseToApplication.mDatabase.getReference("comments");

        mReferenceComments.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String,CommentUser>> genericTypeIndicator =new GenericTypeIndicator<HashMap<String, CommentUser>>(){};
                DatabaseToApplication.commentUserList = dataSnapshot.getValue(genericTypeIndicator);
                Set keys = DatabaseToApplication.commentUserList.keySet();
                Iterator it = keys.iterator();
                Object key = it;
                while (it.hasNext()){
                    key = it.next();
                    Log.d("keykey",key.toString());
                    Log.d("keykey",DatabaseToApplication.commentUserList.get(key).getComment());
                    if(DatabaseToApplication.commentUserList.get(key).getIdOfProduct().compareTo(myCar.getId())==0)
                    {
                        cardView = new CardView(getApplicationContext());
                        textView =new TextView(getApplicationContext());
                        sb = new StringBuilder();
                        sb.append("user ").append(DatabaseToApplication.commentUserList.get(key).getIdOfUser());
                        sb.append(":    ").append(DatabaseToApplication.commentUserList.get(key).getComment());
                        textView.setText(sb);
                        cardView.addView(textView);
                        linearLayout.addView(cardView);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Car> genericTypeIndicator =new GenericTypeIndicator<Car>(){};
                myCar = dataSnapshot.getValue(genericTypeIndicator);
                stock.setText(Integer.toString(myCar.getStock()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void onClickBuy(View v)
    {
        myCar.sellCar();
        //add to paner or history
        returnToStorage();

    }
    public void onClickReturn(View v)
    {
        returnToStorage();
    }
    public void returnToStorage()
    {
        Intent intent = new Intent(getApplicationContext(), MyStorageProduct.class);
        startActivity(intent);
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
    private void updateIdFirebase(HashMap<String, User> userList) {
        Set keys = userList.keySet();
        Iterator it = keys.iterator();
        Object key = it;
        DatabaseToApplication.userListAuth = new HashMap<>();
        while (it.hasNext()) {
            key = it.next();
            Log.d("keykey", key.toString());
            Log.d("keykey", userList.get(key).getIdAuth());
            DatabaseToApplication.userListAuth.put(userList.get(key).getIdAuth(), userList.get(key));

        }
    }

    public void onClickSubmit(View v)
    {
        TextView comment = findViewById(R.id.editTextComment);
        String commentString = comment.getText().toString();
        String users = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String idOfCar = myCar.getId();
        CommentUser commentUser = new CommentUser(users, commentString, idOfCar);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String key = DatabaseToApplication.mDatabase.getReference("comments").push().getKey();
        DatabaseToApplication.commentUserList.put(key, commentUser);
        DatabaseToApplication.mDatabase.getReference("comments").child(key).setValue(commentUser);
    }
}
