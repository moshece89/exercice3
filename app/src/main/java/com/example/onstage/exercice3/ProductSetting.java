package com.example.onstage.exercice3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import model.Car;

public class ProductSetting extends AppCompatActivity {

    private Car myCar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_setting);

        String maker = getIntent().getStringExtra(Constants.MAKER);
        myCar = Constants.myCars.getCarListToMaker().get(maker);
        Button buy = findViewById(R.id.button_Buy);
        TextView makers, model, year, color , price, stock, id;
        makers =  findViewById(R.id.textView_Maker);
        model = findViewById(R.id.textView_Model);
        year = findViewById(R.id.textView_Year);
        color = findViewById(R.id.textView_Color);
        price = findViewById(R.id.textView_Price);
        stock = findViewById(R.id.textView_Stock);
        id = findViewById(R.id.textView_ID);

        makers.setText(myCar.getCar_Maker());
        model.setText(myCar.getCar_Model());
        year.setText(Integer.toString(myCar.getYear()));
        color.setText(myCar.getColor());
        price.setText(myCar.getPrice());
        stock.setText(Integer.toString(myCar.getStock()));
        id.setText(Integer.toString(myCar.getSku()));
        if(myCar.getStock() == 0)
        {
            buy.setEnabled(false);
        }
        else
        {
            buy.setEnabled(true);
        }
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
}
