package com.example.onstage.exercice3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

import model.Car;
import model.User;

public class MyAccount extends AppCompatActivity {

    private static final String TAG = "Account";
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        createActivitieForMyAccount();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();

        bundle.putString("Name", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        bundle.putString("Email", FirebaseAuth.getInstance().getCurrentUser().getEmail());
        bundle.putString("Email", FirebaseAuth.getInstance().getCurrentUser().getUid());

        mFirebaseAnalytics.logEvent("Access_account", bundle);
    }


    private void createActivitieForMyAccount() {
        Log.e(TAG, "createAcitivity() >>");


        TableLayout tableLayout = findViewById(R.id.CarInMyPossessionTable);
        TextView setting = findViewById(R.id.Setting_TextView);
        User user;
        String IdAuth = getIntent().getStringExtra(Constants.AUTHENTIFICATION);
        user = DatabaseToApplication.userListAuth.get(IdAuth);
        String settingString = "Name:   "+FirebaseAuth.getInstance().getCurrentUser().getDisplayName()+ "\n" +"Id Auth:    " + user.getIdAuth() + "\n";
        settingString += "Id:   " + user.getId() + "\n";
        settingString += "IdFirebase:   "  + user.getIdFirebase() + "\n";
        setting.setText(settingString);
        List<String> carInMyPossession = user.getCarsInMyPossesion();
        TextView textView;
        ImageView imageview;
        StringBuilder sb;
        TableRow tableRow;
        Car car;
        tableLayout.removeAllViews();
        for (String str:carInMyPossession
             ) {
            if(DatabaseToApplication.myCars.getCatalogue().containsKey(str)) {
                car = DatabaseToApplication.myCars.getCatalogue().get(str);
                tableRow = new TableRow(getApplicationContext());
                sb = new StringBuilder();
                textView = new TextView(getApplicationContext());
                imageview = new ImageView(getApplicationContext());
                sb.append(car.getCar_maker());
                sb.append("\n");
                sb.append(car.getCar_model());
                sb.append("     ");
                sb.append(car.getPrice());

                textView.setText(sb.toString());
                Picasso.get().load(car.getImage_URL()).fit().placeholder(R.mipmap.ic_launcher).into(imageview);

                CardView cardView = new CardView(getApplicationContext());

                textView.setVisibility(View.VISIBLE);
                tableRow.addView(imageview);
                cardView.addView(textView);
                tableRow.addView(cardView);
                final Car finalCar = car;
                tableRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), ProductSetting.class);
                        intent.putExtra(Constants.MAKER, finalCar.getCar_maker());
                        intent.putExtra(Constants.ID, finalCar.getId());
                        startActivity(intent);
                    }
                });

                tableLayout.addView(tableRow);

            }
        }
        Log.e(TAG, "createActivity() >>");
    }

    public void onClickReturnBack(View v)
    {
        Intent intent = new Intent(getApplicationContext(),MyStorageProduct.class);
        startActivity(intent);
    }
}

