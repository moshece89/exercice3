package com.example.onstage.exercice3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.analytics.FirebaseAnalytics;

public class Filter extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

    }

    public void onClickFilter(View v)
    {
        EditText minYear =findViewById(R.id.editText_minYear);
        EditText maxYear =findViewById(R.id.editText_MaxYear);
        EditText minPrice =findViewById(R.id.editText_minPrice);
        EditText maxPrice =findViewById(R.id.editText_MaxPrice);

        Bundle bundle = new Bundle();
        bundle.putString(Constants.MINPRICE, minPrice.getText().toString());
        bundle.putString(Constants.MAXPRICE, maxPrice.getText().toString());
        bundle.putString(Constants.MINYEAR, minYear.getText().toString());
        bundle.putString(Constants.MAXYEAR, maxYear.getText().toString());
        mFirebaseAnalytics.logEvent(Constants.SEARCHPARAMETER, bundle);

        DatabaseToApplication.minPrice = Integer.parseInt(minPrice.getText().toString());
        DatabaseToApplication.maxPrice = Integer.parseInt(maxPrice.getText().toString());
        DatabaseToApplication.minYear = Integer.parseInt(minYear.getText().toString());
        DatabaseToApplication.maxYear = Integer.parseInt(maxYear.getText().toString());
        Intent intent =new Intent(getApplicationContext(), MyStorageProduct.class);
        startActivity(intent);
    }
}
