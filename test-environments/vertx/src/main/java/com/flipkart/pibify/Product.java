package com.flipkart.pibify;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 02/10/24
 */
public class Product {
    String id, name;
    int weight;
    double price;

    public Product(String id, String name, double price, int weight) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.weight = weight;
    }

    public Product() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
