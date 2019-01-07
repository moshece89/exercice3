package com.example.onstage.exercice3;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import model.Car;

public class address extends AppCompatActivity {
    private Car myCar;
    private Handler m_Handler = new Handler();
    private FirebaseAnalytics mFirebaseAnalytics;
    private String TAG = "Address";

    //------------------------ we are not conducting any data verification checks since this is not part of the exercise, this is only added as a demonstration of user experience ---------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate() >>");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        final String maker = getIntent().getStringExtra(Constants.MAKER);
        final String ids = getIntent().getStringExtra(Constants.ID);
        myCar = DatabaseToApplication.myCars.getCarListToMaker().get(maker + ids);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Log.e(TAG, "onCreate() >>");
    }

    public void OnClickSubmit(View v) {

        Log.e(TAG, "onClickSubmit() >>");

        if(verifyInputs()) {
            Toast.makeText(this, Constants.SHIPPEDSUCCESS, Toast.LENGTH_LONG).show();
            myCar.sellCar();
            Bundle bundle = new Bundle();
            bundle.putString("car_maker", myCar.getCar_maker());
            bundle.putString("car_model", myCar.getCar_model());
            bundle.putString("car_ID", myCar.getId());
            bundle.putString("car_color", myCar.getColor());
            bundle.putString("car_price", myCar.getPrice());
            bundle.putString("CURRENCY", "Dollar $$");
            bundle.putDouble("VALUE", Double.parseDouble(myCar.getPrice().substring(1)));
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, bundle);
            m_Handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    returnToStorage();
                }
            }, 1000);
        }
        else {
            Toast.makeText(this, Constants.SHIPPEDNOTSUCCESS, Toast.LENGTH_LONG).show();
        }

        Log.e(TAG, "onClickSubmit() >>");

    }


    public void returnToStorage() {
        Log.e(TAG, "returnToStorage() >>");

        Intent intent = new Intent(getApplicationContext(), MyStorageProduct.class);
        startActivity(intent);
        Log.e(TAG, "returnToStorage() >>");
    }

    private boolean verifyInputs() {

        Log.e(TAG, "verifyInputs() >>");
        boolean isLegal = false;
        EditText current = findViewById(R.id.nameBox);

        if (checkIfLegal(current))
        {
            current = findViewById(R.id.lastNameBox);
            if (checkIfLegal(current))
            {
                current = findViewById(R.id.counrtyBox);
                if (checkIfLegal(current))
                {
                    current = findViewById(R.id.cityBox);
                    if (checkIfLegal(current))
                    {
                        current = findViewById(R.id.streenNameBox);
                        if (checkIfLegal(current))
                        {
                            current = findViewById(R.id.streetNumberBox);
                            if (current.getText().toString().length() >= 1)
                            {
                                current = findViewById(R.id.zipCodeBox);
                                if (current.getText().toString().length() >= 6 && current.getText().toString().length() <= 10)
                                {
                                    isLegal = true;
                                }
                            }
                        }
                    }
                }

            }

        }
        Log.e(TAG, "verifyInputs() >>");

        return isLegal;
    }

    private boolean checkIfLegal(EditText current) {

        Log.e(TAG, "checkIfLegal() >>");

        boolean isLegal = false;
        int length = current.getText().toString().length();

      if(length >=2 )
          if( current.getText().toString().matches("[a-zA-Z]+")) {
              isLegal = true;
          }

        Log.e(TAG, "checkIfLegal() >>");

      return isLegal;
    }
}


