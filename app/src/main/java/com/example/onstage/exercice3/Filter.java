package com.example.onstage.exercice3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.HashMap;
import java.util.Map;

public class Filter extends AppCompatActivity {
    private static final String TAG = "Filter";
    private FirebaseAnalytics mFirebaseAnalytics;
    private FlurryAgent mFlurryAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate() >>");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Log.e(TAG, "onCreate() >>");


    }

    public void onClickFilter(View v)
    {
        Log.e(TAG, "onClickFilter() >>");

        final EditText minYear =findViewById(R.id.editText_minYear);
        final EditText maxYear =findViewById(R.id.editText_MaxYear);
        final EditText minPrice =findViewById(R.id.editText_minPrice);
        final EditText maxPrice =findViewById(R.id.editText_MaxPrice);

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.MIN_PRICE, Integer.parseInt(minPrice.getText().toString()));
        bundle.putInt(Constants.MAX_PRICE, Integer.parseInt(maxPrice.getText().toString()));
        bundle.putInt(Constants.MIN_YEAR, Integer.parseInt(minYear.getText().toString()));
        bundle.putInt(Constants.MAX_YEAR, Integer.parseInt(maxYear.getText().toString()));
        mFirebaseAnalytics.logEvent(Constants.SEARCH_PARAMETERS, bundle);

        Map<String, String> flurryData =  new HashMap<String, String>() { {
        put(Constants.MIN_PRICE, minPrice.getText().toString());
        put(Constants.MAX_PRICE, maxPrice.getText().toString());
        put(Constants.MIN_YEAR, minYear.getText().toString());
        put(Constants.MAX_PRICE, maxYear.getText().toString());
    } };


        mFlurryAgent.logEvent(Constants.SEARCH_PARAMETERS, flurryData);


        DatabaseToApplication.minPrice = Integer.parseInt(minPrice.getText().toString());
        DatabaseToApplication.maxPrice = Integer.parseInt(maxPrice.getText().toString());
        DatabaseToApplication.minYear = Integer.parseInt(minYear.getText().toString());
        DatabaseToApplication.maxYear = Integer.parseInt(maxYear.getText().toString());
        Intent intent =new Intent(getApplicationContext(), MyStorageProduct.class);
        startActivity(intent);
        Log.e(TAG, "onClickFilter() >>");

    }
}
