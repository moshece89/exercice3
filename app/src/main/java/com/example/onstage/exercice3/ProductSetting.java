package com.example.onstage.exercice3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import model.Car;
import model.CommentUser;
import model.User;
import model.Users;

public class ProductSetting extends AppCompatActivity {


    private static final String TAG = "ProductSetting";
    private CardView cardView;
    private TextView textView;
    private Car myCar;
    private LinearLayout linearLayout;
    private TextView newComment;
    private Button submit;
    private StringBuilder sb;
    private FirebaseAuth mAuth;
    private FlurryAgent mFlurryAgent;
    private static int sportsCars = 0;
    private static int americanCars = 0;
    private static int electricCars = 0;
    private static int luxaryCars = 0;
    private static int trucksAndJeeps = 0;
    private static int japaneeseCars = 0;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate() >>");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_setting);
        mAuth = FirebaseAuth.getInstance();

        final String maker = getIntent().getStringExtra(Constants.MAKER);
        final String ids = getIntent().getStringExtra(Constants.ID);
        myCar = DatabaseToApplication.myCars.getCarListToMaker().get(maker + ids);
        linearLayout = findViewById(R.id.linearLayout2_test);
        newComment = findViewById(R.id.editTextComment);
        submit = findViewById(R.id.button_Submit);
        Button buy = findViewById(R.id.button_Buy);
        ImageView carImage;
        TextView model;
        double price = Double.parseDouble(myCar.getPrice().substring(1));
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        //----------------- texview and image picture button
        final TextView stock;
        model = findViewById(R.id.textView_Model);

        stock = findViewById(R.id.textView_Stock);
        carImage = findViewById(R.id.imageViewCarPic);
        StringBuilder sb1 = new StringBuilder();

        sb1.append(Constants.MAKERPRODUCT).append(myCar.getCar_maker()).append("\n");
        sb1.append(Constants.MODEL).append(myCar.getCar_model()).append("\n");
        sb1.append(Constants.YEAR).append(myCar.getYear()).append("\n");
        sb1.append(Constants.COLOR).append(myCar.getColor()).append("\n");
        if(Sign_In.loggedInWith == "facebook")
        {
            price *= 0.9;
            sb1.append("Facebook price:").append(price).append("\n");
        }
       else
           {
            sb1.append(Constants.PRICE).append(price).append("\n");
        }
        sb1.append(Constants.IDPRODUCT).append(myCar.getId()).append("\n");
        model.setText(sb1);


        Bundle bundle = new Bundle();
        bundle.putString("car_maker", myCar.getCar_maker());
        bundle.putString("car_model", myCar.getCar_model());
        bundle.putString("ITEM_NAME", myCar.getCar_model());
        bundle.putString("ITEM_ID", myCar.getId());
        bundle.putString("ITEM_CATEGORY", "car");
        bundle.putString("car_color", myCar.getColor());
        bundle.putDouble("PRICE", price);

        Map<String, String> flurryData = new HashMap<String, String>() {
            {
                put("car_maker", myCar.getCar_maker());
                put("car_model", myCar.getCar_model());
                put("car_ID", myCar.getId());
                put("car_color", myCar.getColor());
                put("car_price", myCar.getPrice());
            }
        };


        mFlurryAgent.logEvent("product_view", flurryData);

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
        if (myCar.getCar_maker() == "Maserati" || myCar.getCar_maker() == "Aston Martin" || myCar.getCar_maker() == "Jaguar" || myCar.getCar_maker() == "Alfa Romeo") {
            sportsCars++;
            if (isBiggest(sportsCars)) {
                mFirebaseAnalytics.setUserProperty("favourite_cars", "sports");

            }
        }

        else if (myCar.getCar_maker() == "Tesla") {
            electricCars++;
            if (isBiggest(electricCars)) {
                mFirebaseAnalytics.setUserProperty("favourite_cars", "electric");

            }
        }

