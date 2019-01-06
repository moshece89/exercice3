package model;

import android.util.Log;

import com.example.onstage.exercice3.DatabaseToApplication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

public class Car {
    private String car_maker;

    public String getImage_URL() {
        return image_URL;
    }

    public int getRow() {
        return row;
    }

    public String getId() {
        return id;
    }

    public FirebaseDatabase getmDatabase() {
        return mDatabase;
    }

    private String car_model;
    private String color;
    private String price;

    public void setImage_URL(String image_URL) {
        this.image_URL = image_URL;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setmDatabase(FirebaseDatabase mDatabase) {
        this.mDatabase = mDatabase;
    }

    private String image_URL;
    private int row;
    private String id;
    private int stock;
    private int year;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    public Car(String car_Maker, String car_Model, String color, String price, String id, int stock, int year, String image_URL, int row) {
        this.car_maker = car_Maker;
        this.car_model = car_Model;
        this.color = color;
        this.price = price;
        this.id = id;
        this.stock = stock;
        this.year = year;
        this.image_URL = image_URL;
        this.row = row;
    }

    public Car() {
    }

    public String getCar_maker() {
        return car_maker;
    }

    public void setCar_maker(String car_maker) {
        this.car_maker = car_maker;
    }

    public String getCar_model() {
        return car_model;
    }

    public void setCar_model(String car_model) {
        this.car_model = car_model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSku() {
        return id;
    }

    public void setSku(String sku) {
        this.id = sku;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCarPic(){return image_URL;}

    public void sellCar()
    {
        if(stock!=0)
        {
          //  stock--;
            DatabaseReference mReference = mDatabase.getReference("cars");

            String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseToApplication.userListAuth.get(id).updateTheCarInPossession(this.id);
            DatabaseReference mReferences =mDatabase.getReference("users");
            mReferences.setValue(DatabaseToApplication.userList);

            mReference.child(Integer.toString(this.row)).child("stock").setValue(--stock);
        }
    }
}
