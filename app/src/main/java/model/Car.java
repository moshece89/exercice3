package model;

public class Car {
    private String car_Maker;
    private String car_Model;
    private String color;
    private String price;
    private int sku;
    private int stock;
    private int year;
    public Car(String car_Maker, String car_Model, String color, String price, int sku, int stock, int year) {
        this.car_Maker = car_Maker;
        this.car_Model = car_Model;
        this.color = color;
        this.price = price;
        this.sku = sku;
        this.stock = stock;
        this.year = year;
    }

    public Car() {
    }

    public String getCar_Maker() {
        return car_Maker;
    }

    public void setCar_Maker(String car_Maker) {
        this.car_Maker = car_Maker;
    }

    public String getCar_Model() {
        return car_Model;
    }

    public void setCar_Model(String car_Model) {
        this.car_Model = car_Model;
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

    public int getSku() {
        return sku;
    }

    public void setSku(int sku) {
        this.sku = sku;
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


}
