package com.example.onstage.exercice3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class Filter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
    }

    public void onClickFilter(View v)
    {
        EditText minYear =findViewById(R.id.editText_minYear);
        EditText maxYear =findViewById(R.id.editText_MaxYear);
        EditText minPrice =findViewById(R.id.editText_minPrice);
        EditText maxPrice =findViewById(R.id.editText_MaxPrice);

        DatabaseToApplication.minPrice = Integer.parseInt(minPrice.getText().toString());
        DatabaseToApplication.maxPrice = Integer.parseInt(maxPrice.getText().toString());
        DatabaseToApplication.minYear = Integer.parseInt(minYear.getText().toString());
        DatabaseToApplication.maxYear = Integer.parseInt(maxYear.getText().toString());
        DatabaseToApplication.makeClicked = ((RadioButton)findViewById(R.id.radioButton_Maker)).isChecked();
        Intent intent =new Intent(getApplicationContext(), MyStorageProduct.class);
        startActivity(intent);


    }
}
