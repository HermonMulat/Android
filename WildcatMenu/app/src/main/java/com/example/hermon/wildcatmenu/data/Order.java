package com.example.hermon.wildcatmenu.data;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class Order {

    private String uid;
    private List<String> menuItems = new ArrayList<String>();
    private String customerName;

    public Order() {

    }

    public Order(String uid, List<String> foodID, Realm realmMenu) {
        this.uid = uid;

        menuItems = foodID;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<String> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<String> menuItems) {
        this.menuItems = menuItems;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
