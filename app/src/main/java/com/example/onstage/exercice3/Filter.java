package com.example.onstage.exercice3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.HashMap;
import java.util.Map;

public class Filter extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    private FlurryAgent mFlurryAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

    }

    public void onClickFilter(View v)
    {
        final EditText minYear =findViewById(R.id.editText_minYear);
        final EditText maxYear =findViewById(R.id.editText_MaxYear);
        final EditText minPrice =findViewById(R.id.editText_minPrice);
        final EditText maxPrice =findViewById(R.id.editText_MaxPrice);

        Bundle bundle = new Bundle();
        bundle.putInt("min_price", Integer.parseInt(minPrice.getText().toString()));
        bundle.putInt("max_price", Integer.parseInt(maxPrice.getText().toString()));
        bundle.putInt("min_year", Integer.parseInt(minYear.getText().toString()));
        bundle.putInt("max_year", Integer.parseInt(maxYear.getText().toString()));
        mFirebaseAnalytics.logEvent("search_parameters", bundle);

        Map<String, String> flurryData =  new HashMap<String, String>() { {
        put("min_price", minPrice.getText().toString());
        put("max_price", maxPrice.getText().toString());
        put("min_year", minYear.getText().toString());
        put("max_year", maxYear.getText().toString());
    } };


        mFlurryAgent.logEvent("search_parameters", flurryData);


        DatabaseToApplication.minPrice = Integer.parseInt(minPrice.getText().toString());
        DatabaseToApplication.maxPrice = Integer.parseInt(maxPrice.getText().toString());
        DatabaseToApplication.minYear = Integer.parseInt(minYear.getText().toString());
        DatabaseToApplication.maxYear = Integer.parseInt(maxYear.getText().toString());
       // DatabaseToApplication.modelClicked = ((RadioButton)findViewById(R.id.radioButton_Model)).isChecked();
        Intent intent =new Intent(getApplicationContext(), MyStorageProduct.class);
        startActivity(intent);
    }
}
