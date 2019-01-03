package com.example.onstage.exercice3;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import model.Car;

public class address extends AppCompatActivity {
    private Car myCar;
    private Handler m_Handler = new Handler();
    private FirebaseAnalytics mFirebaseAnalytics;

    //------------------------ we are not conducting any data verification checks since this is not part of the exercise, this is only added as a demonstration of user experience ---------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        final String maker = getIntent().getStringExtra(Constants.MAKER);
        final String ids = getIntent().getStringExtra(Constants.ID);
        myCar = DatabaseToApplication.myCars.getCarListToMaker().get(maker + ids);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

    }

    public void OnClickSubmit(View v) {
        if(verifyInputs())
        {
        Toast.makeText(this, Constants.SHIPPEDSUCCESS, Toast.LENGTH_LONG).show();
        myCar.sellCar();

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
    }


    public void returnToStorage() {
        Intent intent = new Intent(getApplicationContext(), MyStorageProduct.class);
        startActivity(intent);
    }

    private boolean verifyInputs() {
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
        return isLegal;
    }

    private boolean checkIfLegal(EditText current) {

        boolean isLegal = false;
        int length = current.getText().toString().length();
      if(length >=2 )
          if( current.getText().toString().matches("[a-zA-Z]+"))
              isLegal = true;

      return isLegal;
    }
}


