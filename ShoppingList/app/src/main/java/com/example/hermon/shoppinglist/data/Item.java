package com.example.hermon.shoppinglist.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

import static java.lang.Float.parseFloat;

public class Item extends RealmObject {

    @PrimaryKey
    private String ItemID;
    private String name,category, description;
    private boolean purchased;
    private float priceEST;

    public Item(){
        this.purchased = false;
    }

    public Item (String itemName, String itemCategory,
                 String itemDescription, float itemPrice){
        this.name = itemName;
        this.description = itemDescription;
        this.priceEST = itemPrice;
        this.category = itemCategory;
        this.purchased = false;
    }



    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public String getPriceEST() {
        return String.format("%.2f $", this.priceEST);
    }

    public String getPrice(){
        return String.format("%.2f", this.priceEST);
    }


    public void setPriceEST(String priceEST) {

        if (priceEST.equals("")){
            this.priceEST = 0;
        }
        else {
            this.priceEST = parseFloat(priceEST);
        }

    }

    public String getItemID() {
        return ItemID;
    }
}
