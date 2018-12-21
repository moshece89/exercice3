package model;

public class Product {
    public String getName_product() {
        return name_product;
    }

    public void setName_product(String name_product) {
        this.name_product = name_product;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    private String name_product ;
    private int id;
    private String currency ;
    private int stock ;
    private double price ;

    public Product(String name_product, int id, String currency, int stock, double price) {
        this.name_product = name_product;
        this.id = id;
        this.currency = currency;
        this.stock = stock;
        this.price = price;
    }

    public void print()
    {
        System.out.println("Name of product" + name_product + "\n");
        System.out.println("id" + id + "\n");
        System.out.println("Stock" + stock + "\n");
        System.out.println("price" + price + "\n");
        System.out.println("currency" + currency + "\n");
    }
}