        else if (myCar.getCar_maker() == "Ford" || myCar.getCar_maker() == "Dodge" || myCar.getCar_maker() == "Pontiac" || myCar.getCar_maker() == "Lincoln" || myCar.getCar_maker() == "GMC" || myCar.getCar_maker() == "Chevrolet" || myCar.getCar_maker() == "Chrysler" || myCar.getCar_maker() == "Buick" || myCar.getCar_maker() == "Plymouth" ) {
            americanCars++;
            if (isBiggest(americanCars)) {
                mFirebaseAnalytics.setUserProperty("favourite_cars", "american");

            }
        }
        else if (myCar.getCar_maker() == "Audi" || myCar.getCar_maker() == "Mercedes-Benz" || myCar.getCar_maker() == "Lexus") {
            luxaryCars++;
            if (isBiggest(luxaryCars)) {
                mFirebaseAnalytics.setUserProperty("favourite_cars", "luxary");

            }
        }
        else if (myCar.getCar_maker() == "Land Rover" || myCar.getCar_maker() == "Ram") {
            trucksAndJeeps++;
            if (isBiggest(trucksAndJeeps)) {
                mFirebaseAnalytics.setUserProperty("favourite_cars", "trucks");

            }
        }
            else {
            japaneeseCars++;
            if (isBiggest(japaneeseCars)) {
                mFirebaseAnalytics.setUserProperty("favourite_cars", "japanese");
            }
        }

        Picasso.get().load(myCar.getImage_URL()).fit().placeholder(R.mipmap.ic_launcher).into(carImage);
        if (myCar.getStock() == 0) {
            buy.setEnabled(false);
        } else {
            buy.setEnabled(true);
        }


        Set keys = DatabaseToApplication.commentUserList.keySet();
        Iterator it = keys.iterator();
        Object key = it;
        DatabaseToApplication.userListAuth = new HashMap<>();

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mReference = mDatabase.getReference(Constants.CARS).child(Integer.toString(myCar.getRow()));
        DatabaseReference mReferenceUsers = DatabaseToApplication.mDatabase.getReference(Constants.USERS);
        DatabaseReference mReferenceComments = DatabaseToApplication.mDatabase.getReference(Constants.COMMENTS);


