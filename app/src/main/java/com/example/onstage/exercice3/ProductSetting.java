package com.example.onstage.exercice3;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import model.Car;

public class ProductSetting extends AppCompatActivity {

    private Car myCar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_setting);

        String maker = getIntent().getStringExtra(Constants.MAKER);
        String ids =getIntent().getStringExtra(Constants.ID);
        myCar = Constants.myCars.getCarListToMaker().get(maker+ids);
        URL image_URL = null;

        Button buy = findViewById(R.id.button_Buy);
        ImageView carImage;
        TextView makers, model, year, color , price, stock, id;
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
}
