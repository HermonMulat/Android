package com.example.hermon.wildcatmenu.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Food extends RealmObject{
    @PrimaryKey
    private String FoodID;
    private String name;
    private String description;
    private float price;
    private String category;

    public Food(){}

    public String getFoodID() {
        return FoodID;
    }

    public void setFoodID(String foodID) {
        FoodID = foodID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return String.format("$%.2f", price);
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