        //----------------------- listener---------------------------
        mReferenceUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, User>> genericTypeIndicator = new GenericTypeIndicator<HashMap<String, User>>() {
                };
                DatabaseToApplication.userList = dataSnapshot.getValue(genericTypeIndicator);
                DatabaseToApplication.updateIdFirebase(DatabaseToApplication.userList);
                DatabaseToApplication.users = new Users(DatabaseToApplication.userList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mReferenceComments.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, CommentUser>> genericTypeIndicator = new GenericTypeIndicator<HashMap<String, CommentUser>>() {
                };
                DatabaseToApplication.commentUserList = dataSnapshot.getValue(genericTypeIndicator);
                Set keys = DatabaseToApplication.commentUserList.keySet();
                Iterator it = keys.iterator();
                Object key = it;
                while (it.hasNext()) {
                    key = it.next();
                    if (DatabaseToApplication.commentUserList.get(key).getIdOfProduct().compareTo(myCar.getId()) == 0) {
                        cardView = new CardView(getApplicationContext());
                        textView = new TextView(getApplicationContext());
                        sb = new StringBuilder();
                        sb.append("    ").append(DatabaseToApplication.commentUserList.get(key).getComment());
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
                GenericTypeIndicator<Car> genericTypeIndicator = new GenericTypeIndicator<Car>() {
                };
                myCar = dataSnapshot.getValue(genericTypeIndicator);
                stock.setText(Constants.STOCK + Integer.toString(myCar.getStock()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Log.e(TAG, "onCreate() >>");

    }

    public void onClickBuy(View v) {
        Log.e(TAG, "onClickBuy() >>");


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(Constants.BUY);
        builder.setMessage(Constants.CONFIRME_BUY);

        builder.setPositiveButton(Constants.YES, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                Bundle bundle = new Bundle();
                bundle.putString("car_maker", myCar.getCar_maker());
                bundle.putString("car_model", myCar.getCar_model());
                bundle.putString("car_ID", myCar.getId());
                bundle.putString("car_color", myCar.getColor());
                bundle.putString("car_price", myCar.getPrice());
                bundle.putString("CURRENCY", "Dollar $$");
                bundle.putDouble("VALUE", Double.parseDouble(myCar.getPrice().substring(1)));
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, bundle);

                buyCar();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(Constants.NO, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();


        Log.e(TAG, "onClickBuy() >>");


    }

    public void onClickReturn(View v) {
        returnToStorage();
    }

    public void returnToStorage() {
        Log.e(TAG, "returnStorage() >>");

        Intent intent = new Intent(getApplicationContext(), MyStorageProduct.class);
        startActivity(intent);
        Log.e(TAG, "returnStorage() >>");
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, Constants.SRCNAME);
            return d;
        } catch (Exception e) {
            return null;
        }
    }


    public void onClickSubmit(View v) {
        Log.e(TAG, "onClickSubmit() >>");

        TextView comment = findViewById(R.id.editTextComment);
        String commentString = comment.getText().toString();
        String users = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String idOfCar = myCar.getId();
        if ((commentString != null) || (commentString.compareTo("") != 0) || (commentString.compareTo(" ") != 0)) {
            CommentUser commentUser = new CommentUser(users, commentString, idOfCar);


            String key = DatabaseToApplication.mDatabase.getReference(Constants.COMMENTS).push().getKey();
            DatabaseToApplication.commentUserList.put(key, commentUser);
            DatabaseToApplication.mDatabase.getReference(Constants.COMMENTS).child(key).setValue(commentUser);

            Toast toast = new Toast(getApplicationContext());
            toast.makeText(ProductSetting.this, Constants.SUBMIT_MESSAGE, Toast.LENGTH_SHORT).show();

            cardView = new CardView(getApplicationContext());
            textView = new TextView(getApplicationContext());
            sb = new StringBuilder();
            sb.append("    ").append(DatabaseToApplication.commentUserList.get(key).getComment());
            textView.setText(sb);
            cardView.addView(textView);
            linearLayout.addView(cardView);
        }


        Log.e(TAG, "onClickSubmit() >>");

    }

    private void buyCar() {
        Log.e(TAG, "buyCar() >>");

        if (mAuth.getCurrentUser().isAnonymous()) {
            Intent intent = new Intent(getApplicationContext(), Sign_In.class);
            Toast.makeText(this, R.string.You_Are_anonymous_Toast_Message,
                    Toast.LENGTH_LONG).show();
            intent.putExtra(Constants.USERSETTING, Constants.ANONYMOUS);
            startActivity(intent);
        } else {
            String[] stock = ((TextView) findViewById(R.id.textView_Stock)).getText().toString().split(":");
            if (Integer.parseInt(stock[1]) == 0) {
                Toast toast = new Toast(getApplicationContext());
                toast.makeText(ProductSetting.this, Constants.OUTOFSTOCK, Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getApplicationContext(), MyStorageProduct.class);
                //Intent intent = new Intent (getApplicationContext(), address.class);
                //intent.putExtra(Constants.MAKER, getIntent().getStringExtra(Constants.MAKER));
                //intent.putExtra(Constants.ID, getIntent().getStringExtra(Constants.ID));
                myCar.sellCar();
                startActivity(intent);
            }
        }
        Log.e(TAG, "buyCar() >>");

    }

    private boolean isBiggest(int iCar)
    {
        boolean biggest = false;

        if (iCar >= sportsCars && iCar >= americanCars && iCar >= japaneeseCars && iCar >= luxaryCars && iCar >= trucksAndJeeps && iCar >= electricCars)
        {
            biggest = true;
        }

        return biggest;
    }
    public void onClickDemo(View v) {
        Log.e(TAG, "onClickDemo() >>");
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URLDEMO)));
        Log.d("Video", "Video Playing....");

        Log.e(TAG, "onClickDemo() >>");
    }

}
